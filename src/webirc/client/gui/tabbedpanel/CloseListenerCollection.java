package webirc.client.gui.tabbedpanel;

import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 01.01.2007 16:37:54
 */
public class CloseListenerCollection extends Vector {

  /**
   * Fires a close event to all listeners.
   *
   * @param sender the widget sending the event.
   */
  public void fireClose(Widget sender) {
    for (Iterator it = iterator(); it.hasNext();) {
      CloseListener listener = (CloseListener) it.next();
      listener.onClose(sender);
    }
  }

}

