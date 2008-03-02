package webirc.client.gui.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import webirc.client.WebIRC;
import webirc.client.gui.decorators.DialogDecorator;
import webirc.client.gui.decorators.NoSuchDecoratorException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * A form of popup that has a caption area at the top and can be dragged by the
 * user.
 * <p/>
 * <p/>
 * <img class='gallery' src='DialogBox.png'/>
 * </p>
 * <p/>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-DialogBox { the outside of the dialog }</li>
 * <li>.gwt-DialogBox .Caption { the caption }</li>
 * </ul>
 */
public abstract class AbstractDialog extends PopupPanel implements HasHTML, MouseListener {

  private Widget child;
  protected HTML caption = new HTML();
  private boolean dragging;
  private int dragStartX, dragStartY;

  protected Collection dialogListeners = new Vector();

  /**
   * The left button in the SOUTH edge. Commonly it is a OK button.
   */
  protected Button firstButton = new Button();
  /**
   * The right button in the SOUTH edge. Commonly it is a Cancel button.
   */
  protected Button secondButton = new Button();
  protected HorizontalPanel buttonsPanel = new HorizontalPanel();
  protected DialogDecorator decorator;
  protected boolean inited = false;

  /**
   * Creates an empty dialog box. It should not be shown until its child widget
   * has been added using {@link #add}.
   */
  public AbstractDialog() {
    try {
      decorator = new DialogDecorator();
    }
    catch (NoSuchDecoratorException e) {
      e.printStackTrace();
      Window.alert("Couldn't intialize dialog:\n" + e.getMessage());
    }

    super.add(decorator);

    firstButton.setText(WebIRC.dialogMessages.ok());
    secondButton.setText(WebIRC.dialogMessages.cancel());

    buttonsPanel.add(firstButton);
    buttonsPanel.add(new HTML("&nbsp;&nbsp;&nbsp;"));
    buttonsPanel.add(secondButton);

    caption.addMouseListener(this);
    caption.setWidth("100%");

    firstButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        fireOnFirstButtonPressed();
      }
    });
    secondButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        fireOnSecondButtonPressed();
      }
    });
  }

  public AbstractDialog(String caption) {
    this();
    this.caption.setText(caption);
  }

  public AbstractDialog(String caption, Widget child) {
    this();
    this.caption.setText(caption);
    add(child);
  }

  public void show() {
    if (child != null) {
      super.show();
      makeTransparent(getElement());

      if (!inited) {
        try {
          decorator.initDecorator();
        }
        catch (Exception e) {
          e.printStackTrace();
          Window.alert("Couldn't show dialog:\n" + e.getMessage());
        }

        decorator.getHeaderPanel().add(caption);
        decorator.getContentPanel().add(child);
        decorator.getButtonsPanel().add(buttonsPanel);
        inited = true;
      }

      setPopupPosition((Window.getClientWidth() - getOffsetWidth()) / 2,
                       (Window.getClientHeight() - getOffsetHeight()) / 2);
    }
  }

  public void add(Widget w) {
    if (child != null)
      return;

    child = w;
  }

  public String getHTML() {
    return caption.getHTML();
  }

  public String getText() {
    return caption.getText();
  }

  public void onMouseDown(Widget sender, int x, int y) {
    dragging = true;
    DOM.setCapture(caption.getElement());
    dragStartX = x;
    dragStartY = y;
  }

  public void onMouseEnter(Widget sender) {
  }

  public void onMouseLeave(Widget sender) {
  }

  public void onMouseMove(Widget sender, int x, int y) {
    if (dragging) {
      int absX = x + getAbsoluteLeft();
      int absY = y + getAbsoluteTop();
      setPopupPosition(absX - dragStartX, absY - dragStartY);
    }
  }

  public void onMouseUp(Widget sender, int x, int y) {
    dragging = false;
    DOM.releaseCapture(caption.getElement());
  }

  public boolean remove(Widget w) {
    if (child != w)
      return false;

    decorator.getContentPanel().remove(w);
    child = null;
    return true;
  }

  public void setHTML(String html) {
    caption.setHTML(html);
  }

  public void setText(String text) {
    caption.setText(text);
  }

  public void addDialogListener(DialogListener listener) {
    dialogListeners.add(listener);
  }

  public void removeDialogListener(DialogListener listener) {
    dialogListeners.remove(listener);
  }

  public void fireOnFirstButtonPressed() {
    for (Iterator it = dialogListeners.iterator(); it.hasNext();) {
      DialogListener listener = (DialogListener) it.next();
      listener.onFirstButtonPressed();
    }
  }

  public void fireOnSecondButtonPressed() {
    for (Iterator it = dialogListeners.iterator(); it.hasNext();) {
      DialogListener listener = (DialogListener) it.next();
      listener.onSecondButtonPressed();
    }
  }

  protected String prepareText(String text) {
    return text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>");
  }

  private static native void makeTransparent(Element elem) /*-{
    if (elem.__frame != null)
      elem.__frame.style.filter = 'alpha(opacity=0)';
  }-*/;
}