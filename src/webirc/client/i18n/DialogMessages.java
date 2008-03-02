package webirc.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author Ayzen
 * @version 1.0 20.08.2006 1:07:46
 */
public interface DialogMessages extends Messages {
  public String ok();
  public String cancel();
  public String yes();
  public String no();
  public String enter();
  public String close();
  public String information();
  public String error();
  public String confirmation();
  public String inputDialog();
  public String connect();
  public String welcomeToWebIRC(String version);
  public String nickname();
  public String emptyNickname();
  public String enterNickname();

  public String listOfChannels();
  public String channelName();
  public String users();
  public String channelTopic();
  public String enterTheKey();
  public String channelNeedsKey(String channel);
}

