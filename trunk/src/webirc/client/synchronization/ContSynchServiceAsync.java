package webirc.client.synchronization;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Ayzen
 * @version 1.0 23.01.2007 19:38:22
 */
public interface ContSynchServiceAsync {

  public void synchronize(String message, AsyncCallback async);

}
