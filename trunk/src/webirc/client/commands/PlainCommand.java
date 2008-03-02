package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 28.08.2006 23:13:30
 */
public class PlainCommand extends IRCCommand {

  String message;

  public PlainCommand(String message) {
    this.message = message;
  }

  public String getFullMessage() {
    return message;
  }
}
