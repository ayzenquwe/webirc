package webirc.client.gui.tabbedpanel;

/**
 * @author Ayzen
 * @version 1.0 16.07.2006 15:43:34
 */
public interface SourcesTabbedEvents {
  /**
   * Adds a listener interface to receive click events.
   *
   * @param listener the listener interface to add
   */
  public void addTabListener(TabbedListener listener);

  /**
   * Removes a previously added listener interface.
   *
   * @param listener the listener interface to remove
   */
  public void removeTabListener(TabbedListener listener);
}
