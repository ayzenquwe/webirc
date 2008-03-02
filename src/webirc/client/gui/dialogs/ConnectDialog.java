package webirc.client.gui.dialogs;

import com.google.gwt.user.client.ui.VerticalPanel;
import webirc.client.WebIRC;
import webirc.client.gui.dialogs.panels.NicknamePanel;

/**
 * @author Ayzen
 * @version 1.0 31.07.2006 22:02:59
 */
public class ConnectDialog extends AbstractDialog {

  private VerticalPanel panel = new VerticalPanel();
  private NicknamePanel nicknamePanel = new NicknamePanel();

  private boolean validated = false;

  public ConnectDialog() {
    super(WebIRC.dialogMessages.welcomeToWebIRC(WebIRC.VERSION));
    firstButton.setText(WebIRC.dialogMessages.connect());

    buttonsPanel.clear();
    buttonsPanel.add(firstButton);

    panel.setWidth("100%");
    panel.add(nicknamePanel);
    add(panel);
  }

  public void show() {
    super.show();
    nicknamePanel.getTbNick().setFocus(true);
  }

  public boolean isValidated() {
    return validated;
  }

  public void fireOnFirstButtonPressed() {
    if (nicknamePanel.validate()) {
      validated = true;
      hide();
      super.fireOnFirstButtonPressed();
    }
  }

  public String getNick() {
    return nicknamePanel.getNick();
  }
}
