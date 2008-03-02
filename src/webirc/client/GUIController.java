package webirc.client;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import webirc.client.commands.MessageCommand;
import webirc.client.commands.PlainCommand;
import webirc.client.gui.ActionPanel;
import webirc.client.gui.contactpanel.ContactPanel;
import webirc.client.gui.dialogs.ConfirmDialog;
import webirc.client.gui.inputpanel.InputListener;
import webirc.client.gui.inputpanel.InputPanel;
import webirc.client.gui.messagepanel.MessagePanel;
import webirc.client.gui.settingsPanel.SettingsPanel;
import webirc.client.gui.tabbedpanel.SourcesTabbedEvents;
import webirc.client.gui.tabbedpanel.TabbedListener;

/**
 * @author Ayzen
 * @version 1.0 06.01.2007 21:13:10
 */
public class GUIController implements InputListener {

  public static final String ICON_WEBIRC = "WebIRC.ico";
  public static final String ICON_MESSAGE = "Message.ico";
  public static final String ICON_ID = "icon";

  private static final String PANEL_ROOT = "RootPanel";
  private static final String PANEL_LOGIN = "LoginPanel";
  private static final String PANEL_INPUT = "InputPanel";
  private static final String PANEL_INPUT_SETTINGS = "InputSettingsPanel";
  private static final String PANEL_MESSAGE = "MessagePanel";
  private static final String PANEL_TABS = "TabsPanel";
  private static final String PANEL_CONTACT = "ContactPanel";
  private static final String PANEL_ACTION = "ActionPanel";
  private static final String PANEL_SETTINGS = "settingsPanel";

  private static GUIController instance = null;

  private MessagePanel messagePanel = new MessagePanel();
  private ContactPanel contactPanel = new ContactPanel();
  private InputPanel inputPanel = new InputPanel();
  private SettingsPanel settingsPanel = new SettingsPanel();

  private boolean guiCreated = false;

  private static Element icon;

  private static boolean windowFocused = true;

  private GUIController() {}

  public static GUIController getInstance() {
    if (instance == null)
      instance = new GUIController();
    return instance;
  }

  /**
   * GUI initialization.
   */
  public void initGUI() {
    // Checks if GUI is already created
    if (guiCreated)
      return;

    icon = DOM.getElementById(ICON_ID);

    // Trying to find login screen
    Panel loginPanel = RootPanel.get(PANEL_LOGIN);
    // If theme has login screen we have to switch it to root screen
    if (loginPanel != null) {
      Panel rootPanel = RootPanel.get(PANEL_ROOT);
      if (rootPanel != null) {
        loginPanel.setVisible(false);
        rootPanel.setVisible(true);
      } else
        MainSystem.showError("Couldn't found '" + PANEL_ROOT + "'");
    }

    addPanel(inputPanel, PANEL_INPUT);
    addPanel(messagePanel, PANEL_MESSAGE);
    addPanel(contactPanel, PANEL_CONTACT);
    addPanel(new ActionPanel(), PANEL_ACTION);
    addPanel(settingsPanel, PANEL_SETTINGS);

    // Additional panels
    // Input settings panel (smileys, bold, italic, etc)
    Panel slot = RootPanel.get(PANEL_INPUT_SETTINGS);
    if (slot != null)
      inputPanel.injectSettingsPanel(slot);
    // Tabs panel
    slot = RootPanel.get(PANEL_TABS);
    if (slot != null)
      messagePanel.injectTabs(slot);

    contactPanel.fixup();
    messagePanel.addTabListener(new TabbedEventsHandler());
    inputPanel.addInputListener(this);

    Window.addWindowFocusListener(new WindowFocusListener() {
      public void onWindowFocused() {
        GUIController.windowFocused = true;
        MainSystem.log("Window get focus.");
        // Simple notification
        MainSystem.getInstance().onWindowFocus();
      }

      public void onWindowBlurred() {
        GUIController.windowFocused = false;
        MainSystem.log("Window lost focus.");
      }
    });

    guiCreated = true;
  }

  /**
   * Adds a panel to the page.
   *
   * @param panel a panel
   * @param panelName a name of a panel
   */
  private void addPanel(Widget panel, String panelName) {
    Panel slot = RootPanel.get(panelName);
    if (slot != null) {
      slot.add(panel);
    } else
      MainSystem.showError("Couldn't found '" + panelName + "'");
  }

  /**
   * Setting an icon for browser window.
   *
   * @param iconPath path to icon file
   */
  public static void setIcon(String iconPath) {
    Element head = DOM.getParent(icon);
    DOM.removeChild(head, icon);
    DOM.setAttribute(icon, "href", iconPath);
    DOM.appendChild(head, icon);
  }

  /**
   * Check if browser window is focused.
   *
   * @return true, if window is focused
   */
  public static boolean isWindowFocused() {
    return windowFocused;
  }

  /**
   * Clear all data in the GUI.
   */
  public void cleanup() {
//    messagePanel.removeAll();
    contactPanel.removeAllChannels();
  }

  /**
   * Handles send button press
   *
   * @param messageText the text from input panel
   */
  public void onSendButtonPressed(String messageText) {
    if (messageText == null || messageText.length() == 0)
      return;

    // If WebIRC is connected to IRC server
    if (MainSystem.isConnected()) {
      Object receiver = messagePanel.getSelectedTabEntity();
      boolean messageForServer;
      messageForServer = messageText.charAt(0) == '/';

      if (receiver != null && !receiver.equals(MessagePanel.SYSTEM_TAB) && !messageForServer) {
        MessageCommand message = new MessageCommand(receiver, messageText);
        message.setSender(MainSystem.getUser());
        messagePanel.addMessage(message.getReceiver(), message);
        MainSystem.sendCommand(message);
      }
      else {
        messagePanel.addMessage(MessagePanel.SYSTEM_TAB, new PlainCommand(messageText));
        MainSystem.sendMessage(messageText);
      }
    }
    // If WebIRC is not connected rto IRC server
    else {
      // Displaying a confirm dialog with proposition to connect to some IRC server
      new ConfirmDialog(WebIRC.mainMessages.reloginConfirmation(), new Command() {
        public void execute() {
          MainSystem.reconnect();
        }
      }).show();
    }
  }

  //********************************************** Getters

  public ContactPanel getContactPanel() {
    return contactPanel;
  }

  public InputPanel getInputPanel() {
    return inputPanel;
  }

  public MessagePanel getMessagePanel() {
    return messagePanel;
  }

  public SettingsPanel getSettingsPanel() {
    return settingsPanel;
  }

  public boolean isGuiCreated() {
    return guiCreated;
  }

  //********************************************** Inner classes

  private class TabbedEventsHandler implements TabbedListener {

    public boolean onBeforeTabSelected(SourcesTabbedEvents sender, int tabIndex) {
      return true;
    }

    public void onTabSelected(SourcesTabbedEvents sender, int tabIndex) {
      Object entity = messagePanel.getSelectedTabEntity();
      if (entity instanceof Channel)
        contactPanel.selectChannel((Channel) entity);
      inputPanel.requestFocus();
    }

    public boolean onBeforeTabClosed(SourcesTabbedEvents sender, int tabIndex) {
      return true;
    }

    public void onTabClosed(SourcesTabbedEvents sender, int index) {
    }
  }

}
