package webirc.client.gui.decorators;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Ayzen
 * @version 1.0 17.12.2006 0:18:16
 */
public class Decorator extends ComplexPanel {

  protected static final String IMPL = "_impl";

  public Decorator() {
    setElement(DOM.createDiv());
  }

  protected Decorator(Element element) {
    setElement(element);
  }

  /**
   * Creates filled decorator, HTML to be filled with it searches by id.
   *
   * @param id content's id
   * @throws NoSuchDecoratorException if content with such id is not found
   */
  protected Decorator(String id) throws NoSuchDecoratorException {
    this();

    // Checks if decoratorHTML is already initializated, if not - initializes it
    String decoratorHTML = getDecoratorHTML();
    if (decoratorHTML == null) {
      Element element = DOM.getElementById(id);
      if (element == null)
        throw new NoSuchDecoratorException(id);
      decoratorHTML = DOM.getInnerHTML(element);
      decoratorHTML = prepareContent(decoratorHTML);
      setDecoratorHTML(decoratorHTML);
    }

    DOM.setInnerHTML(getElement(), decoratorHTML);
  }

  protected String getDecoratorHTML() {
    return null;
  }

  protected void setDecoratorHTML(String decoratorHTML) {}

  public void add(Widget w, int beforeIndex) {
    insert(w, getElement(), beforeIndex);
  }

  public void add(Widget w) {
    add(w, getElement());
  }

  /**
   * Adds a decorator widget to the panel. There isn't DOM.appendChild(), because a decorator is already
   * in the DOM structure.
   *
   * @param decorator the decorator widget to be added
   */
  public void addDecorator(Decorator decorator) {
    if (decorator == null)
      return;

    decorator.setParent(this);
    getChildren().add(decorator);
  }

  /**
   * Searches for the decorator by id in the DOM structure and then adds it to the parent decorator.
   *
   * @param id the id of decorator to be added
   * @return found decorator
   * @throws NoSuchDecoratorException throws if there are no decorator with such id in the DOM
   */
  protected Decorator addDecorator(String id) throws NoSuchDecoratorException {
    Element element = findElement(id);

    Decorator decorator = new Decorator(element);
    addDecorator(decorator);

    return decorator;
  }

  protected Element findElement(String id) throws NoSuchDecoratorException {
    Element element = DOM.getElementById(id);
    if (element == null)
      throw new NoSuchDecoratorException(id);

    DOM.setAttribute(element, "id", "");

    return element;
  }

  protected String prepareContent(String content) {
    return content;
  }

}