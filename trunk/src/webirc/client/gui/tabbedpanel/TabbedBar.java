package webirc.client.gui.tabbedpanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.Vector;

public class TabbedBar extends Composite implements SourcesTabbedEvents,
                                                    ClickListener {

  public static final String STYLE_NORMAL = "gwt-TabbedBar";
  public static final String STYLE_TABSBLOCK = "gwt-TabbedBar-TabBlock";
  public static final String STYLE_TABSBLOCK_FIRST = "gwt-TabbedBar-first";
  public static final String STYLE_TABSBLOCK_REST = "gwt-TabbedBar-rest";

  /**
   * All tabs
   */
  private Vector tabs = new Vector();
  /**
   * The main panel. This one needs for "overflow: hidden".
   */
  private SimplePanel container = new SimplePanel() {
  };
  /**
   * The panel which holds all blocks of tabs
   */
  private VerticalPanel tabsPanel = new VerticalPanel();
  /**
   * The last added block
   */
  private HorizontalPanel lastBlock = null;
  /**
   * `
   * Index of the selected tab
   */
  private int selectedTab = -1;

  private TabbedListenerCollection tabListeners;

  private boolean sizeSetted = false;

  /**
   * Creates an empty tab bar.
   */
  public TabbedBar() {
    initWidget(container);
    tabsPanel.setWidth("100%");
    container.add(tabsPanel);
    // All overflowed tabs we will moved to new blocks
    DOM.setStyleAttribute(getElement(), "overflow", "hidden");
    sinkEvents(Event.ONCLICK);
    tabsPanel.setStyleName(STYLE_NORMAL);
  }

  /**
   * Adds a new tab with the specified text.
   *
   * @param text the new tab's text
   */
  public void addTab(String text) {
    insertTab(text, tabs.size());
  }

  public void addTabListener(TabbedListener listener) {
    if (tabListeners == null)
      tabListeners = new TabbedListenerCollection();
    tabListeners.add(listener);
  }

  /**
   * Gets the tab that is currently selected.
   *
   * @return the selected tab
   */
  public TabbedBarItem getSelectedTab() {
    return getItem(selectedTab);
  }

  /**
   * Gets the index of tab that is currently selected.
   *
   * @return index of the selected tab
   */
  public int getSelectedTabIndex() {
    return selectedTab;
  }

  public String getSelectedTabTitle() {
    TabbedBarItem item = getSelectedTab();
    if (item != null)
      return item.getText();
    else return null;
  }

  /**
   * Gets the number of tabs present.
   *
   * @return the tab count
   */
  public int getTabCount() {
    return tabs.size();
  }

  /**
   * Gets the specified tab's title.
   *
   * @param index the index of the tab whose title is to be retrieved
   * @return the tab's title
   */
  public String getTabTitle(int index) {
    if (index >= getTabCount())
      return null;
    return getItem(index).getText();
  }

  public void setTabTitle(int index, String title) {
    if (index >= getTabCount())
      return;
    getItem(index).setText(title);
  }

  /**
   * Inserts a new tab at the specified index.
   *
   * @param text        the new tab's text
   * @param beforeIndex the index before which this tab will be inserted
   */
  public void insertTab(String text, int beforeIndex) {
    TabbedBarItem item = new TabbedBarItem(text);
    item.addClickListener(this);
    item.addCloseListener(new CloseHandler());

    HorizontalPanel block;
    TabbedBarItem beforeItem = getItem(beforeIndex);
    if (beforeItem != null) {
      block = (HorizontalPanel) beforeItem.getParent();
      beforeIndex = block.getWidgetIndex(beforeItem);
    }
    // Tab will be inserted at the end of the last tabs block
    else {
      if (tabsPanel.getWidgetCount() > 0)
        block = lastBlock;
      else
        block = addTabsBlock();
      beforeIndex = block.getWidgetCount() - 1;
    }
    // The focus with setVisible is need for correct cell resize in Opera
    block.setVisible(false);
    block.insert(item, beforeIndex);
    block.setVisible(true);
    if (isAttached()) {
      if (!sizeSetted) {
        setWidth("" + getOffsetWidth());
        sizeSetted = true;
      }
      validateBlock(block);
    }
    tabs.add(item);
  }

  /**
   * Validates items position in a block. All items with position
   * bigger than the bar width it moves to the next block. If
   * there is an empty space in the block it tries to move items
   * to this block.
   *
   * @param block the block of items to validate.
   */
  private void validateBlock(HorizontalPanel block) {
    int itemIndex = -1;
    int freeSpace = 0;
    // The x coordinate of right side of this panel
    int panelEnd = getAbsoluteLeft() + getOffsetWidth();
    if (panelEnd <= 0)
      return;
    // Validating each item...
    for (int i = 1; i < block.getWidgetCount() - 1; i++) {
      Widget item = block.getWidget(i);
      // The x coordinate of right side of an item
      int itemEnd = item.getAbsoluteLeft() + item.getOffsetWidth();
      // If an item gets out of this panel
      if (itemEnd > panelEnd) {
        itemIndex = i;
        break;
      }
      // If it is the last item, then we saving the amount of free space
      if (i == block.getWidgetCount() - 2)
        freeSpace = panelEnd - itemEnd;
    }
    // If there are some items we need to move to the next block
    if (itemIndex != -1 && block.getWidgetCount() > 3) {
      HorizontalPanel nextBlock;
      // Index of validating block
      int blockIndex = tabsPanel.getWidgetIndex(block);
      if (blockIndex == -1)
        return;
      // If this block is the last then we should add new block
      if (blockIndex == tabsPanel.getWidgetCount() - 1)
        nextBlock = addTabsBlock();
      else
        nextBlock = lastBlock;
      // Moving items to the next block...
      while (itemIndex != block.getWidgetCount() - 1) {
        Widget widget = block.getWidget(itemIndex);
        block.remove(widget);
        nextBlock.insert(widget, 1);
      }
      validateBlock(nextBlock);
    }
    // Trying to fill the rest of free space with items in the last block
    else if (freeSpace > 0) {
      HorizontalPanel nextBlock = lastBlock;
      if (nextBlock != null && nextBlock != block) {
        while (nextBlock.getWidgetCount() > 2) {
          Widget item = nextBlock.getWidget(1);
          int itemWidth = item.getOffsetWidth();
          if (itemWidth <= freeSpace) {
            nextBlock.remove(item);
            block.insert(item, block.getWidgetCount() - 1);
            freeSpace -= itemWidth;
          }
          else break;
        }
        // Delete block if it's empty
        if (nextBlock.getWidgetCount() <= 2) {
          tabsPanel.remove(nextBlock);
          if (lastBlock == nextBlock)
            lastBlock = block;
        }
      }
    }
  }

  private HorizontalPanel addTabsBlock() {
    HorizontalPanel block = new HorizontalPanel();
    block.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);

    HTML first = new HTML("&nbsp;", true), rest = new HTML("&nbsp;", true);
    first.setStyleName(STYLE_TABSBLOCK_FIRST);
    rest.setStyleName(STYLE_TABSBLOCK_REST);
    rest.setWidth("100%");

    block.add(first);
    block.add(rest);
    block.setCellHeight(first, "100%");
    block.setCellHeight(rest, "100%");
    block.setCellWidth(rest, "100%");

    // If there is the selected tab we need to add new block to the top of tabsPanel
    if (selectedTab != -1)
      tabsPanel.insert(block, 0);
    else
      tabsPanel.add(block);

    lastBlock = block;
    return block;
  }

  public void onClick(Widget sender) {
    for (int i = 0; i < tabs.size(); i++) {
      if (tabs.get(i) == sender) {
        selectTab(i);
        return;
      }
    }
  }

  /**
   * Removes the tab at the specified index.
   *
   * @param index the index of the tab to be removed
   */
  public void removeTab(int index) {
    TabbedBarItem item = getItem(index);
    HorizontalPanel block = (HorizontalPanel) item.getParent();

    // This is needed because there is a bug in Opera :(
    int blockIndex = tabsPanel.getWidgetIndex(block);
    tabsPanel.remove(block);
    block.remove(item);
    tabsPanel.insert(block, blockIndex);

    if (index == selectedTab)
      selectedTab = -1;
    else if (index < selectedTab)
      selectedTab--;

    if (block.getWidgetCount() <= 2)
      tabsPanel.remove(block);
    else
      validateBlock(block);
    tabs.remove(index);
  }

  public void removeTabListener(TabbedListener listener) {
    if (tabListeners != null)
      tabListeners.remove(listener);
  }

  /**
   * Programmatically selects the specified tab.
   *
   * @param index the index of the tab to be selected
   * @return <code>true</code> if successful, <code>false</code> if the
   *         change is denied by the {@link TabListener}.
   */
  public boolean selectTab(int index) {
    if (tabListeners != null) {
      if (!tabListeners.fireBeforeTabSelected(this, index))
        return false;
    }

    TabbedBarItem item = getItem(selectedTab);
    if (item != null)
      item.setSelected(false);
    // Setting the selected tab
    item = getItem(index);
    HorizontalPanel block = (HorizontalPanel) item.getParent();
    // If a block which holds the selected tab not at the bottom of tabsPanel...
    if (tabsPanel.getWidgetIndex(block) != tabsPanel.getWidgetCount() - 1) {
      // Moving a block to the bottom of tabsPanel
      tabsPanel.remove(block);
      tabsPanel.add(block);
    }
    if (item != null)
      item.setSelected(true);

    selectedTab = index;

    if (tabListeners != null)
      tabListeners.fireTabSelected(this, index);
    return true;
  }

  public boolean closeTab(int index) {
    if (tabListeners != null) {
      if (!tabListeners.fireBeforeTabClosed(this, index))
        return false;
    }

    if (index == selectedTab)
      selectedTab = -1;

    removeTab(index);

    if (tabListeners != null)
      tabListeners.fireTabClosed(this, index);
    return true;
  }

  public TabbedBarItem getItem(int index) {
    if (index < tabs.size() && index >= 0)
      return (TabbedBarItem) tabs.get(index);
    else return null;
  }

  public void collapse() {
    tabsPanel.setVisible(false);
    setVisible(false);
    DOM.setStyleAttribute(getElement(), "overflow", "visible");
  }

  public void adjust() {
    setWidth("100%");
    setVisible(true);
    DOM.setStyleAttribute(getElement(), "overflow", "hidden");
    setWidth("" + getOffsetWidth());
    tabsPanel.setVisible(true);
    for (int i = tabsPanel.getWidgetCount() - 1; i >= 0; i--) {
      HorizontalPanel block = (HorizontalPanel) tabsPanel.getWidget(i);
      validateBlock(block);
    }
  }

  /**
   * Class that handles close button presses.
   */
  private class CloseHandler implements CloseListener {

    /**
     * Searches the tab for closing and closes it.
     *
     * @param sender tab whose close button was pressed
     */
    public void onClose(Widget sender) {
      for (int i = 0; i < tabs.size(); i++) {
        if (tabs.get(i) == sender) {
          closeTab(i);
          return;
        }
      }
    }

  }
}

