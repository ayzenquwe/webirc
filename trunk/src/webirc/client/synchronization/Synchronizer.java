package webirc.client.synchronization;

import webirc.client.commands.CommandListener;
import webirc.client.commands.CommandListenerCollection;
import webirc.client.commands.CommandParser;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Command;

/**
 * @author Ayzen
 * @version 1.0 21.01.2007 16:28:56
 */
public abstract class Synchronizer {

  private ArrayList connectionListeners;

  protected CommandParser parser = new CommandParser();
  private CommandListenerCollection commandListeners;

  protected Synchronizer(SynchronizeListener listener) {
    addConnectionListener(listener);
  }

  public abstract void connect(String host, int port);

  public abstract void synchronize(String message);

  public abstract void stop();

  protected void fireReady() {
    if (connectionListeners != null) {
      DeferredCommand.add(new Command() {
        public void execute() {
          for (int i = 0; i < connectionListeners.size(); i++) {
            SynchronizeListener listener = (SynchronizeListener) connectionListeners.get(i);
            listener.onReady();
          }
        }
      });
    }
  }

  protected void fireConnected() {
    if (connectionListeners != null) {
      for (int i = 0; i < connectionListeners.size(); i++) {
        SynchronizeListener listener = (SynchronizeListener) connectionListeners.get(i);
        listener.onConnected();
      }
    }
  }

  protected void fireNotConnected() {
    if (connectionListeners != null) {
      for (int i = 0; i < connectionListeners.size(); i++) {
        SynchronizeListener listener = (SynchronizeListener) connectionListeners.get(i);
        listener.onNotConnected();
      }
    }
  }

  protected void notifyCommandListeners(Iterator it) {
    if (commandListeners != null)
      commandListeners.notifyListeners(it);
  }

  public void addConnectionListener(SynchronizeListener listener) {
    if (connectionListeners == null)
      connectionListeners = new ArrayList();
    connectionListeners.add(listener);
  }

  public void removeConnectionListener(SynchronizeListener listener) {
    if (connectionListeners != null)
      connectionListeners.remove(listener);
  }

  public void addCommandListener(CommandListener listener) {
    if (commandListeners == null)
      commandListeners = new CommandListenerCollection();
    commandListeners.add(listener);
  }

  public void removeCommandListener(CommandListener listener) {
    commandListeners.remove(listener);
  }

}
