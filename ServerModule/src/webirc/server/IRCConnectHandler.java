package webirc.server;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class IRCConnectHandler extends Thread {

  public static final int NOT_CONNECTED = 0;
  public static final int CONNECTED = 1;

  private int state = NOT_CONNECTED;
  private boolean wasConnected = false;

  public static final String PING = "PING";
  public static final String PONG = "PONG";

  private Socket socket = null;

  private BufferedReader in;
  private Writer out;

  private String encodingIn = "Windows-1251";
  private String encodingOut = "Windows-1251";

  private Logger logger;
  private Logger historyLogger;

  public IRCConnectHandler(Logger logger, Logger historyLogger) {
    this.logger = logger;
    this.historyLogger = historyLogger;
  }

  /**
   * String that used for message storage
   */
  private StringBuffer messagesList = new StringBuffer();

  public void connect(String connectMessage) throws IOException {
    int index = connectMessage.indexOf(':');
    String host = connectMessage.substring(0, index);
    int port = Integer.parseInt(connectMessage.substring(index + 1));
    if (state != NOT_CONNECTED) // otherwise disconnected or connect
      throw new SocketException("Socket closed or already open (" + state + ")");
    socket = null;
    try {
      logger.info("Connecting to " + host + ":" + port);
      socket = new Socket(host, port);
    }
    catch (IOException e) {
      if (socket != null)
        socket.close();
      socket = null;
      throw e;
    }

    prepare();
  }

  protected void prepare() throws IOException {
    if (socket == null)
      throw new SocketException("Socket is null, not connected");
    state = CONNECTED;
    in  = new BufferedReader(new InputStreamReader(socket.getInputStream(),
            encodingIn));
    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),
            encodingOut));
    wasConnected = true;
    start();
  }

  public void send(String line) throws IOException {
    if (state == CONNECTED) {
      historyLogger.info(line);
      out.write(line);
      out.flush();
    }  
  }

  public void run() {
    try {
      String line;
      boolean needNotify = false;
      while (!isInterrupted()) {
        line = in.readLine();
        if (line != null) {
          historyLogger.info(line);
          if (line.startsWith(PING))
            send(PONG + " :" + line.substring(line.indexOf(":") + 1) + "\r\n");
          else {
            if (messagesList.length() != 0)
              messagesList.append('\n');
            messagesList.append(line);
            needNotify = true;
          }
          if (!in.ready() && needNotify) {
            needNotify = false;
            logger.trace("Received messages from server");
            notifyClient();
          }
        }
        else {
          logger.warn("Line from IRC server is null");
          close();
          notifyClient();
        }
      }
      logger.info("IRCHandler thread has been interrupted");
    } 
    catch (IOException exc) {
      logger.error("Exception while working with socket " + socket, exc);
      close();
      notifyClient();
    }
  }

  private synchronized void notifyClient() {
    notify();
  }

  public void close() {
    if (state == NOT_CONNECTED) {
      logger.info("Socket is already closed");
      return;
    }

    state = NOT_CONNECTED;
    interrupt();
    try {
      in.close();
      out.close();
      socket.close();
    }
    catch (IOException e) {
      logger.error("Exception during socket close", e);
    }

    logger.info("Socket has been closed. (" + socket.toString() + ")");
  }

  /**
   * Gets all messages and clears messagesList
   *
   * @return all server messages
   * @throws java.net.SocketException throws commonly if messageList is null or empty
   */
  public String getMessagesList() throws SocketException {
    if (messagesList == null)
      throw new SocketException("The messageList in IRCHandler is null");
    if (messagesList.length() == 0 && state == NOT_CONNECTED)
      throw new SocketException("Not connected to IRC server (state=" + state + ";list=" +
                                messagesList.length() + ")");

    String result = messagesList.toString();
    messagesList.setLength(0);
    return result;
  }

  public boolean isConnected() {
    return state != NOT_CONNECTED;
  }

  public boolean isWasConnected() {
    return wasConnected;
  }

  public void setWasConnected(boolean wasConnected) {
    this.wasConnected = wasConnected;
  }
  
}

