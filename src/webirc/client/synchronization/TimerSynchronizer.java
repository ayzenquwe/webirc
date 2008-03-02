package webirc.client.synchronization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import webirc.client.MainSystem;
import webirc.client.WebIRC;

import java.util.List;

/**
 * @author Ayzen
 * @version 1.0 30.06.2006 14:28:47
 */
public class TimerSynchronizer extends Synchronizer {

  private static final int SYNCH_TIME = 5000;

  private SynchronizeServiceAsync service;

  private boolean connected = false;

  /**
   * Indicates if WebIRC is trying to synchronize with server
   */
  private boolean synchronization = false;

  private String messageToSend = null;

  /**
   * Timer for synchronization.
   */
  private Timer synchTimer;
  private int synchTime = 0;

  /**
   * The count of errors occured during the synchronization.
   */
  private int errors = 0;

  public TimerSynchronizer(SynchronizeListener listener) {
    super(listener);
    service = App.getInstance();

    synchTimer = new Timer() {
      public void run() {
        if (!synchronization)
          synchronize(null);
      }
    };

    fireReady();
  }

  public void connect(String host, int port) {
    if (connected)
      stop();
    synchronize("CONNECT " + host + ':' + port + "\r\n");
  }

  public void setSynchTime(int millis) {
    if (millis != 0) {
      synchTime = millis;
      restartTimer();
    }
    else synchTimer.cancel();
  }

  public void restartTimer() {
    synchTimer.cancel();
    if (synchTime > 0)
      synchTimer.scheduleRepeating(synchTime);
  }

  public void synchronize(String message) {
    if (synchTime == 0)
      setSynchTime(SYNCH_TIME);

    if (messageToSend == null || messageToSend.trim().length() == 0)
      messageToSend = message != null ? message + "\r\n" : null;
    else if (message != null && message.trim().length() > 0)
      messageToSend += message + "\r\n";
    // If WebIRC is processing synchronization then we need to defer sending command
    if (synchronization)
      return;

    MainSystem.log(">> Synchronization with server...");
    restartTimer();
    synchronization = true;
    service.synchronize(messageToSend, new AsyncCallback() {
      public void onSuccess(Object response) {
        MainSystem.log("<< Synchronization succeed.");
        synchronization = false;

        // If there is another message we need to send...
        if (messageToSend != null)
          synchronize(null);

        // Parsing the result
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
          if (errorType != SynchPacket.NO_ERROR)
            MainSystem.showFatalError(ErrorResolver.getMessage(errorType));
        }
        else {
          MainSystem.log("Server sent null or not SynchPacket.");
          // After 5 failures close connection
          if (++errors >= 5)
            MainSystem.showFatalError(WebIRC.errorMessages.incorrectResponse());
        }
      }

      public void onFailure(Throwable caught) {
        if (!connected)
          fireNotConnected();

        synchronization = false;
        MainSystem.log("<< Synchronization with server failed.");
        // After 5 failures close connection
        if (++errors >= 5)
          MainSystem.showFatalError(WebIRC.errorMessages.synchError(), WebIRC.DEBUG ? caught : null);
      }
    }

    );
    messageToSend = null;
  }

  public void stop() {
    setSynchTime(0);
    connected = false;
  }

  /**
   * Utility/Convinience class.
   * Use SynchronizeService.App.getInstance () to access static instance of SynchronizeServiceAsync
   */
  public static class App {
    private static SynchronizeServiceAsync app = null;

    public static synchronized SynchronizeServiceAsync getInstance() {
      if (app == null) {
        app = (SynchronizeServiceAsync) GWT.create(SynchronizeService.class);
        ((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "/SynchronizeService");
      }
      return app;
    }
  }

}