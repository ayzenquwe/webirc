package webirc.client.synchronization;

/**
 * @author Ayzen
 */
public interface SynchronizeListener {

  public void onReady();

  public void onConnected();

  public void onNotConnected();

}
