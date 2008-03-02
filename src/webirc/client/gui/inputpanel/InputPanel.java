package webirc.client.gui.inputpanel;

import com.google.gwt.user.client.ui.*;
import webirc.client.WebIRC;
import webirc.client.gui.ImageButton;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 14.07.2006 23:38:46
 */
public class InputPanel extends Composite {

  private static final String STYLE_TEXTAREA = "gwt-inputPanel-textArea";

  private VerticalPanel mainPanel = new VerticalPanel();
  private HorizontalPanel settingsPanel = new HorizontalPanel();
  private HorizontalPanel panel = new HorizontalPanel();
  private TextArea textArea = new TextArea();
  private Button sendButton = new Button(WebIRC.mainMessages.send());

  ImageButton smilesButton;

  Collection inputListeners;

  int cursorPos = 0;

  public InputPanel() {
    initWidget(mainPanel);

    mainPanel.setSize("100%", "100%");

    panel.setSize("100%", "100%");
    sendButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        fireOnSendButtonPressed(textArea.getText());
        textArea.setText("");
        cursorPos = 0;
      }
    });
    textArea.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        cursorPos = textArea.getCursorPos();
      }
    });
    textArea.addKeyboardListener(new KeyboardListenerAdapter() {
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if (keyCode == KEY_ENTER || ((keyCode == 's' || keyCode == 'S') && modifiers == MODIFIER_ALT)) {
          fireOnSendButtonPressed(textArea.getText());
          textArea.setText("");
          textArea.cancelKey();
          cursorPos = 0;
        }
      }
      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        if (keyCode != KEY_ENTER && ((keyCode != 's' && keyCode != 'S') || modifiers != MODIFIER_ALT))
          cursorPos = textArea.getCursorPos();
      }
    });
    textArea.addFocusListener(new FocusListenerAdapter() {
      public void onFocus(Widget sender) {
        cursorPos = textArea.getCursorPos();
      }
    });

    panel.add(textArea);
    panel.add(sendButton);
    panel.setCellVerticalAlignment(sendButton, HorizontalPanel.ALIGN_MIDDLE);
    textArea.setSize("100%", "100%");
    panel.setCellWidth(textArea, "100%");
    panel.setCellHeight(textArea, "100%");

    smilesButton = new ImageButton();
    smilesButton.defineStyle("SmileButton");
    smilesButton.addClickListener(new SmileAction());
    settingsPanel.add(smilesButton);
    HTML right = new HTML();
    settingsPanel.add(right);
    settingsPanel.setCellWidth(right, "100%");

    mainPanel.add(settingsPanel);
    mainPanel.add(panel);
    mainPanel.setCellHeight(panel, "100%");

    textArea.setStyleName(STYLE_TEXTAREA);
  }

  public void injectSettingsPanel(Panel container) {
    if (container == null)
      return;

    mainPanel.remove(settingsPanel);
    container.add(settingsPanel);
  }

  public void addInputListener(InputListener listener) {
    if (inputListeners == null)
      inputListeners = new Vector();
    inputListeners.add(listener);
  }

  public void removeInputListener(InputListener listener) {
    if (inputListeners != null)
      inputListeners.remove(listener);
  }

  public void fireOnSendButtonPressed(String textMessage) {
    for (Iterator it = inputListeners.iterator(); it.hasNext();) {
      InputListener listener = (InputListener) it.next();
      listener.onSendButtonPressed(textMessage);
    }
  }

  public void requestFocus() {
    textArea.setFocus(true);
  }

  private class SmileAction implements ClickListener {

    public void onClick(Widget sender) {
      SmilePanel smilePanel = SmilePanel.getInstance();
      smilePanel.setTextArea(textArea);
      smilePanel.setCursorPos(cursorPos);
      smilePanel.setPopupPosition(0, 0);
      smilePanel.show();
      smilePanel.setPopupPosition(smilesButton.getAbsoluteLeft(),
              smilesButton.getAbsoluteTop() - smilePanel.getOffsetHeight());
      smilePanel.addPopupListener(new PopupListener() {
        public void onPopupClosed(PopupPanel sender, boolean autoClosed) {

        }
      });

    }

  }
}
