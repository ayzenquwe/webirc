package webirc.client.gui;

import com.google.gwt.user.client.ui.Label;
import webirc.client.User;
import webirc.client.WebIRC;

/**
 * @author Ayzen
 * @version 1.0 02.09.2006 16:21:30
 */
public class StatusIcon extends Label {

  public static final String STYLE_ICON = "gwt-Icon";
  public static final String STYLE_ICON_CHANOWNER = "gwt-Icon-ChannelOwner";
  public static final String STYLE_ICON_ADMIN = "gwt-Icon-Admin";
  public static final String STYLE_ICON_OPERATOR = "gwt-Icon-Operator";
  public static final String STYLE_ICON_HALFOP = "gwt-Icon-HalfOperator";
  public static final String STYLE_ICON_VOICE = "gwt-Icon-Voice";
  public static final String STYLE_ICON_CHANNEL = "gwt-Icon-Channel";
  public static final String STYLE_ICON_INFO = "gwt-Icon-Info";
  public static final String STYLE_ICON_ERROR = "gwt-Icon-Error";
  public static final String STYLE_ICON_UNKNOWN = "gwt-Icon-Unknown";
  public static final String STYLE_ICON_SYSTEM = "gwt-Icon-System";
  public static final String STYLE_ICON_NOTICE = "gwt-Icon-Notice";
  public static final String STYLE_ICON_BAN = "gwt-Icon-Ban";
  public static final String STYLE_ICON_UNBAN = "gwt-Icon-Unban";
  public static final String STYLE_ICON_ENTER = "gwt-Icon-User-Enter";
  public static final String STYLE_ICON_EXIT = "gwt-Icon-User-Exit";
  public static final String STYLE_ICON_KICK = "gwt-Icon-Kick";

  public static final int TYPE_EMPTY = 0;
  public static final int TYPE_CHANOWNER = 1;
  public static final int TYPE_ADMIN = 2;
  public static final int TYPE_OPERATOR = 3;
  public static final int TYPE_HALFOP = 4;
  public static final int TYPE_VOICE = 5;
  public static final int TYPE_CHANNEL = 6;
  public static final int TYPE_INFO = 7;
  public static final int TYPE_ERROR = 8;
  public static final int TYPE_UNKNOWN = 9;
  public static final int TYPE_SYSTEM = 10;
  public static final int TYPE_NOTICE = 11;
  public static final int TYPE_BAN = 12;
  public static final int TYPE_UNBAN = 13;
  public static final int TYPE_ENTER = 14;
  public static final int TYPE_EXIT = 15;
  public static final int TYPE_KICK = 16;

  private int type;

  public StatusIcon() {
    setType(TYPE_EMPTY);
  }

  public StatusIcon(int type) {
    super();
    setType(type);
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
    switch (type) {
      case TYPE_EMPTY:
        setStyleName(STYLE_ICON);
        setTitle("");
        break;
      case TYPE_CHANNEL:
        setStyleName(STYLE_ICON_CHANNEL);
        setTitle(WebIRC.mainMessages.channel());
        break;
      case TYPE_CHANOWNER:
        setStyleName(STYLE_ICON_CHANOWNER);
        setTitle(WebIRC.mainMessages.channelOwner());
        break;
      case TYPE_ADMIN:
        setStyleName(STYLE_ICON_ADMIN);
        setTitle(WebIRC.mainMessages.admin());
        break;
      case TYPE_OPERATOR:
        setStyleName(STYLE_ICON_OPERATOR);
        setTitle(WebIRC.mainMessages.operator());
        break;
      case TYPE_HALFOP:
        setStyleName(STYLE_ICON_HALFOP);
        setTitle(WebIRC.mainMessages.halfOperator());
        break;
      case TYPE_VOICE:
        setStyleName(STYLE_ICON_VOICE);
        setTitle(WebIRC.mainMessages.voice());
        break;
      case TYPE_INFO:
        setStyleName(STYLE_ICON_INFO);
        setTitle(WebIRC.mainMessages.info());
        break;
      case TYPE_ERROR:
        setStyleName(STYLE_ICON_ERROR);
        setTitle(WebIRC.mainMessages.error());
        break;
      case TYPE_UNKNOWN:
        setStyleName(STYLE_ICON_UNKNOWN);
        setTitle(WebIRC.mainMessages.unknown());
        break;
      case TYPE_SYSTEM:
        setStyleName(STYLE_ICON_SYSTEM);
        setTitle(WebIRC.mainMessages.system());
        break;
      case TYPE_NOTICE:
        setStyleName(STYLE_ICON_NOTICE);
        setTitle(WebIRC.mainMessages.notice());
        break;
      case TYPE_BAN:
        setStyleName(STYLE_ICON_BAN);
        setTitle(WebIRC.mainMessages.ban());
        break;
      case TYPE_UNBAN:
        setStyleName(STYLE_ICON_UNBAN);
        setTitle(WebIRC.mainMessages.unban());
        break;
      case TYPE_ENTER:
        setStyleName(STYLE_ICON_ENTER);
        break;
      case TYPE_EXIT:
        setStyleName(STYLE_ICON_EXIT);
        break;
      case TYPE_KICK:
        setStyleName(STYLE_ICON_KICK);
        break;
    }
    addStyleName(STYLE_ICON);
  }

  public void setUserIconType(char type) {
    switch (type) {
      case User.TYPE_NORMAL:
        setType(StatusIcon.TYPE_EMPTY);
        break;
      case User.TYPE_CHANOWNER:
        setType(StatusIcon.TYPE_CHANOWNER);
        break;
      case User.TYPE_ADMIN:
        setType(StatusIcon.TYPE_ADMIN);
        break;
      case User.TYPE_OPERATOR:
        setType(StatusIcon.TYPE_OPERATOR);
        break;
      case User.TYPE_HALFOP:
        setType(StatusIcon.TYPE_HALFOP);
        break;
      case User.TYPE_VOICE:
        setType(StatusIcon.TYPE_VOICE);
        break;
    }
    addStyleName(STYLE_ICON);
  }
}
