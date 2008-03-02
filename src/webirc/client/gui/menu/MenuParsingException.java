package webirc.client.gui.menu;

/**
 * @author Ayzen
 * @version 1.0 26.02.2007 0:30:27
 */
public class MenuParsingException extends Exception {

  public MenuParsingException(String message) {
    super("Exception while parsing menus file:\n" + message);
  }

}
