package webirc.client.gui;

import webirc.client.ExCommand;
import webirc.client.WebIRC;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

/**
 * @author Ayzen
 * @version 1.0 23.02.2007 17:46:44
 */
public class Popups {

  /**
   * Showing simple input popup panel.
   *
   * @param x the X coordinate
   * @param y the Y coordinate
   * @param text description
   * @param resultCommand command to do after user input data
   */
  public static void showInput(final int x, final int y, String text, final ExCommand resultCommand) {
    final DecoratedPopup popup = new DecoratedPopup(true);
    FlowPanel panel = new FlowPanel();

    Label lblText = new Label(text + ":", false);
    final TextBox field = new TextBox();
    field.addKeyboardListener(new KeyboardListenerAdapter() {
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if (keyCode == KeyboardListener.KEY_ENTER)
          popup.hide();
      }
    });

    Button okButton = new Button(WebIRC.dialogMessages.ok());
    okButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        popup.hide();
      }
    });

    panel.setStyleName("gwt-InputPopup");
    okButton.setStyleName("gwt-InputPopup-Button");
    field.setVisibleLength(10);

    panel.add(lblText);
    panel.add(field);
    panel.add(okButton);

    popup.add(panel);
    popup.setPopupPosition(0, 0);
    popup.show();

    if (x + popup.getOffsetWidth() > Window.getClientWidth())
      popup.setPopupPosition(x - popup.getOffsetWidth(), y);
    else
      popup.setPopupPosition(x, y);

    field.setFocus(true);

    popup.addPopupListener(new PopupListener() {
      public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        if (!autoClosed)
          resultCommand.execute(field.getText());
      }
    });
  }

  /**
   * Showing error message in a simple popup.
   *
   * @param message message to show
   * @param left the X coordinate
   * @param top the Y coordinate
   */
  public static void showErrorMessage(String message, int left, int top) {
    PopupPanel tip = new PopupPanel(true);
    tip.add(new Label(message));
    tip.setStyleName("ErrorMessage");
    tip.setPopupPosition(left, top);
    tip.show();
  }

}
