package webirc.client;

import webirc.client.commands.MessageCommand;
import webirc.client.commands.NoticeCommand;
import webirc.client.utils.Utils;

import java.util.Date;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 17.09.2006 12:48:03
 */
public class CTCPHandler {

  public static final char CTCP_DELIM = '\u0001';

  public static final String COMMAND_ACTION = "ACTION";
  public static final String COMMAND_CLIENTINFO = "CLIENTINFO";
  public static final String COMMAND_DCC = "DCC";
  public static final String COMMAND_ERRORMSG = "ERRORMSG";
  public static final String COMMAND_FINGER = "FINGER";
  public static final String COMMAND_PING = "PING";
  public static final String COMMAND_SOURCE = "SOURCE";
  public static final String COMMAND_TIME = "TIME";
  public static final String COMMAND_USERINFO = "USERINFO";
  public static final String COMMAND_VERSION = "VERSION";

  public static void handleCTCP(String command, Vector parameters, User forUser) {
    String response = "";
    if (command.equalsIgnoreCase(COMMAND_CLIENTINFO))
      response = prepareCTCP(COMMAND_CLIENTINFO, "WebIRC currently knows these CTCP commands: " +
                                                 knownCommands() + '.');
    else if (command.equalsIgnoreCase(COMMAND_VERSION))
      response = prepareCTCP(COMMAND_VERSION, "WebIRC:" + WebIRC.VERSION + "(Build #" + WebIRC.BUILD + ")" +
                                              ':' + Utils.browserInfo());
    else if (command.equalsIgnoreCase(COMMAND_PING))
      response = prepareCTCP(COMMAND_PING, "" + parameters.firstElement());
    else if (command.equalsIgnoreCase(COMMAND_DCC))
      response = prepareCTCP(COMMAND_ERRORMSG, "WebIRC client does not support DCC.");
    else if (command.equalsIgnoreCase(COMMAND_USERINFO))
      response = prepareCTCP(COMMAND_USERINFO, MainSystem.getInstance().getUser().getRealName());
    else if (command.equalsIgnoreCase(COMMAND_FINGER))
      response = prepareCTCP(COMMAND_FINGER, MainSystem.getInstance().getUser().getRealName());
    else if (command.equalsIgnoreCase(COMMAND_TIME))
      response = prepareCTCP(COMMAND_TIME, new Date().toString());
    else if (command.equalsIgnoreCase(COMMAND_SOURCE))
      response = prepareCTCP(COMMAND_SOURCE, WebIRC.HOME);
    else if (command.equalsIgnoreCase(COMMAND_ACTION))
      return;
    else
      response = prepareCTCP(COMMAND_ERRORMSG, "Unknown CTCP command '" + command + "'.");

    NoticeCommand noticeCommand = new NoticeCommand(forUser, response);
    noticeCommand.setType(MessageCommand.TYPE_CTCP);
    MainSystem.getInstance().sendCommand(noticeCommand);
  }

  public static String getCTCPMessage(MessageCommand cmd) {
    String command = cmd.getCtcpCommand();
    String text = "";
    if (cmd.getText().length() > command.length() + 1)
      text = cmd.getText().substring(command.length() + 1);

    String response;
    User sender = cmd.getSender();
    if (sender != null)
      response = sender.getNickname() + ' ';
    else
      response = MainSystem.getInstance().getUserNick() + ' ';

    if (command.equalsIgnoreCase(COMMAND_CLIENTINFO))
      response += WebIRC.eventMessages.ctcpClientInfo();
    else if (command.equalsIgnoreCase(COMMAND_VERSION))
      response += WebIRC.eventMessages.ctcpVersion();
    else if (command.equalsIgnoreCase(COMMAND_PING))
      response += WebIRC.eventMessages.ctcpPing();
    else if (command.equalsIgnoreCase(COMMAND_DCC))
      response += WebIRC.eventMessages.ctcpDCC();
    else if (command.equalsIgnoreCase(COMMAND_USERINFO))
      response += WebIRC.eventMessages.ctcpUserInfo();
    else if (command.equalsIgnoreCase(COMMAND_FINGER))
      response += WebIRC.eventMessages.ctcpFinger();
    else if (command.equalsIgnoreCase(COMMAND_TIME))
      response += WebIRC.eventMessages.ctcpTime();
    else if (command.equalsIgnoreCase(COMMAND_SOURCE))
      response += WebIRC.eventMessages.ctcpSource();
    else if (command.equalsIgnoreCase(COMMAND_ACTION))
      response += text;
    else if (command.equalsIgnoreCase(COMMAND_ERRORMSG))
      response += WebIRC.eventMessages.ctcpErrorMsg(text);
    else
      response += WebIRC.eventMessages.ctcpUnknown(command);

    return response;
  }

  private static String prepareCTCP(String command, String text) {
    return CTCP_DELIM + command + ' ' + text + CTCP_DELIM;
  }

  private static String knownCommands() {
    return COMMAND_ACTION + ' ' + COMMAND_CLIENTINFO + ' ' + COMMAND_DCC + ' ' + COMMAND_ERRORMSG + ' ' +
           COMMAND_FINGER + ' ' + COMMAND_PING + ' ' + COMMAND_SOURCE + ' ' + COMMAND_TIME + ' ' +
           COMMAND_USERINFO + ' ' + COMMAND_VERSION;
  }
}
