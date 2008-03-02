package webirc.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author Ayzen
 * @version 1.0 20.08.2006 14:39:17
 */
public interface MainMessages extends Messages {
  public String login();
  public String logout();
  public String logoutConfirmation();
  public String loginConfirmation();
  public String exitConfirmation();
  public String reloginConfirmation();
  public String webIRCAbout();
  public String channels();
  public String users();
  public String enter();
  public String list();
  public String send();
  public String server();
  public String unknownCommand();
  public String connectReply();
  public String commandResponseReply();
  public String errorReply();
  public String unknownReply();
  public String info();
  public String error();
  public String unknown();
  public String system();
  public String notice();
  public String ban();
  public String unban();

  public String channel();
  public String channelOwner();
  public String admin();
  public String operator();
  public String halfOperator();
  public String voice();
  public String message();

  public String channelName();
}
