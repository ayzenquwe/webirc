package webirc.client.commands;

import webirc.client.Channel;

import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 06.08.2006 18:01:39
 */
public class ErrorReplyCommand extends ReplyCommand {
  public ErrorReplyCommand(int replyType, String params) {
    super(replyType, params);
    parameters = new Vector();

    if (isBadChannelKey()) {
      parameters.add(new Channel(nextParam()));
      parameters.add(lastParam());
    }
    else {
      while (params.charAt(paramsIndex + 1) != ':')
        parameters.add(nextParam());
      parameters.add(lastParam());
    }
  }

  public boolean isNoNicknameGiven() {
    return replyType == 431;
  }
  
  public boolean isErroneusNickname() {
    return replyType == 432;
  }

  public boolean isNicknameInUse() {
    return replyType == 433;
  }

  public boolean isNickCollision() {
    return replyType == 436;
  }

  public boolean isUnavailResource() {
    return replyType == 437;
  }

  public boolean isBadChannelKey() {
    return replyType == 475;
  }

}
