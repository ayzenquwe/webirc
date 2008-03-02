package webirc.client.gui.menu;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.*;

import java.util.Vector;

import webirc.client.gui.DecoratedPopup;
import webirc.client.utils.Utils;

/**
 * A standard menu bar widget. A menu bar can contain any number of menu items,
 * each of which can either fire a {@link com.google.gwt.user.client.Command} or
 * open a cascaded menu bar.
 * <p/>
 * <p/>
 * <img class='gallery' src='MenuBar.png'/>
 * </p>
 * <p/>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-MenuBar { the menu bar itself }</li>
 * <li>.gwt-MenuBar .gwt-MenuItem { menu items }</li>
 * <li>.gwt-MenuBar .gwt-MenuItem-selected { selected menu items }</li>
 * </ul>
 * <p/>
 * <p/>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.MenuBarExample}
 * </p>
 */
public class MenuBar extends Widget implements PopupListener {

  private Element body;
  private Vector items = new Vector();
  private MenuBar parentMenu;
  private DecoratedPopup popup;
  private MenuItem selectedItem;
  private MenuBar shownChildMenu;
  private boolean vertical, autoOpen;

  private MenuListenerCollection menuListeners;

  /**
   * Creates an empty horizontal menu bar.
   */
  public MenuBar() {
    this(false);
  }

  /**
   * Creates an empty menu bar.
   *
   * @param vertical <code>true</code> to orient the menu bar vertically
   */
  public MenuBar(boolean vertical) {
    super();

    Element table = DOM.createTable();
    body = DOM.createTBody();
    DOM.appendChild(table, body);

    if (!vertical) {
      Element tr = DOM.createTR();
      DOM.appendChild(body, tr);
    }

    this.vertical = vertical;

    Element outer = DOM.createDiv();
    DOM.appendChild(outer, table);
    setElement(outer);
    setStyleName("gwt-MenuBar");
  }

  /**
   * Adds a menu item to the bar.
   *
   * @param item the item to be added
   */
  public void addItem(MenuItem item) {
    Element tr;
    if (vertical) {
      tr = DOM.createTR();
      DOM.appendChild(body, tr);
    }
    else {
      tr = DOM.getChild(body, 0);
    }

    DOM.appendChild(tr, item.getElement());

    item.setParentMenu(this);
    item.setSelectionStyle(false);
    items.add(item);
  }

  /**
   * Adds a menu item to the bar, that will fire the given command when it is
   * selected.
   *
   * @param text   the item's text
   * @param asHTML <code>true</code> to treat the specified text as html
   * @param cmd    the command to be fired
   * @return the {@link com.google.gwt.user.client.ui.MenuItem} object created
   */
  public MenuItem addItem(String text, boolean asHTML, Command cmd) {
    MenuItem item = new MenuItem(text, asHTML, cmd);
    addItem(item);
    return item;
  }

  /**
   * Adds a menu item to the bar, that will open the specified menu when it is
   * selected.
   *
   * @param text   the item's text
   * @param asHTML <code>true</code> to treat the specified text as html
   * @param popup  the menu to be cascaded from it
   * @return the {@link com.google.gwt.user.client.ui.MenuItem} object created
   */
  public MenuItem addItem(String text, boolean asHTML, MenuBar popup) {
    MenuItem item = new MenuItem(text, asHTML, popup);
    addItem(item);
    return item;
  }

  /**
   * Adds a menu item to the bar, that will fire the given command when it is
   * selected.
   *
   * @param text the item's text
   * @param cmd  the command to be fired
   * @return the {@link com.google.gwt.user.client.ui.MenuItem} object created
   */
  public MenuItem addItem(String text, Command cmd) {
    MenuItem item = new MenuItem(text, cmd);
    addItem(item);
    return item;
  }

  /**
   * Adds a menu item to the bar, that will open the specified menu when it is
   * selected.
   *
   * @param text  the item's text
   * @param popup the menu to be cascaded from it
   * @return the {@link com.google.gwt.user.client.ui.MenuItem} object created
   */
  public MenuItem addItem(String text, MenuBar popup) {
    MenuItem item = new MenuItem(text, popup);
    addItem(item);
    return item;
  }

  /**
   * Removes all menu items from this menu bar.
   */
  public void clearItems() {
    Element container = getItemContainerElement();
    while (DOM.getChildCount(container) > 0) {
      DOM.removeChild(container, DOM.getChild(container, 0));
    }
    items.clear();
  }

  /**
   * Gets whether this menu bar's child menus will open when the mouse is moved
   * over it.
   *
   * @return <code>true</code> if child menus will auto-open
   */
  public boolean getAutoOpen() {
    return autoOpen;
  }

  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    MenuItem item = findItem(DOM.eventGetTarget(event));
    switch (DOM.eventGetType(event)) {
      case Event.ONCLICK: {
        // Fire an item's command when the user clicks on it.
        if (item != null) {
          doItemAction(item, true);
        }
        break;
      }

      case Event.ONMOUSEOVER: {
        if (item != null) {
          itemOver(item);
        }
        break;
      }

      case Event.ONMOUSEOUT: {
        if (item != null) {
          itemOver(null);
        }
        break;
      }
    }
  }

  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    // If the menu popup was auto-closed, close all of its parents as well.
    if (autoClosed) {
      closeAllParents();
    }

    // When the menu popup closes, remember that no item is
    // currently showing a popup menu.
    onHide();
    shownChildMenu = null;
    popup = null;
  }

  /**
   * Removes the specified menu item from the bar.
   *
   * @param item the item to be removed
   */
  public void removeItem(MenuItem item) {
    int idx = items.indexOf(item);
    if (idx == -1) {
      return;
    }

    Element container = getItemContainerElement();
    DOM.removeChild(container, DOM.getChild(container, idx));
    items.remove(idx);
  }

  /**
   * Sets whether this menu bar's child menus will open when the mouse is moved
   * over it.
   *
   * @param autoOpen <code>true</code> to cause child menus to auto-open
   */
  public void setAutoOpen(boolean autoOpen) {
    this.autoOpen = autoOpen;
  }

  protected void onDetach() {
    // When the menu is detached, make sure to close all of its children.
    if (popup != null) {
      popup.hide();
    }

    super.onDetach();
  }

  /*
   * Closes all parent menu popups.
   */
  void closeAllParents() {
    MenuBar curMenu = this;
    while (curMenu != null) {
      curMenu.close();

      if ((curMenu.parentMenu == null) && (curMenu.selectedItem != null)) {
        curMenu.selectedItem.setSelectionStyle(false);
        curMenu.selectedItem = null;
        if (curMenu.menuListeners != null)
          curMenu.menuListeners.fireMenuClosed();
      }

      curMenu = curMenu.parentMenu;
    }
  }

  /*
   * Performs the action associated with the given menu item. If the item has a
   * popup associated with it, the popup will be shown. If it has a command
   * associated with it, and 'fireCommand' is true, then the command will be
   * fired. Popups associated with other items will be hidden.
   *
   * @param item the item whose popup is to be shown. @param fireCommand <code>true</code>
   * if the item's command should be fired, <code>false</code> otherwise.
   */
  void doItemAction(final MenuItem item, boolean fireCommand) {
    // If the given item is already showing its menu, we're done.
    if ((shownChildMenu != null) && (item.getSubMenu() == shownChildMenu)) {
      return;
    }

    // If another item is showing its menu, then hide it.
    if (shownChildMenu != null) {
      shownChildMenu.onHide();
      shownChildMenu = null;
      popup.hide();
    }

    // If the item has no popup, optionally fire its command.
    if (item.getSubMenu() == null) {
      if (fireCommand) {
        // Close this menu and all of its parents.
        closeAllParents();

        // Fire the item's command.
        Command cmd = item.getCommand();
        if (cmd != null) {
          DeferredCommand.add(cmd);
        }
      }
      return;
    }

    // Ensure that the item is selected.
    selectItem(item);

    // Create a new popup for this item, and position it next to
    // the item (below if this is a horizontal menu bar, to the
    // right if it's a vertical bar).
    popup = new DecoratedPopup(true) {
      public boolean onEventPreview(Event event) {
        // Hook the popup panel's event preview. We use this to keep it from
        // auto-hiding when the parent menu is clicked.
        switch (DOM.eventGetType(event)) {
          case Event.ONCLICK:
            // If the event target is part of the parent menu, suppress the
            // event altogether.
            Element target = DOM.eventGetTarget(event);
            Element parentMenuElement = item.getParentMenu().getElement();
            if (DOM.isOrHasChild(parentMenuElement, target)) {
              return false;
            }
            break;
        }

        return super.onEventPreview(event);
      }
    };
    popup.add(item.getSubMenu());
    item.getSubMenu().onShow();
    popup.addPopupListener(this);

    shownChildMenu = item.getSubMenu();
    item.getSubMenu().parentMenu = this;

    final MenuBar willBeShown = shownChildMenu;
    new Timer() {
      public void run() {
        if (willBeShown != shownChildMenu)
          return;
        
        Element elem = popup.getElement();
        DOM.setStyleAttribute(elem, "left", "-1000px");
        DOM.setStyleAttribute(elem, "top", "-1000px");
        // Show the popup, ensuring that the menubar's event preview remains on top
        // of the popup's.
        popup.show();

        int width = popup.getOffsetWidth() + 20;
        int height = popup.getOffsetHeight() + 20;
        int x, y;
        if (vertical) {
          x = item.getAbsoluteLeft() + item.getOffsetWidth();
          y = item.getAbsoluteTop();
        }
        else {
          x = item.getAbsoluteLeft();
          y = item.getAbsoluteTop() + item.getOffsetHeight();
        }
        if (x + width > Window.getClientWidth())
          x = item.getAbsoluteLeft() - width;
        if (y + height > Window.getClientHeight())
          y = Window.getClientHeight() - height;

        if (Utils.isOpera())
          popup.setSize("" + width, "" + height);
        popup.setPopupPosition(x, y);
      }
    }.schedule(300);
  }

  void itemOver(MenuItem item) {
    if (item == null) {
      // Don't clear selection if the currently selected item's menu is showing.
      if ((selectedItem != null)
          && (shownChildMenu == selectedItem.getSubMenu())) {
        return;
      }
    }

    // Style the item selected when the mouse enters.
    selectItem(item);

    // If child menus are being shown, or this menu is itself
    // a child menu, automatically show an item's child menu
    // when the mouse enters.
    if (item != null) {
      if ((shownChildMenu != null) || (parentMenu != null) || autoOpen) {
        doItemAction(item, false);
      }
    }
  }

  /**
   * Closes this menu (if it is a popup).
   */
  private void close() {
    if (parentMenu != null) {
      parentMenu.popup.hide();
    }
  }

  private MenuItem findItem(Element hItem) {
    for (int i = 0; i < items.size(); ++i) {
      MenuItem item = (MenuItem) items.get(i);
      if (DOM.isOrHasChild(item.getElement(), hItem)) {
        return item;
      }
    }

    return null;
  }

  private Element getItemContainerElement() {
    if (vertical) {
      return body;
    }
    else {
      return DOM.getChild(body, 0);
    }
  }

  /*
   * This method is called when a menu bar is hidden, so that it can hide any
   * child popups that are currently being shown.
   */
  private void onHide() {
    if (shownChildMenu != null) {
      shownChildMenu.onHide();
      popup.hide();
    }
  }

  /*
   * This method is called when a menu bar is shown.
   */
  private void onShow() {
    // Select the first item when a menu is shown.
    if (items.size() > 0) {
      selectItem((MenuItem) items.get(0));
    }
  }

  private void selectItem(MenuItem item) {
    if (item == selectedItem) {
      return;
    }

    if (selectedItem != null) {
      selectedItem.setSelectionStyle(false);
    }

    if (item != null) {
      item.setSelectionStyle(true);
    }

    selectedItem = item;
  }

  public void addMenuListener(MenuListener listener) {
    if (menuListeners == null)
      menuListeners = new MenuListenerCollection();
    menuListeners.add(listener);
  }

  public void removeMenuListener(MenuListener listener) {
    if (menuListeners != null)
      menuListeners.remove(listener);
  }

}
