package webirc.client.synchronization;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Ayzen
 * @version 1.0 23.01.2007 19:38:22
 */
public interface ContSynchService extends RemoteService {

   public SynchPacket synchronize(String message);

}
