package webirc.client.gui.tabbedpanel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;

/**
 * @author Ayzen
 * @version 1.0 09.07.2006 12:23:00
 */
public class TabbedPanel extends Composite implements TabbedListener,
                                                      SourcesTabbedEvents {

  protected Panel mainPanel;
  protected DeckedPanel deck = new DeckedPanel();
  protected TabbedBar tabBar = new TabbedBar();
  protected TabbedListenerCollection tabListeners;

  /**
   * Creates an empty tab panel.
   */
  public TabbedPanel() {
    VerticalPanel panel = new VerticalPanel();
    initWidget(panel);

    panel.add(tabBar);
    panel.setCellVerticalAlignment(tabBar, VerticalPanel.ALIGN_BOTTOM);
    panel.add(deck);
    panel.setCellHeight(deck, "100%");

    tabBar.setWidth("100%");

    tabBar.addTabListener(this);

    setStyleName("gwt-TabPanel");
    deck.setStyleName("gwt-TabPanelBottom");

    mainPanel = panel;
  }

  /**
   * Adds a widget to the tab panel.
   *
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   */
  public void add(Widget w, String tabText) {
    insert(w, tabText, getWidgetCount());
  }

  public void addTabListener(TabbedListener listener) {
    if (tabListeners == null)
      tabListeners = new TabbedListenerCollection();
    tabListeners.add(listener);
  }

  public Widget getSelectedTab() {
    int selectedTabIndex = tabBar.getSelectedTabIndex();
    if (selectedTabIndex >= 0)
      return deck.getItem(selectedTabIndex);
    else return null;
  }

  /**
   * Gets the deck panel within this tab panel.
   *
   * @return the deck panel
   */
  public DeckedPanel getDeckPanel() {
    return deck;
  }

  /**
   * Gets the tab bar within this tab panel
   *
   * @return the tab bar
   */
  public TabbedBar getTabBar() {
    return tabBar;
  }

  public Widget getWidget(int index) {
    return deck.getItem(index);
  }

  /**
   * Gets the number of widgets in this tab panel.
   *
   * @return the widget count
   */
  public int getWidgetCount() {
    return deck.getItemCount();
  }

  public int getWidgetIndex(Widget child) {
    return deck.getItemIndex(child);
  }

  /**
   * Inserts a widget into the tab panel.
   *
   * @param widget the widget to be inserted
   * @param tabText the text to be shown on its tab
   * @param beforeIndex the index before which it will be inserted
   */
  public void insert(Widget widget, String tabText, int beforeIndex) {
    tabBar.insertTab(tabText, beforeIndex);
    deck.insert(widget, beforeIndex);
  }

  /**
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator iterator() {
    return deck.iterator();
  }

  public boolean onBeforeTabSelected(SourcesTabbedEvents sender, int tabIndex) {
    if (tabListeners != null)
      return tabListeners.fireBeforeTabSelected(this, tabIndex);
    return true;
  }

  public void onTabSelected(SourcesTabbedEvents sender, int tabIndex) {
    deck.showWidget(tabIndex);
    if (tabListeners != null)
      tabListeners.fireTabSelected(this, tabIndex);
  }

  public boolean onBeforeTabClosed(SourcesTabbedEvents sender, int tabIndex) {
    // Doesn't allow to close the last tab
    if (deck.getItemCount() == 1)
      return false;
    if (tabListeners != null)
      return tabListeners.fireBeforeTabClosed(sender, tabIndex);
    return true;
  }

  public void onTabClosed(SourcesTabbedEvents sender, int tabIndex) {
    deck.remove(deck.getItem(tabIndex));
    if (tabListeners != null)
      tabListeners.fireTabClosed(sender, tabIndex);
  }

  /**
   * Removes the given widget, and its associated tab.
   *
   * @param widget the widget to be removed
   */
  public void remove(Widget widget) {
    int count = deck.getItemCount();
    for (int i = 0; i < count; ++i) {
      if (deck.getItem(i) == widget) {
        tabBar.removeTab(i);
        deck.remove(widget);
        break;
      }
    }
  }

  public void removeTabListener(TabbedListener listener) {
    if (tabListeners != null)
      tabListeners.remove(listener);
  }

  /**
   * Programmatically selects the specified tab.
   *
   * @param index the index of the tab to be selected
   */
  public void selectTab(int index) {
    tabBar.selectTab(index);
  }
}
