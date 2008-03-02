package webirc.client.gui.messagepanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Panel;
import webirc.client.Channel;
import webirc.client.User;
import webirc.client.WebIRC;
import webirc.client.utils.Utils;
import webirc.client.commands.IRCCommand;
import webirc.client.gui.tabbedpanel.SourcesTabbedEvents;
import webirc.client.gui.tabbedpanel.TabbedBarItem;
import webirc.client.gui.tabbedpanel.TabbedPanel;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 30.06.2006 12:59:44
 */
public class MessagePanel extends TabbedPanel {

  public static final String SYSTEM_TAB = WebIRC.mainMessages.server();

  /**
   * Contains Users and Channels
   */
  Vector tabs = new Vector();

  HashMap commandQueries = new HashMap();

  public MessagePanel() {
    Window.addWindowResizeListener(new WindowResizeListener() {
      private int wasWidth = 0;
      private int wasHeight = 0;
      public void onWindowResized(int width, int height) {
        if (!isAttached())
          return;
        if (wasWidth != width || wasHeight != height) {
          deck.collapse();
          tabBar.collapse();
          setSize("20px", "20px");
          DeferredCommand.add(new Command() {                                                  
            public void execute() {
              tabBar.adjust();
              Utils.maximize(MessagePanel.this);
              deck.adjustSize();
            }
          });
        }
        wasWidth = width;
        wasHeight = height;
      }
    });
  }

  public void injectTabs(Panel container) {
    if (container == null)
      return;

    mainPanel.remove(tabBar);
    container.add(tabBar);
  }

  protected void onAttach() {
    super.onAttach();
    Utils.maximize(this);
  }

  public HistoryPanel addTab(Object entity) {
    tabs.add(entity);

//    deck.collapse();
    HistoryPanel panel = new HistoryPanel();
    add(panel, entity.toString());
//    deck.adjustSize();

    checkQuery(entity, panel);

    HistoryPanel tab = (HistoryPanel) getSelectedTab();
    if (tab != null)
      tab.scrollToBottom();
    
    return panel;
  }

  public void removeTab(Object entity) {
    remove(getTab(entity));
    tabs.remove(entity);
  }

  public void changeEntityName(Object entity, String name) {
    for (int i = 0; i < tabBar.getTabCount(); i++) {
      Object tab = tabs.get(i);
      if (tabBar.getTabTitle(i).equalsIgnoreCase(((User) entity).getNickname()) && tab instanceof User) {
        ((User) tab).setNickname(name);
        tabBar.setTabTitle(i, name);
        break;
      }
    }
  }

  private void checkQuery(Object entity, HistoryPanel panel) {
    Vector query = (Vector) commandQueries.get(entity.toString());    
    if (query != null) {
      for (int i = 0; i < query.size(); i++)
        panel.addMessage((IRCCommand) query.get(i));
      commandQueries.remove(entity.toString());
    }
  }

  public int getEntityIndex(Object entity) {
    for (int i = 0; i < tabs.size(); i++)
      if (tabs.get(i).equals(entity))
        return i;
    return -1;
  }

  public Object getSelectedTabEntity() {
    int index = getTabBar().getSelectedTabIndex();
    if (index >= 0)
      return tabs.get(index);
    else
      return null;
  }

  public HistoryPanel getTab(Object entity) {
    for (int i = 0; i < tabs.size(); i++)
      if (tabs.get(i).equals(entity))
        return (HistoryPanel) getWidget(i);
    return null;
  }

  public boolean isTabExist(Object entity) {
    return tabs.contains(entity);
  }

  public void selectTab(Object entity) {
    super.selectTab(tabs.indexOf(entity));
  }

  public void removeAll() {
    for (int i = tabs.size() - 1; i >= 0; i--)
      removeTab(tabs.get(i));
  }

  public void addMessage(IRCCommand command) {
    Object entity = getSelectedTabEntity();
    if (entity == null)
      entity = SYSTEM_TAB;
    addMessage(entity, command);
  }

  public void addMessage(Object entity, IRCCommand command) {
    int index = getEntityIndex(entity);
    HistoryPanel historyPanel = null;
    if (index != -1)
      historyPanel = (HistoryPanel) getWidget(index);
    // If the message for user historyPanel or channel historyPanel
    if (entity instanceof Channel) {
      if (historyPanel != null)
        historyPanel.addMessage(command);
      else {
        // Adding message to query. It will be get when user opens a historyPanel
        Vector query = (Vector) commandQueries.get(entity.toString());
        if (query == null) {
          query = new Vector();
          commandQueries.put(entity.toString(), query);
        }
        query.add(command);
      }
    }
    else if (entity instanceof User) {
      if (historyPanel == null)
        historyPanel = addTab(entity);
      historyPanel.addMessage(command);
//      if (!entity.equals(getSelectedTabEntity()))
//        tabBar.getItem(se)
    }
    // If the message for system historyPanel
    else if (entity instanceof String) {
      if (historyPanel == null)
        historyPanel = addTab(SYSTEM_TAB);
      historyPanel.addMessage(command);
    }
    if (historyPanel != null) {
      if (getSelectedTab() == historyPanel)
        historyPanel.scrollToBottom();
      else {
        TabbedBarItem tab = tabBar.getItem(index);
        if (tab != null)
          tab.setChanged(true);
      }
    }
  }

  public void addSystemMessage(IRCCommand command) {
    addMessage(SYSTEM_TAB, command);
  }

  public static boolean isSystemTab(TabbedBarItem tab) {
    return tab != null && SYSTEM_TAB.equalsIgnoreCase(tab.getText());
  }

  public void onTabSelected(SourcesTabbedEvents sender, int tabIndex) {
    super.onTabSelected(sender, tabIndex);
    ((HistoryPanel) getWidget(tabIndex)).scrollToBottom();
    Window.setTitle(tabBar.getSelectedTabTitle());
  }

  public void onTabClosed(SourcesTabbedEvents sender, int tabIndex) {
    super.onTabClosed(sender, tabIndex);
    tabs.remove(tabIndex);
  }
}
