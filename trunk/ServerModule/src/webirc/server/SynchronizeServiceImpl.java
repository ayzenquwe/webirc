package webirc.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.log4j.Logger;
import webirc.client.synchronization.SynchPacket;
import webirc.client.synchronization.SynchronizeService;
import webirc.server.exceptions.SynchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.ConnectException;
import java.net.SocketException;

public class SynchronizeServiceImpl extends RemoteServiceServlet implements SynchronizeService {

  public SynchPacket synchronize(String message) {
    HttpServletRequest request = getThreadLocalRequest();
    HttpSession session = request.getSession();
    // Getting session logger
    Logger logger = Utils.getLogger("timer", session, request);

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

      // Checking if server not connected to an IRC or was disconnected
      if (!handler.isConnected()) {
        if (handler.isWasConnected())
          throw new SynchException("Disconnected from the IRC", SynchPacket.DISCONNECTED_ERROR);
        else
          throw new SynchException("Not connected to an IRC", SynchPacket.NOT_CONNECTED_ERROR);
      }

      response.setMessages(handler.getMessagesList());

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
      response.setErrorType(SynchPacket.NOT_CONNECTED_ERROR);
    }
    catch (Exception e) {
      logger.error(e.getMessage(), e);
      response.setErrorType(SynchPacket.SYNCH_ERROR);
    }

    return response;
  }

}
