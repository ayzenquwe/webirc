package webirc.client.gui.dialogs;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import webirc.client.WebIRC;

/**
 * @author Ayzen
 * @version 1.0 29.07.2006 22:44:24
 */
public class MessageBox extends AbstractDialog implements DialogListener {

  public static final int TYPE_INFORMATION = 0;
  public static final int TYPE_ERROR = 1;

  public static final String STYLE_IMAGE = "gwt-MessageBox-Image";

  private static final String IMAGE_INFORMATION = "images/information.gif";
  private static final String IMAGE_ERROR = "images/error.gif";

  private Image image = new Image();
  private HTML messageContainer = new HTML();

  private MessageBox() {
    super();
    image.setStyleName(STYLE_IMAGE);
    addDialogListener(this);

    buttonsPanel.clear();
    buttonsPanel.add(firstButton);
//    panel.add(image, DockPanel.WEST);
    add(messageContainer);
  }

  public MessageBox(String message) {
    this();
    caption.setText(WebIRC.dialogMessages.information());
    messageContainer.setHTML(prepareText(message));
    image.setUrl(IMAGE_INFORMATION);
  }

  public MessageBox(String caption, String message) {
    this();
    this.caption.setText(caption);
    messageContainer.setHTML(prepareText(message));
    image.setUrl(IMAGE_INFORMATION);
  }

  public MessageBox(String caption, String message, boolean withoutButtons) {
    this(caption, message);
    if (withoutButtons)
      buttonsPanel.clear();
  }

  public MessageBox(String caption, String message, int type) {
    this();
    this.caption.setText(caption);
    if (message != null)
      messageContainer.setHTML(prepareText(message));
    switch (type) {
      case TYPE_INFORMATION:
        image.setUrl(IMAGE_INFORMATION);
        break;
      case TYPE_ERROR:
        image.setUrl(IMAGE_ERROR);
        break;
      default:
        image.setUrl(IMAGE_INFORMATION);
    }
  }

  public void onFirstButtonPressed() {
    hide();
  }

  public void onSecondButtonPressed() {
  }
}
