package webirc.client.commands;

import java.util.Collection;

/**
 * @author Ayzen
 * @version 1.0 22.07.2006 13:16:33
 */
public abstract class ReplyCommand extends IRCCommand {

  protected int replyType;
  private String receiver;
  private String replyMessage;
  protected Collection parameters;

  protected ReplyCommand(int replyType, String params) {
    super(null, null, params);
    this.replyType = replyType;
    receiver = nextParam();
    replyMessage = lastParam();
  }

  public static ReplyCommand isReply(String command, String params) {
    int replyType;
    try {
      replyType = Integer.parseInt(command);
    } catch (NumberFormatException e) {
      return null;
    }
    ReplyCommand result;
    if (isConnectReply(replyType))
      result = new ConnectReplyCommand(replyType, params);
    else if (isCommandResponseReply(replyType))
      result = new CommandResponseReplyCommand(replyType, params);
    else if (isErrorReply(replyType))
      result = new ErrorReplyCommand(replyType, params);
    else
      result = new UnknownReplyCommand(replyType, params);
    return result;
  }

  public String getReceiver() {
    return receiver;
  }

  public String getReplyMessage() {
    return replyMessage;
  }

  public int getReplyType() {
    return replyType;
  }

  public Collection getParameters() {
    return parameters;
  }

  private static boolean isConnectReply(int replyType) {
    return replyType > 0 && replyType < 100;
  }

  private static boolean isCommandResponseReply(int replyType) {
    return replyType >= 200 && replyType < 400;
  }

  private static boolean isErrorReply(int replyType) {
    return replyType >= 400 && replyType < 600;
  }
}
