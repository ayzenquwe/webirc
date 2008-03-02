package webirc.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author Ayzen
 * @version 1.0 20.08.2006 17:15:01
 */
public interface EventMessages extends Messages {
  public String youChangedNick(String newNick);
  public String someoneChangedNick(String oldNick, String newNick);
  public String youQuit();
  public String userQuit(String nick, String saying);
  public String saying(String saying);
  public String userJoins(String nick);
  public String youPart();
  public String userPart(String nick);
  public String youHaveBeenKicked(String channel, String who, String reason);
  public String youHaveBeenKickedMsg(String who, String reason);
  public String someoneHasBeenKicked(String nick, String who, String reason);
  public String changeMode(String who, String mode);
  public String addUserMode(String who, String type, String forUser);
  public String removeUserMode(String who, String type, String forUser);
  public String ban(String who, String mask);
  public String unban(String who, String mask);
  public String topic(String topic);
  public String topicChanged(String who, String topic);

  public String ctcpClientInfo();
  public String ctcpDCC();
  public String ctcpErrorMsg(String message);
  public String ctcpFinger();
  public String ctcpPing();
  public String ctcpSource();
  public String ctcpTime();
  public String ctcpUserInfo();
  public String ctcpVersion();
  public String ctcpUnknown(String command);

  public String ctcpResponse(String command, String response);
  public String ctcpResponseSent(String response);
}
