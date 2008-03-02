package webirc.client.gui.decorators;

import com.google.gwt.user.client.ui.Panel;

/**
 * @author Ayzen
 * @version 1.0 17.12.2006 15:51:38
 */
public class DialogDecorator extends Decorator {

  private static final String ID = "decorator_dialog";
  private static final String ID_HEADER = "decorator_dialog_header";
  private static final String ID_CONTENT = "decorator_dialog_body";
  private static final String ID_BUTTONS = "decorator_dialog_buttons";

  private static String decoratorHTML = null;

  /**
   * Content of dialog's header
   */
  private Panel headerPanel = null;
  /**
   * Content of dialog's body
   */
  private Panel contentPanel = null;
  /**
   * Space for buttons
   */
  private Panel buttonsPanel = null;

  /**
   * Creates decorator for dialog window.
   *
   * @throws NoSuchDecoratorException throws if it couldn't found HTML content or some parts for decorator
   */
  public DialogDecorator() throws NoSuchDecoratorException {
    super(ID);
  }

  protected String getDecoratorHTML() {
    return decoratorHTML;
  }

  protected void setDecoratorHTML(String decoratorHTML) {
    DialogDecorator.decoratorHTML = decoratorHTML;
  }

  public void initDecorator() throws NoSuchDecoratorException, DecoratorNotAttachedException {
    if (!isAttached())
      throw new DecoratorNotAttachedException("DialogDecorator");

    headerPanel = addDecorator(ID_HEADER + IMPL);
    contentPanel = addDecorator(ID_CONTENT + IMPL);
    buttonsPanel = addDecorator(ID_BUTTONS + IMPL);
  }

  protected String prepareContent(String content) {
    if (content == null)
      return null;

    String modified = content.replaceAll(ID_HEADER, ID_HEADER + IMPL);
    modified = modified.replaceAll(ID_CONTENT, ID_CONTENT + IMPL);
    modified = modified.replaceAll(ID_BUTTONS, ID_BUTTONS + IMPL);
    return modified;
  }

  public Panel getButtonsPanel() {
    return buttonsPanel;
  }

  public Panel getContentPanel() {
    return contentPanel;
  }

  public Panel getHeaderPanel() {
    return headerPanel;
  }
  
}
