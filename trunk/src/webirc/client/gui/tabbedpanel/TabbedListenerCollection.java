package webirc.client.gui.tabbedpanel;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 16.07.2006 15:39:37
 */
public class TabbedListenerCollection extends Vector {

  /**
   * Fires a beforeTabSelected event to all listeners.
   *
   * @param sender the widget sending the event
   * @param tabIndex the index of the tab being selected
   */
  public boolean fireBeforeTabSelected(SourcesTabbedEvents sender, int tabIndex) {
    for (Iterator it = iterator(); it.hasNext();) {
      TabbedListener listener = (TabbedListener) it.next();
      if (!listener.onBeforeTabSelected(sender, tabIndex))
        return false;
    }
    return true;
  }

  /**
   * Fires a tabSelected event to all listeners.
   *
   * @param sender the widget sending the event
   * @param tabIndex the index of the tab being selected
   */
  public void fireTabSelected(SourcesTabbedEvents sender, int tabIndex) {
    for (Iterator it = iterator(); it.hasNext();) {
      TabbedListener listener = (TabbedListener) it.next();
      listener.onTabSelected(sender, tabIndex);
    }
  }

  public void fireTabClosed(SourcesTabbedEvents sender, int index) {
    for (Iterator it = iterator(); it.hasNext();) {
      TabbedListener listener = (TabbedListener) it.next();
      listener.onTabClosed(sender, index);
    }
  }

  public boolean fireBeforeTabClosed(SourcesTabbedEvents sender, int tabIndex) {
    for (Iterator it = iterator(); it.hasNext();) {
      TabbedListener listener = (TabbedListener) it.next();
      if (!listener.onBeforeTabClosed(sender, tabIndex))
        return false;
    }
    return true;
  }

}
