package webirc.client.commands;

import java.util.List;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 30.06.2006 16:46:49
 */
public class CommandParser {

  public List parseMessages(String message) {
    String prefix = "";
    String command = "";
    String params = "";
    int index;
    int paramsIndex;
    List result = new Vector();

    String[] messages = message.split("\n");
    for (int i = 0; i < messages.length; i++) {
      index = 0;
      // Trying to get the Prefix
      try {
        if (messages[i].charAt(index) == ':') {
          index = messages[i].indexOf(' ');
          prefix = messages[i].substring(1, index);
          // Skipping space
          index++;
        }
      }
      catch (Exception e) {
        // Do nothing
      }
      if (index != -1) {
        // Getting the command
        paramsIndex = messages[i].indexOf(' ', index);
        if (paramsIndex != -1) {
          command = messages[i].substring(index, paramsIndex);
          // Getting the Params string
          params = messages[i].substring(paramsIndex);
        } else {
          command = messages[i].substring(index);
          params = "";
        }
      }
      result.add(getCommand(prefix, command, params));
    }

    return result;
  }

  public IRCCommand parseMessage(String message) {
    List commands = parseMessages(message);
    if (!commands.isEmpty())
      return (IRCCommand) commands.get(0);
    else
      return null;
  }

  private IRCCommand getCommand(String prefix, String command, String params) {
    ReplyCommand reply = ReplyCommand.isReply(command, params);
    if (reply != null)
      return reply;
    else if (command.equalsIgnoreCase(MessageCommand.getName()))
      return new MessageCommand(prefix, command, params);
    else if (command.equalsIgnoreCase(NoticeCommand.getName()))
      return new NoticeCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(JoinCommand.getName()))
      return new JoinCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(PartCommand.getName()))
      return new PartCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(NickCommand.getName()))
      return new NickCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(QuitCommand.getName()))
      return new QuitCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(KickCommand.getName()))
      return new KickCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(ModeCommand.getName()))
      return new ModeCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(TopicCommand.getName()))
      return new TopicCommand(prefix, command, params);
    else if(command.equalsIgnoreCase(ErrorCommand.getName()))
      return new ErrorCommand(prefix, command, params);
    else
      return new UnknownCommand(prefix, command, params);
  }
}
