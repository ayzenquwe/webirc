package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 02.09.2006 23:24:26
 */
public class NoticeCommand extends MessageCommand {
  
  public static String getName() {
    return "NOTICE";
  }

  public NoticeCommand(String message, int type) {
    super(message, type);
    name = getName();
  }

  public NoticeCommand(String prefix, String command, String params) {
    super(prefix, command, params);
    name = getName();
  }

  public NoticeCommand(Object receiver, String text) {
    super(receiver, text);
    name = getName();
  }

}
