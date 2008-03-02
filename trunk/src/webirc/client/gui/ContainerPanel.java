package webirc.client.gui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that based on TABLE element and can contain only one widget.
 * The main different is correct setWidth("100%", "100%") working on Opera and Mozilla and
 * by default it has this size.
 *
 * @author Ayzen
 * @version 1.0 27.07.2006 0:27:36
 */
public class ContainerPanel extends SimplePanel implements HasHorizontalAlignment, HasVerticalAlignment {

  Element containerElement;
  private HorizontalAlignmentConstant horizAlign = HasHorizontalAlignment.ALIGN_LEFT;
  private VerticalAlignmentConstant vertAlign = HasVerticalAlignment.ALIGN_TOP;

  /**
   * Creates simple TABLE with only one cell.
   */
  public ContainerPanel() {
    setElement(DOM.createTable());
    Element body = DOM.createTBody();
    Element tr = DOM.createTR();
    Element td = DOM.createTD();
    DOM.appendChild(getElement(), body);
    DOM.appendChild(body, tr);
    DOM.appendChild(tr, td);
    containerElement = td;

    DOM.setAttribute(getElement(), "cellSpacing", "0");
    DOM.setAttribute(getElement(), "cellPadding", "0");

    setHorizontalAlignment(horizAlign);
    setVerticalAlignment(vertAlign);

    setSize("100%", "100%");
    DOM.setStyleAttribute(containerElement, "width", "100%");
    DOM.setStyleAttribute(containerElement, "height", "100%");
  }

  public ContainerPanel(Widget childWidget) {
    this();
    add(childWidget);
  }

  public ContainerPanel(String style) {
    this();
    setStyleName(style);
  }

  public ContainerPanel(Widget childWidget, String style) {
    this();
    setStyleName(style);
    add(childWidget);
  }

  public void add(Widget w) {
    super.add(w);
  }

  public boolean remove(Widget w) {
    if (super.remove(w)) {
      try {
        setWidget(null);
      } catch (Exception e) {/**/}
      return true;
    }
    return false;
  }

  protected Element getContainerElement() {
    return containerElement;
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizAlign;
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    DOM.setAttribute(getContainerElement(), "align", align.getTextAlignString());
    horizAlign = align;
  }

  public VerticalAlignmentConstant getVerticalAlignment() {
    return vertAlign;
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    DOM.setStyleAttribute(getContainerElement(), "verticalAlign", align.getVerticalAlignString());
    vertAlign = align;
  }
}
