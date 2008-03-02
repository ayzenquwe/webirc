package webirc.client.gui.dialogs;

import webirc.client.WebIRC;
import webirc.client.gui.dialogs.panels.NicknamePanel;

/**
 * @author Ayzen
 * @version 1.0 30.07.2006 23:45:56
 */
public class ChangeNickDialog extends AbstractDialog implements DialogListener {

  private NicknamePanel panel = new NicknamePanel();

  public ChangeNickDialog() {
    super();
    addDialogListener(this);
    setText(WebIRC.dialogMessages.enterNickname());
    add(panel);
  }

  public String getNickname() {
    return panel.getNick();
  }

  public void onFirstButtonPressed() {
    if (panel.validate())
      hide();
  }

  public void onSecondButtonPressed() {
    hide();
  }
}
