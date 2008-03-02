package webirc.client.synchronization;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SynchronizeServiceAsync {
  void synchronize(String message, AsyncCallback async);
}
