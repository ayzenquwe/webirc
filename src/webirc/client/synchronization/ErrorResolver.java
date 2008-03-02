package webirc.client.synchronization;

import webirc.client.WebIRC;

/**
 * @author Ayzen
 * @version 1.0 09.02.2007 17:16:30
 */
public class ErrorResolver {

  public static String getMessage(int errorType) {
    switch (errorType) {
      case SynchPacket.NOT_CONNECTED_ERROR:
        return WebIRC.errorMessages.notConnectedError();
      case SynchPacket.NO_HANDLER_ERROR:
        return WebIRC.errorMessages.noHandler();
      case SynchPacket.SYNCH_ERROR:
        return WebIRC.errorMessages.internalSynchError();
      case SynchPacket.FATAL_SYNCH_ERROR:
        return WebIRC.errorMessages.internalFatalSynchError();
      case SynchPacket.COULDNT_CONNECT_ERROR:
        return WebIRC.errorMessages.connectError();
      case SynchPacket.DISCONNECTED_ERROR:
        return WebIRC.errorMessages.disconnectedError();
    }
    return null;
  }

}
