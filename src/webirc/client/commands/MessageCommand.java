package webirc.client.commands;

import webirc.client.Channel;
import webirc.client.User;
import webirc.client.utils.Utils;

import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 15.07.2006 0:27:57
 */
public class MessageCommand extends IRCCommand {
  public static String getName() {
    return "PRIVMSG";
  }

  public static final char CTCP_DELIM = '\u0001';

  public static final int TYPE_MESSAGE = 0;
  public static final int TYPE_EVENT = 1;
  public static final int TYPE_ADDUSERMODE = 2;
  public static final int TYPE_REMOVEUSERMODE = 3;
  public static final int TYPE_BAN = 4;
  public static final int TYPE_UNBAN = 5;
  public static final int TYPE_MODE = 6;
  public static final int TYPE_ENTER = 7;
  public static final int TYPE_EXIT = 8;
  public static final int TYPE_KICK = 9;
  public static final int TYPE_CTCP = 10;
  public static final int TYPE_TOPIC = 11;

  /**
   * Message receiver
   */
  private Object receiver;
  /**
   * Message's text
   */
  private String text;

  /**
   * Type of message
   */
  private int type = TYPE_MESSAGE;

  /**
   * CTCP command's name
   */
  private String ctcpCommand;
  /**
   * CTCP command's parameters
   */
  private Vector ctcpParameters;

  public MessageCommand(String message, int type) {
    name = getName();
    text = message;
    this.type = type;
  }

  public MessageCommand(Object receiver, String text) {
    name = getName();
    this.receiver = receiver;
    this.text = text;
    params = ' ' + receiver.toString() + " :" + text;
  }

  /**
   * Parses incoming message command.
   *
   * @param prefix command's prefix
   * @param command command's name
   * @param params command's parameters
   */
  public MessageCommand(String prefix, String command, String params) {
    super(prefix, command, params);
    name = getName();
    String entity = nextParam();
    if (Utils.isChannel(entity))
      receiver = new Channel(entity);
    else
      receiver = new User(entity);
    text = lastParam();

    // If the command contents CTCP command inside...
    if (isCTCP()) {
      type = TYPE_CTCP;
      // Removing the CTCP command delimeters
      text = text.substring(1, text.length() - 1);
      String[] ctcpContent = text.split(" ");
      // Checking what CTCP command it is and it's parameters
      if (ctcpContent != null && ctcpContent.length > 0) {
        ctcpCommand = ctcpContent[0];
        ctcpParameters = new Vector();
        for (int i = 1; i < ctcpContent.length; i++)
          ctcpParameters.add(ctcpContent[i]);
      }
    }
  }

  public Object getReceiver() {
    return receiver;
  }

  public String getText() {
    return text;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getCtcpCommand() {
    return ctcpCommand;
  }

  public Vector getCtcpParameters() {
    return ctcpParameters;
  }

  public String getCtcpText() {
    String result = null;
    if (isCTCP()) {
      int index = text.indexOf(' ');
      if (index != -1 && index + 1 <= text.length() - 1)
        result = text.substring(1 + index, text.length() - 1);
    }
    return result;
  }

  /**
   * Determines whether the message command contents CTCP command.
   *
   * @return true if there is a CTCP command
   */
  private boolean isCTCP() {
    char firstChar = text.charAt(0);
    char lastChar = text.charAt(text.length() - 1);
    return firstChar == CTCP_DELIM && lastChar == CTCP_DELIM;
  }
}