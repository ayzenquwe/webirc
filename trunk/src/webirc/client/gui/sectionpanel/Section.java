package webirc.client.gui.sectionpanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.*;
import webirc.client.utils.Utils;
import webirc.client.gui.ContainerPanel;

import java.util.Iterator;

/**
 * @author Ayzen
 * @version 1.0 09.08.2006 22:15:05
 */
public abstract class Section extends Composite implements HasWidgets {

  public static final String STYLE = "gwt-Section";
  public static final String STYLE_HEADER = "gwt-Section-Header";
  public static final String STYLE_CONTENT = "gwt-Section-Content";

  /**
   * The main panel that has header, not collapsed space and content
   */
  protected VerticalPanel mainPanel = new VerticalPanel();
  /**
   * Scrollable area in which content is located
   */
  protected SimplePanel scrollArea = new SimplePanel();
  /**
   * Container for scrollable area
   */
  protected ContainerPanel scrollAreaContainer = new ContainerPanel(scrollArea);
  /**
   * The header of section. Simply has a label with section's name
   */
  protected HTML header = new HTML();
  /**
   * The content of section. This panel can be collapsed
   */
  protected VerticalPanel contentPanel = new VerticalPanel();
  /**
   * If true there will be a widget counter next to section's name
   */
  private boolean showCounter = false;

  private int widgetCounter = 0;
  private String title = "";

  protected boolean collapsed = false;

  public Section() {
    scrollArea.add(contentPanel);
    contentPanel.setWidth("100%");
    mainPanel.setSize("90%", "100%");
    mainPanel.add(header);
    mainPanel.setCellHorizontalAlignment(header, VerticalPanel.ALIGN_CENTER);
    mainPanel.add(scrollAreaContainer);
    mainPanel.setCellHeight(scrollAreaContainer, "100%");
    initWidget(mainPanel);
    DOM.setStyleAttribute(scrollArea.getElement(), "overflow", "auto");

    mainPanel.setStyleName(STYLE);
    header.setStyleName(STYLE_HEADER);
    contentPanel.setStyleName(STYLE_CONTENT);
  }

  public void setTitle(String title) {
    this.title = title;
    header.setText(title);
  }

  public void setHeight(String height) {
    scrollArea.setHeight("10");
    super.setHeight(height);
    Element parent = DOM.getParent(getElement());
    DOM.setStyleAttribute(parent, "height", height);

    scrollAreaContainer.setHeight("100%");
    Utils.maximize(scrollArea);
  }

  public Iterator iterator() {
    return contentPanel.iterator();
  }

  public void add(Widget widget) {
    contentPanel.add(widget);
    contentPanel.setCellWidth(widget, "100%");
    widgetCounter++;
    updateCounter();
  }

  public boolean remove(Widget widget) {
    widgetCounter--;
    updateCounter();
    return contentPanel.remove(widget);
  }

  public void clear() {
    contentPanel.clear();
    widgetCounter = 0;
    updateCounter();
  }

  public void insert(Widget widget, int position) {
    contentPanel.insert(widget, position);
    widgetCounter++;
    updateCounter();
  }

  protected void updateCounter() {
    if (showCounter) {
      String counter = widgetCounter != 0 ? " (" + widgetCounter + ")" : "";
      header.setText(title + counter);
    }
  }

  public void fixup() {
    setHeight(getOffsetHeight() + "");
  }

  public boolean isShowCounter() {
    return showCounter;
  }

  public void setShowCounter(boolean showCounter) {
    this.showCounter = showCounter;
  }

}