package webirc.client.gui.decorators;

/**
 * @author Ayzen
 * @version 1.0 17.12.2006 16:55:31
 */
public class DecoratorNotAttachedException extends Exception {

  private String decoratorName;

  public DecoratorNotAttachedException(String name) {
    decoratorName = name;
  }

  public String getMessage() {
    return "Decorator '" + decoratorName + "' is not attached";
  }

}
