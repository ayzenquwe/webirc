package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 06.08.2006 15:37:40
 */
public class ErrorCommand extends IRCCommand {
  public static String getName() {
    return "ERROR";
  }

  private String message;

  public ErrorCommand(String message) {
    name = getName();
    params = " :" + message;
  }

  public ErrorCommand(String prefix, String command, String params) {
    super(prefix, command, params);
    name = getName();
    message = lastParam();
  }

  public String getMessage() {
    return message;
  }
}
