package webirc.client.synchronization;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Packet with synchronization information from server to client.
 *
 * @author Ayzen
 * @version 1.0 09.02.2007 11:11:42
 */
public class SynchPacket implements IsSerializable {

  /**
   * Synchronization withou errors.
   */
  public static final int NO_ERROR = 0;
  /**
   * Server doesn't connected to an IRC server.
   */
  public static final int NOT_CONNECTED_ERROR = 1;
  /**
   * Server doesn't has information about client (no session).
   */
  public static final int NO_HANDLER_ERROR = 2;
  /**
   * Other synchronization errors.
   */
  public static final int SYNCH_ERROR = 3;
  /**
   * Fatal synchronization error.
   */
  public static final int FATAL_SYNCH_ERROR = 4;
  /**
   * Server couldn't connect to an IRC
   */
  public static final int COULDNT_CONNECT_ERROR = 5;
  /**
   * Server has been disconnected from an IRC
   */
  public static final int DISCONNECTED_ERROR = 6;

  /**
   * Type of an error that could occures while synchronizing.
   */
  private int errorType = NO_ERROR;
  /**
   * Messages from an IRC server.
   */
  private String messages = null;

  public SynchPacket() {
  }

  public int getErrorType() {
    return errorType;
  }

  public void setErrorType(int errorType) {
    this.errorType = errorType;
  }

  public String getMessages() {
    return messages;
  }

  public void setMessages(String messages) {
    this.messages = messages;
  }

}
