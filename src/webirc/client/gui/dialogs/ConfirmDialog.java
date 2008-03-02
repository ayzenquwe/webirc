package webirc.client.gui.dialogs;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import webirc.client.WebIRC;

/**
 * @author Ayzen
 * @version 1.0 06.01.2007 20:47:39
 */
public class ConfirmDialog extends AbstractDialog {

  private HTML messageContainer = new HTML();

  public ConfirmDialog(String text) {
    super(WebIRC.dialogMessages.confirmation());

    firstButton.setText(WebIRC.dialogMessages.yes());
    secondButton.setText(WebIRC.dialogMessages.no());

    messageContainer.setHTML(prepareText(text));
    add(messageContainer);
  }

  public ConfirmDialog(String text, final Command command) {
    this(text);

    addDialogListener(new DialogListener() {
      public void onFirstButtonPressed() {
        if (command != null)
          command.execute();
        hide();
      }

      public void onSecondButtonPressed() {
        hide();
      }
    });
  }

}
