package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 05.08.2006 16:39:13
 */
public class NickCommand extends IRCCommand {
  public static String getName() {
    return "NICK";
  }

  private String nick;

  public NickCommand(String nick) {
    name = getName();
    params = ' ' + nick;
  }

  public NickCommand(String prefix, String command, String params) {
    super(prefix, command, params);
    name = getName();
    nick = lastParam();
  }

  public String getNick() {
    return nick;
  }
}
