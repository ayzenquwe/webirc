package webirc.client.gui.menu;

import webirc.client.gui.DecoratedPopup;
import com.google.gwt.user.client.Window;

/**
 * @author Ayzen
 * @version 1.0 25.02.2007 14:03:23
 */
public class ContextMenu implements MenuListener {

  private DecoratedPopup popup = new DecoratedPopup(true);
  private MenuBar menu;

  public ContextMenu() {
  }

  public ContextMenu(MenuBar menu) {
    setMenu(menu);
  }

  public void show(int x, int y) {
    popup.setPopupPosition(-10, -10);
    popup.show();

    if (x + popup.getOffsetWidth() > Window.getClientWidth())
      x = x - popup.getOffsetWidth();
    if (y + popup.getOffsetHeight() > Window.getClientHeight())
      y = Window.getClientHeight() - popup.getOffsetHeight(); 

    popup.setPopupPosition(x, y);
  }

  public MenuBar getMenu() {
    return menu;
  }

  public void setMenu(MenuBar menu) {
    if (this.menu != null)
      this.menu.removeFromParent();
    this.menu = menu;
    menu.addMenuListener(this);
    popup.add(menu);
  }

  /**
   * Hiding context menu popup panel when menu is become closed.
   */
  public void onMenuClosed() {
    popup.hide();
  }

}
