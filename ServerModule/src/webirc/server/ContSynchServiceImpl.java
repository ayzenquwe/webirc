package webirc.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.log4j.Logger;
import webirc.client.synchronization.ContSynchService;
import webirc.client.synchronization.SynchPacket;
import webirc.server.exceptions.SynchException;
import webirc.server.exceptions.ThreadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.ConnectException;
import java.net.SocketException;

/**
 * @author Ayzen
 * @version 1.0 23.01.2007 19:38:22
 */
public class ContSynchServiceImpl extends RemoteServiceServlet implements ContSynchService {
  /**
   * Continuation servlet will drop connection after this time.
   */
  private static final long WAIT_TIME = 270000; // 4.5 minutes
  /**
   * Continuation servlet will drop connection after this time (Opera fix).
   */
  private static final long OPERA_WAIT_TIME = 120000; // 2 minutes

  public SynchPacket synchronize(String message) {
    HttpServletRequest request = getThreadLocalRequest();
    HttpSession session = request.getSession();
    // Getting session logger
    Logger logger = Utils.getLogger("continuation", session, request);

    logger.trace("Request from client");

    SynchPacket response = new SynchPacket();

    IRCConnectHandler handler = (IRCConnectHandler) session.getAttribute("irc");

    try {
      if (message != null && message.trim().length() > 0) {
        // Connecting to an IRC server
        if (message.startsWith("CONNECT")) {
          int index = message.indexOf('\r');
          // Deleting the word "CONNECT" at the beginning of message
          String connectMessage = message.substring(8, index);

          // Getting the user's IP and converting it to hex
          String hexedIp = Utils.getHexedIP(request.getRemoteAddr());

          // USER - is client's IP address in hex
          message = message.substring(index + 2).replaceFirst("user_name", hexedIp);

          // Close previous connection if it exists
          if (handler != null)
            handler.close();

          // Creating IRCConnectHandler and putting it in session attribute
          logger.trace("Creating an IRC handler for client");
          Logger historyLogger = Logger.getLogger("history." + request.getRemoteAddr() + '.' + session.getId());

          handler = new IRCConnectHandler(logger, historyLogger);
          session.setAttribute("irc", handler);
          handler.connect(connectMessage);
        }
        else if (handler == null)
          throw new SynchException("Client is trying to send a message, but IRCHandler is not initialized",
                                   SynchPacket.NO_HANDLER_ERROR);

        if (message.trim().length() > 0)
          handler.send(message);
      }

      if (handler == null)
        throw new SynchException("IRCHandler is not initialized for this client", SynchPacket.NO_HANDLER_ERROR);

      boolean waitForData;
      // Checking if we need to end previous connection
      synchronized (session) {
        // Getting previos connection's thread, which is usually waiting
        Thread prevThread = (Thread) session.getAttribute("ajax");
        if (prevThread != null) {
          prevThread.interrupt();
          // Checking if previous connection is already finished
          if (session.getAttribute("ajax") != null) try {
            // If not, then we should wait some time
            synchronized (prevThread) {
              prevThread.wait(10000);
            }
          }
          catch (InterruptedException e) {
            throw new ThreadException("Thread interrupted while waiting another thread.");
          }
          if (session.getAttribute("ajax") != null)
            // Something wrong - another thread should be dead after 30 seconds...
            throw new ThreadException("Another synch thread is not finished.");
        }

        // Checking if server not connected to an IRC or was disconnected
        if (!handler.isConnected()) {
          if (handler.isWasConnected())
            throw new SynchException("Disconnected from the IRC", SynchPacket.DISCONNECTED_ERROR);
          else
            throw new SynchException("Not connected to an IRC", SynchPacket.NOT_CONNECTED_ERROR);
        }

        String messages = handler.getMessagesList();
        response.setMessages(messages);

        // If there is a data for client we don't need to wait
        if (messages == null || messages.trim().length() == 0) {
          waitForData = true;
          session.setAttribute("ajax", Thread.currentThread());
        } else
          waitForData = false;
      }

      // Main loop which wait for messages from server or thread interruption
      if (waitForData) try {
        synchronized (handler) {
          // We need to drop connection, otherwise session will be destroyed
          long waitTime = Utils.isOpera(request) ? OPERA_WAIT_TIME : WAIT_TIME;
          logger.trace("Waiting " + waitTime + " millis for messages from server...");
          handler.wait(waitTime);
          logger.trace("End of waiting");
        }
        response.setMessages(handler.getMessagesList());
        session.setAttribute("ajax", null);

        if (!handler.isConnected())
          response.setErrorType(SynchPacket.DISCONNECTED_ERROR);
      }
      catch (InterruptedException e) {
        logger.trace("Waiting for messages has been interrupted");
        Thread thisThread = Thread.currentThread();
        response.setMessages(handler.getMessagesList());
        session.setAttribute("ajax", null);
        // Notify thread which interrupted this thread
        synchronized (thisThread) {
          thisThread.notify();
        }
      }

    }
    catch (SynchException e) {
      logger.error(e.getMessage());
      response.setErrorType(e.getErrorType());
    }
    catch (ConnectException e) {
      logger.error(e.getMessage());
      response.setErrorType(SynchPacket.COULDNT_CONNECT_ERROR);
    }
    catch (SocketException e) {
      logger.error(e.getMessage());
      response.setErrorType(SynchPacket.DISCONNECTED_ERROR);
    }
    catch (ThreadException e) {
      logger.fatal(e.getMessage());
      response.setErrorType(SynchPacket.FATAL_SYNCH_ERROR);
    }
    catch (Exception e) {
      logger.error(e.getMessage(), e);
      response.setErrorType(SynchPacket.SYNCH_ERROR);
    }

    return response;
  }

}