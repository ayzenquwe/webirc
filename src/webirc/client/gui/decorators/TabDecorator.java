package webirc.client.gui.decorators;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import webirc.client.MainSystem;

/**
 * @author Ayzen
 * @version 1.0 29.12.2006 13:38:10
 */
public class TabDecorator extends Decorator {

  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_SELECTED = 1;
  public static final int TYPE_HOVER = 2;
  public static final int TYPE_BLINK = 3;
  public static final int TYPE_CHANGED = 4;

  private static final String ID = "decorator_tab";
  private static final String ID_CONTENT = "decorator_tab_content";
  private static final String ID_LEFT = "decorator_tab_left";
  private static final String ID_RIGHT = "decorator_tab_right";

  // Normal styles
  private static final String STYLE_LEFT_NORMAL = "tab-left";
  private static final String STYLE_RIGHT_NORMAL = "tab-right";
  private static final String STYLE_CONTENT_NORMAL = "tab-content";
  // Selected styles
  private static final String STYLE_LEFT_SELECTED = "tab-left-selected";
  private static final String STYLE_RIGHT_SELECTED = "tab-right-selected";
  private static final String STYLE_CONTENT_SELECTED = "tab-content-selected";
  // Hovered styles
  private static final String STYLE_LEFT_HOVER = "tab-left-hover";
  private static final String STYLE_RIGHT_HOVER = "tab-right-hover";
  private static final String STYLE_CONTENT_HOVER = "tab-content-hover";
  // Blinked styles
  private static final String STYLE_LEFT_BLINK = "tab-left-blink";
  private static final String STYLE_RIGHT_BLINK = "tab-right-blink";
  private static final String STYLE_CONTENT_BLINK = "tab-content-blink";
  // Changed styles
  private static final String STYLE_LEFT_CHANGED = "tab-left-changed";
  private static final String STYLE_RIGHT_CHANGED = "tab-right-changed";
  private static final String STYLE_CONTENT_CHANGED = "tab-content-changed";

  private static String decoratorHTML = null;

  private boolean attached = false;

  private Panel contentPanel = null;
  private Element left = null;
  private Element right = null;

  private SimplePanel tempPanel = new SimplePanel();

  public TabDecorator() throws NoSuchDecoratorException {
    super(ID);
  }

  protected String getDecoratorHTML() {
    return decoratorHTML;
  }

  protected void setDecoratorHTML(String decoratorHTML) {
    TabDecorator.decoratorHTML = decoratorHTML;
  }

  public void initDecorator() throws NoSuchDecoratorException, DecoratorNotAttachedException {
    if (!isAttached())
      throw new DecoratorNotAttachedException("TabDecorator");

    contentPanel = addDecorator(ID_CONTENT + IMPL);
    left = findElement(ID_LEFT + IMPL);
    right = findElement(ID_RIGHT + IMPL);

    addStyleType(TYPE_NORMAL);
  }

  protected String prepareContent(String content) {
    if (content == null)
      return null;

    String modified = content.replaceAll(ID_CONTENT, ID_CONTENT + IMPL);
    modified = modified.replaceAll(ID_LEFT, ID_LEFT + IMPL);
    modified = modified.replaceAll(ID_RIGHT, ID_RIGHT + IMPL);
    return modified;
  }

  protected void onAttach() {
    super.onAttach();
    if (!attached)
      try {
        initDecorator();
        contentPanel.add(tempPanel.getWidget());
        attached = true;
      }
      catch (NoSuchDecoratorException e) {
        MainSystem.showError("Couldn't found tab decorator with id - '" + e.getDecoratorId() + "'");
      }
      catch (DecoratorNotAttachedException e) {
        MainSystem.showError("Tab decorator is not attached");
      }
  }

  private void setStyleType(int type, boolean add) {
    switch (type) {
      case TYPE_NORMAL:
        setStyleName(left, STYLE_LEFT_NORMAL, add);
        setStyleName(right, STYLE_RIGHT_NORMAL, add);
        setStyleName(contentPanel.getElement(), STYLE_CONTENT_NORMAL, add);
        break;
      case TYPE_SELECTED:
        setStyleName(left, STYLE_LEFT_SELECTED, add);
        setStyleName(right, STYLE_RIGHT_SELECTED, add);
        setStyleName(contentPanel.getElement(), STYLE_CONTENT_SELECTED, add);
        break;
      case TYPE_HOVER:
        setStyleName(left, STYLE_LEFT_HOVER, add);
        setStyleName(right, STYLE_RIGHT_HOVER, add);
        setStyleName(contentPanel.getElement(), STYLE_CONTENT_HOVER, add);
        break;
      case TYPE_BLINK:
        setStyleName(left, STYLE_LEFT_BLINK, add);
        setStyleName(right, STYLE_RIGHT_BLINK, add);
        setStyleName(contentPanel.getElement(), STYLE_CONTENT_BLINK, add);
        break;
      case TYPE_CHANGED:
        setStyleName(left, STYLE_LEFT_CHANGED, add);
        setStyleName(right, STYLE_RIGHT_CHANGED, add);
        setStyleName(contentPanel.getElement(), STYLE_CONTENT_CHANGED, add);
        break;
    }
  }

  public void addStyleType(int type) {
    setStyleType(type, true);
  }

  public void removeStyleType(int type) {
    setStyleType(type, false);
  }

  public Panel getContentPanel() {
    return contentPanel != null ? contentPanel : tempPanel;
  }

}
