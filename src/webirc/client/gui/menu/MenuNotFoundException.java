package webirc.client.gui.menu;

/**
 * @author Ayzen
 * @version 1.0 25.02.2007 19:47:54
 */
public class MenuNotFoundException extends Exception {

  public MenuNotFoundException(String menuName) {
    super("Menu '" + menuName + "' is not found");
  }

}
