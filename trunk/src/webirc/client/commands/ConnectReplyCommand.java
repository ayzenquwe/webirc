package webirc.client.commands;

import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 22.07.2006 13:37:08
 */
public class ConnectReplyCommand extends ReplyCommand {
  public ConnectReplyCommand(int replyType, String params) {
    super(replyType, params);
    parameters = new Vector();

    if (isWelcomeReply()) {
      String userDataString = params.substring(params.lastIndexOf(" ") + 1);
      String[] userData = userDataString.split("[!@]");
      if (userData.length > 0) {
        parameters.add(userData[0]);
        if (userData.length > 1) {
          parameters.add(userData[1]);
          if (userData.length > 2)
            parameters.add(userData[2]);
        }
      }
    }
  }

  public boolean isWelcomeReply() {
    return replyType == 1;
  }
}
