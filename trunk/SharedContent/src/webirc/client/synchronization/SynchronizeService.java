package webirc.client.synchronization;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SynchronizeService extends RemoteService {

  public SynchPacket synchronize(String message);

}
