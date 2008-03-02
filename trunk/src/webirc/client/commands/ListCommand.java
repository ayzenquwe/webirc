package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 16.09.2006 17:06:07
 */
public class ListCommand extends IRCCommand {
  public static String getName() {
    return "LIST";
  }

  public ListCommand() {
    name = getName();
    params = "";
  }

}
