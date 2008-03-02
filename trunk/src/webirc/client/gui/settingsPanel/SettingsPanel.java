package webirc.client.gui.settingsPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import webirc.client.MainSystem;
import webirc.client.WebIRC;
import webirc.client.gui.ImageButton;
import webirc.client.gui.dialogs.ConfirmDialog;

/**
 * @author Ayzen
 * @version 1.0 06.01.2007 18:54:12
 */
public class SettingsPanel extends Composite {

  private static final String STYLE_DIVIDER = "gwt-Divider";
  private static final String STYLE_LOGOUT = "LogoutButton";
  private static final String STYLE_LOGIN = "LoginButton";

  private Panel mainPanel;

  private ImageButton logoutBtn;
  private ImageButton loginBtn;

  public SettingsPanel() {
    mainPanel = new HorizontalPanel();
    initWidget(mainPanel);

    logoutBtn = new ImageButton(WebIRC.mainMessages.logout());
    logoutBtn.defineStyle(STYLE_LOGOUT);
    loginBtn = new ImageButton(WebIRC.mainMessages.login());
    loginBtn.defineStyle(STYLE_LOGIN);

    logoutBtn.addClickListener(new LogoutButtonListener());
    loginBtn.addClickListener(new LoginButtonListener());
  }

  public void switchButtons() {
    mainPanel.remove(loginBtn);
    mainPanel.remove(logoutBtn);
    if (MainSystem.isConnected())
      mainPanel.add(logoutBtn);
    else
      mainPanel.add(loginBtn);
  }

  private class LogoutButtonListener implements ClickListener {
    public void onClick(Widget sender) {
      if (!MainSystem.isConnected())
        return;

      Command command = new Command() {
        public void execute() {
          MainSystem.sendQuitMessage();
        }
      };
      final ConfirmDialog dialog = new ConfirmDialog(WebIRC.mainMessages.logoutConfirmation(), command);
      dialog.show();
    }
  }

  private class LoginButtonListener implements ClickListener {
    public void onClick(Widget sender) {
      if (MainSystem.isConnected())
        return;

      Command command = new Command() {
        public void execute() {
          MainSystem.reconnect();
        }
      };
      final ConfirmDialog dialog = new ConfirmDialog(WebIRC.mainMessages.loginConfirmation(), command);
      dialog.show();
    }
  }

}
