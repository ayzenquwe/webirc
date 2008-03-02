package webirc.client.gui;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import webirc.client.MainSystem;
import webirc.client.gui.decorators.NoSuchDecoratorException;
import webirc.client.gui.decorators.PopupDecorator;

/**
 * @author Ayzen
 * @version 1.0 03.01.2007 18:12:25
 */
public class DecoratedPopup extends PopupPanel {

  private PopupDecorator decorator;
  private boolean inited = false;
  private Widget child;

  public DecoratedPopup() {
    initDecorator();
  }

  public DecoratedPopup(boolean autoHide) {
    super(autoHide);
    initDecorator();
  }

  public void initDecorator() {
    try {
      decorator = new PopupDecorator();
      super.add(decorator);
    }
    catch (NoSuchDecoratorException e) {
      e.printStackTrace();
      MainSystem.showError("Couldn't intialize popup:\n" + e.getMessage());
    }
  }

  public void add(Widget w) {
    if (child != null)
      return;

    child = w;
  }

  public void show() {
    super.show();
    makeTransparent(getElement());

    if (!inited) try {
      decorator.initDecorator();
      decorator.getContentPanel().add(child);
      inited = true;
    }
    catch (Exception e) {
      e.printStackTrace();
      MainSystem.showError("Couldn't show popup:\n" + e.getMessage());
    }
  }

  public int getOffsetHeight() {
    return DOM.getIntAttribute(decorator.getElement(), "offsetHeight");
  }

  public int getOffsetWidth() {
    return DOM.getIntAttribute(decorator.getElement(), "offsetWidth");
  }

  private static native void makeTransparent(Element elem) /*-{
    if (elem.__frame != null)
      elem.__frame.style.filter = 'alpha(opacity=0)';
  }-*/;

}
