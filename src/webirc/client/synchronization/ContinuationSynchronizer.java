package webirc.client.synchronization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import webirc.client.MainSystem;
import webirc.client.WebIRC;

import java.util.List;

/**
 * @author Ayzen
 * @version 1.0 23.01.2007 21:09:22
 */
public class ContinuationSynchronizer extends Synchronizer {

  private static final int TIMEOUT = 300000; // 5 minutes

  private ContSynchServiceAsync service;

  private boolean connected = false;
  private int pendingSynchs = 0;
  private int failures = 0;
  private int errors = 0;

  public ContinuationSynchronizer(SynchronizeListener listener) {
    super(listener);
    service = App.getInstance();
    fireReady();
  }

  public void connect(String host, int port) {
    if (connected)
      stop();

    synchronize("CONNECT " + host + ':' + port + "\r\n");
  }

  public void synchronize(final String message) {
    MainSystem.log("Synchronizing with server... (pending synchs: " + pendingSynchs + ")");
    pendingSynchs++;

    String msg;
    if (message != null && message.length() > 0)
      msg = message + "\r\n";
    else
      msg = message;
    service.synchronize(msg, new AsyncCallback() {
      public void onSuccess(Object response) {
        if (failures > 0)
          failures = 0;
        pendingSynchs--;
        MainSystem.log("Synchronization with server succeded. (pending synchs: " + pendingSynchs + ")");

        // Parsing the response
        if (response != null && response instanceof SynchPacket) {
          if (errors > 0)
            errors = 0;

          SynchPacket packet = ((SynchPacket) response);
          String messages = packet.getMessages();
          int errorType = packet.getErrorType();

          if (!connected && errorType == SynchPacket.NO_ERROR) {
            connected = true;
            fireConnected();
          }

          // If there are messages from IRC server we should parse them
          if (messages != null && messages.length() > 0) {
            List list = parser.parseMessages(messages);
            notifyCommandListeners(list.iterator());
          }

          // If errors occured while synchronizing show dialog with fatal error
          if (errorType == SynchPacket.NO_ERROR)
            synchronize();
          else
            MainSystem.showFatalError(ErrorResolver.getMessage(errorType));

        }
        else {
          MainSystem.log("Server sent null or not SynchPacket.");
          // After 5 failures close connection
          if (++errors >= 5)
            MainSystem.showFatalError(WebIRC.errorMessages.incorrectResponse());
          else
            synchronize();
        }
      }

      public void onFailure(Throwable caught) {
        if (!connected)
          fireNotConnected();
        pendingSynchs--;
        MainSystem.log("Synchronization with server failed. (pending synchs: " + pendingSynchs + ")");
        if (++failures > 5)
          MainSystem.showFatalError(WebIRC.errorMessages.synchError(), WebIRC.DEBUG ? caught : null);
        else // try again
          synchronize();
      }
    });
  }

  public void stop() {
    connected = false;
  }

  private void synchronize() {
    if (pendingSynchs == 0)
      synchronize("");
  }

  /**
   * Utility/Convinience class.
   * Use ContSynchService.App.getInstance() to access static instance of ContSynchServiceAsync
   */
  public static class App {
    private static ContSynchServiceAsync ourInstance = null;

    public static synchronized ContSynchServiceAsync getInstance() {
      if (ourInstance == null) {
        ourInstance = (ContSynchServiceAsync) GWT.create(ContSynchService.class);
        ((ServiceDefTarget) ourInstance).setServiceEntryPoint(GWT.getModuleBaseURL() + "/ContSynchService");
      }
      return ourInstance;
    }
  }

}
