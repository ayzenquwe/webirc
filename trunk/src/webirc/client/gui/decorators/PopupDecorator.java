package webirc.client.gui.decorators;

import com.google.gwt.user.client.ui.Panel;

/**
 * @author Ayzen
 * @version 1.0 03.01.2007 18:30:12
 */
public class PopupDecorator extends Decorator {

  private static final String ID = "decorator_popup";
  private static final String ID_CONTENT = "decorator_popup_content";

  private static String decoratorHTML = null;

  /**
   * Content of popup's body
   */
  private Panel contentPanel = null;

  /**
   * Creates decorator for popup window.
   *
   * @throws NoSuchDecoratorException throws if it couldn't found HTML content or some parts for decorator
   */
  public PopupDecorator() throws NoSuchDecoratorException {
    super(ID);
  }

  protected String getDecoratorHTML() {
    return decoratorHTML;
  }

  protected void setDecoratorHTML(String decoratorHTML) {
    PopupDecorator.decoratorHTML = decoratorHTML;
  }

  public void initDecorator() throws NoSuchDecoratorException, DecoratorNotAttachedException {
    if (!isAttached())
      throw new DecoratorNotAttachedException("PopupDecorator");

    contentPanel = addDecorator(ID_CONTENT + IMPL);
  }

  protected String prepareContent(String content) {
    if (content == null)
      return null;

    return content.replaceAll(ID_CONTENT, ID_CONTENT + IMPL);
  }

  public Panel getContentPanel() {
    return contentPanel;
  }

}
