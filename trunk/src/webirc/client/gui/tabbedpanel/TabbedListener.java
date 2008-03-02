package webirc.client.gui.tabbedpanel;

/**
 * @author Ayzen
 * @version 1.0 16.07.2006 15:33:18
 */
public interface TabbedListener {

  /**
   * Fired just before a tab is selected.
   *
   * @param sender the widget whose tab was selected.
   * @param tabIndex the index of the tab about to be selected.
   * @return <code>false</code> to disallow the selection. If any listener
   *         returns false, then the selection will be disallowed.
   */
  public boolean onBeforeTabSelected(SourcesTabbedEvents sender, int tabIndex);

  /**
   * Fired when a tab is selected.
   *
   * @param sender the widget whose tab was selected.
   * @param tabIndex the index of the tab that was selected.
   */
  public void onTabSelected(SourcesTabbedEvents sender, int tabIndex);

  /**
   * Fired just before a tab is going to be closed.
   *
   * @param sender the widget whose tab was selected.
   * @param tabIndex the index of the tab about to be selected.
   */
  public boolean onBeforeTabClosed(SourcesTabbedEvents sender, int tabIndex);

  /**
   * Fired when a tab is going to be closed.
   *
   * @param sender the widget whose tab was selected.
   */
  public void onTabClosed(SourcesTabbedEvents sender, int index);

}
