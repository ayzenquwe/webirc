package webirc.client.gui.tabbedpanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import webirc.client.utils.Utils;
import webirc.client.gui.ContainerPanel;

import java.util.Iterator;
import java.util.Vector;

/**
 * A panel that displays all of its child widgets in a 'deck', where only one
 * can be visible at a time. It is used by
 * {@link com.google.gwt.user.client.ui.TabPanel}. (Method remove fixed)
 */
public class DeckedPanel extends ContainerPanel {

  private Vector widgets = new Vector();
  private int visibleWidget = -1;

  private int collapsedWidget = -1;

  public void add(Widget w) {
    insert(w, widgets.size());
  }

  /**
   * Gets the index of the currently-visible widget.
   *
   * @return the visible widget's index
   */
  public int getVisibleWidget() {
    return visibleWidget;
  }

  public boolean insert(Widget w, int beforeIndex) {
    DOM.setStyleAttribute(w.getElement(), "overflow", "auto");
    widgets.insertElementAt(w, beforeIndex);
    return true;
  }

  public boolean remove(Widget w) {
    Widget visibleW = getWidget();
    widgets.remove(w);
    if (visibleW == w) {
      super.remove(w);
      visibleWidget = -1;
    }
    else
      visibleWidget = widgets.indexOf(visibleW);
    return true;
  }

  /**
   * Shows the widget at the specified index. This causes the currently- visible
   * widget to be hidden.
   *
   * @param index the index of the widget to be shown
   */
  public void showWidget(int index) {
    if (visibleWidget != -1) {
      // Without setSize Opera collapses MessagePanel after removing visibleWidget
      setSize("" + getOffsetWidth(), "" + getOffsetHeight());
      super.remove(getWidget());
    }

    visibleWidget = index;

    Widget w = (Widget) widgets.get(index);
    w.setWidth("" + Utils.getClientWidth(this));
    w.setHeight("" + Utils.getClientHeight(this));
    super.add(w);
  }

  public void collapse() {
    Widget widget = getWidget();
    if (widget != null) {
      collapsedWidget = widgets.indexOf(widget);
      widget.setSize("5", "5");
    }
    setSize("15", "15");
  }

  public void adjustSize() {
    setSize("100%", "100%");
    if (collapsedWidget != -1) {
      showWidget(collapsedWidget);
      collapsedWidget = -1;
    }
  }

  public Widget getItem(int index) {
    return (Widget) widgets.get(index);
  }

  public int getItemCount() {
    return widgets.size();
  }

  public int getItemIndex(Widget child) {
    return widgets.indexOf(child);
  }

  public Iterator iterator() {
    return widgets.iterator();
  }
}

