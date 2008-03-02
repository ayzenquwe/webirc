package webirc.client.gui.decorators;

/**
 * @author Ayzen
 * @version 1.0 17.12.2006 12:47:56
 */
public class NoSuchDecoratorException extends Exception {

  private String decoratorId;

  public NoSuchDecoratorException(String decoratorId) {
    this.decoratorId = decoratorId;
  }

  public String getDecoratorId() {
    return decoratorId;
  }

  public String getMessage() {
    return "Couldn't find decorator with id='" + decoratorId + "'";
  }

}