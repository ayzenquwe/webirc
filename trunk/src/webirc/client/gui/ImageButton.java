package webirc.client.gui;

import com.google.gwt.user.client.ui.*;

/**
 * The simple widget for button which can hold an image and a text. It changes it's styles according to mouse events.
 *
 * @author Ayzen
 * @version 1.0 15.07.2006 22:47:01
 */
public class ImageButton extends Composite {

  private String STYLE_TEXT = "gwt-ImageButton-Text";
  private String STYLE_NORMAL = "gwt-ImageButton";
  private String STYLE_MOUSE_OVER = "gwt-ImageButton-mouseOver";
  private String STYLE_PUSHED = "gwt-ImageButton-pushed";

  /**
   * Determines whether button is pushed.
   */
  private boolean pushed = false;

  /**
   * The image. Manipulates by styling.
   */
  private Button button = new Button();
  /**
   * Text of a button. It can be empty.
   */
  private LineLabel lblText;
  /**
   * The panel which holds an image and a text of the button.
   */
  private FlowPanel panel = new FlowPanel();

  private FocusPanel focusPanel = new FocusPanel();

  /**
   * Creates the button without text label.
   */
  public ImageButton() {
    initWidget(focusPanel);
    panel.add(button);
    button.setStyleName(STYLE_NORMAL);
    focusPanel.addMouseListener(new MouseEventsHandler());
    focusPanel.add(panel);
  }

  /**
   * Creates the button with caption.
   *
   * @param text caption text
   */
  public ImageButton(String text) {
    this();
    lblText = new LineLabel(text, false);
    lblText.setStyleName(STYLE_TEXT);
    panel.add(lblText);
  }

  /**
   * Sets button's caption.
   *
   * @param text text for caption
   */
  public void setText(String text) {
    if (lblText == null) {
      lblText = new LineLabel(text, false);
      lblText.setStyleName(STYLE_TEXT);
      panel.add(lblText);
    }
    lblText.setText(text);
  }

  /**
   * Gets button's caption.
   *
   * @return caption text
   */
  public String getText() {
    return lblText.getText();
  }

  /**
   * Defines styles for this button.
   *
   * @param styleName The radical of style name
   */
  public void defineStyle(String styleName) {
    STYLE_NORMAL = "gwt-" + styleName;
    STYLE_MOUSE_OVER = "gwt-" + styleName + "-mouseOver";
    STYLE_PUSHED = "gwt-" + styleName + "-pushed";
    STYLE_TEXT = "gwt-" + styleName + "-Text";
    button.setStyleName(STYLE_NORMAL);
    if (lblText != null)
      lblText.addStyleName(STYLE_TEXT);
  }

  public void addClickListener(ClickListener listener) {
    focusPanel.addClickListener(listener);
  }

  public void removeClickListener(ClickListener listener) {
    focusPanel.removeClickListener(listener);
  }

  public void addMouseListener(MouseListener listener) {
    focusPanel.addMouseListener(listener);
  }

  public void removeMouseListener(MouseListener listener) {
    focusPanel.removeMouseListener(listener);
  }

  /**
   * Handles ImageButton's mouse events and changes styles.
   */
  private class MouseEventsHandler extends MouseListenerAdapter {
    public void onMouseDown(Widget sender, int x, int y) {
      button.addStyleName(STYLE_PUSHED);
      pushed = true;
    }

    public void onMouseUp(Widget sender, int x, int y) {
      if (pushed) {
        button.removeStyleName(STYLE_PUSHED);
        pushed = false;
      }
    }

    public void onMouseEnter(Widget sender) {
      button.addStyleName(STYLE_MOUSE_OVER);
    }

    public void onMouseLeave(Widget sender) {
      pushed = false;
      button.removeStyleName(STYLE_MOUSE_OVER);
      button.removeStyleName(STYLE_PUSHED);
    }
  }
}
