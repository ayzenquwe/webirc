package webirc.client.gui.dialogs;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import webirc.client.WebIRC;

/**
 * @author Ayzen
 * @version 1.0 19.01.2007 21:16:14
 */
public class InputDialog extends AbstractDialog {

  private VerticalPanel contentPanel = new VerticalPanel();
  private HTML messageContainer = new HTML();
  private TextBoxBase tbInput;
  private Command command;

  public InputDialog(String text, boolean isPassword) {
    super(WebIRC.dialogMessages.inputDialog());

    if (isPassword)
      tbInput = new PasswordTextBox();
    else
      tbInput = new TextBox();

    DOM.setStyleAttribute(tbInput.getElement(), "marginTop", "5px");
    contentPanel.add(messageContainer);
    contentPanel.add(tbInput);
    contentPanel.setCellHorizontalAlignment(tbInput, VerticalPanel.ALIGN_CENTER);

    messageContainer.setHTML(prepareText(text));
    add(contentPanel);

    addDialogListener(getDialogListener());

    tbInput.addKeyboardListener(new KeyboardListenerAdapter() {
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if (keyCode == KEY_ENTER)
          proceed();
      }
    });
  }

  public void addCommand(Command command) {
    this.command = command;
  }

  public void show() {
    super.show();
    tbInput.setFocus(true);
  }

  private DialogListener getDialogListener() {
    return new DialogListener() {
      public void onFirstButtonPressed() {
        proceed();
      }

      public void onSecondButtonPressed() {
        hide();
      }
    };
  }

  private void proceed() {
    if (command != null)
      command.execute();
    hide();
  }

  public String getInputText() {
    return tbInput.getText();
  }

}