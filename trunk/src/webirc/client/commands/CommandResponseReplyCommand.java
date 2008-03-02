package webirc.client.commands;

import webirc.client.Channel;

import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 22.07.2006 15:20:43
 */
public class CommandResponseReplyCommand extends ReplyCommand {
  public CommandResponseReplyCommand(int replyType, String params) {
    super(replyType, params);
    parameters = new Vector();

    if (isNamReply()) {
      // Type of channel
      nextParam();
      // Channel name
      Channel channel = new Channel(nextParam());
      // Array of names
      String[] names = lastParam().split(" ");
      parameters.add(channel);
      parameters.add(names);
    }
    else if (isEndOfNames()) {
      // Channel name
      parameters.add(new Channel(nextParam()));
      parameters.add(lastParam());
    }
    else if (isList()) {
      // Channel name
      parameters.add(nextParam());
      // Count of users on channel
      parameters.add(nextParam());
      // Channel's topic
      parameters.add(lastParam());
    }
    else if (isTopic()) {
      // Channel name
      parameters.add(new Channel(nextParam()));
      // Channel's topic
      parameters.add(lastParam());
    }  
  }

  public boolean isListStart() {
    return replyType == 321;
  }

  public boolean isList() {
    return replyType == 322;
  }

  public boolean isListEnd() {
    return replyType == 323;
  }

  public boolean isTopic() {
    return replyType == 332;
  }

  public boolean isNamReply() {
    return replyType == 353;
  }

  public boolean isEndOfNames() {
    return replyType == 366;
  }
  
}
