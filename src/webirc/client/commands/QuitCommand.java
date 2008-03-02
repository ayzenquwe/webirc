package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 05.08.2006 23:34:22
 */
public class QuitCommand extends IRCCommand {
  public static String getName() {
    return "QUIT";
  }

  private String message;

  public QuitCommand(String message) {
    name = getName();
    params = " :" + message;
  }

  public QuitCommand(String prefix, String command, String params) {
    super(prefix, command, params);
    name = getName();
    message = lastParam();
  }

  public String getMessage() {
    return message;
  }
}
