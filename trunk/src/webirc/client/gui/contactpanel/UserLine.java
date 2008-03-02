package webirc.client.gui.contactpanel;

import webirc.client.GUIController;
import webirc.client.User;
import webirc.client.MainSystem;
import webirc.client.gui.messagepanel.MessagePanel;
import webirc.client.gui.menu.MenuController;
import webirc.client.gui.menu.MenuNotFoundException;

/**
 * @author Ayzen
 * @version 1.0 21.08.2006 21:53:25
 */
public class UserLine extends AbstractLine {

  private User user;

  public UserLine(User user) {
    super();
    setUser(user);
  }

  public void lineClicked() {
    MessagePanel messagePanel = GUIController.getInstance().getMessagePanel();
    if (!messagePanel.isTabExist(user))
      messagePanel.addTab(user);
    messagePanel.selectTab(user);
  }

  public void optionsClicked() {
    try {
      int x = optionsBtn.getAbsoluteLeft();
      int y = optionsBtn.getAbsoluteTop();
      MenuController.getInstance().setParameter("user", user);
      MenuController.getInstance().setParameter("channel", MainSystem.getActiveChannel());
      MenuController.getInstance().getContextMenu("user").show(x, y);
    }
    catch (MenuNotFoundException e) {
      MainSystem.showError(e.getMessage());
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
    refresh();
  }

  public void refresh() {
    content.setText(user.getNickname());
    icon.setUserIconType(user.getType());
  }

}
