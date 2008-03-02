package webirc.client.gui.messagepanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import webirc.client.CTCPHandler;
import webirc.client.MainSystem;
import webirc.client.WebIRC;
import webirc.client.commands.*;
import webirc.client.gui.StatusIcon;

/**
 * @author Ayzen
 * @version 1.0 06.07.2006 20:15:02
 */
public class MessageLine extends Widget {

  public static final String STYLE_MESSAGE_DIV = "messageLine";
  public static final String STYLE_MESSAGE_TEXT = "messageText";
  public static final String STYLE_MESSAGE_AUTHOR = "messageAuthor";
  public static final String STYLE_MESSAGE_AUTHOR_USER = "usersMessage";
  public static final String STYLE_MESSAGE_TEXT_CONNECTREPLY = "messageTextConnectReply";
  public static final String STYLE_MESSAGE_AUTHOR_CONNECTREPLY = "messageAuthorConnectReply";
  public static final String STYLE_MESSAGE_TEXT_UNKNOWNREPLY = "messageTextUnknownReply";
  public static final String STYLE_MESSAGE_AUTHOR_UNKNOWNREPLY = "messageAuthorUnknownReply";
  public static final String STYLE_MESSAGE_TEXT_COMMANDRESPONSEREPLY = "messageTextCommandResponseReply";
  public static final String STYLE_MESSAGE_AUTHOR_COMMANDRESPONSEREPLY = "messageAuthorCommandResponseReply";
  public static final String STYLE_MESSAGE_TEXT_ERRORREPLY = "messageTextErrorReply";
  public static final String STYLE_MESSAGE_AUTHOR_ERRORREPLY = "messageAuthorErrorReply";
  public static final String STYLE_MESSAGE_TEXT_UNKNOWNCOMMAND = "messageTextUnknownCommand";
  public static final String STYLE_MESSAGE_AUTHOR_UNKNOWNCOMMAND = "messageAuthorUnknownCommand";
  public static final String STYLE_MESSAGE_TEXT_NOTICE = "messageTextNotice";
  public static final String STYLE_MESSAGE_AUTHOR_NOTICE = "messageAuthorNotice";
  public static final String STYLE_MESSAGE_MODE = "messageMode";
  public static final String STYLE_MESSAGE_MODE_BAN = "messageMode-Ban";
  public static final String STYLE_MESSAGE_MODE_UNBAN = "messageMode-Unban";
  public static final String STYLE_MESSAGE_MODE_ADDUSERTYPE = "messageMode-addUserType";
  public static final String STYLE_MESSAGE_MODE_REMOVEUSERTYPE = "messageMode-removeUserType";
  public static final String STYLE_MESSAGE_ENTER = "message-User-Enter";
  public static final String STYLE_MESSAGE_EXIT = "message-User-Exit";
  public static final String STYLE_MESSAGE_KICK = "message-User-Kick";
  public static final String STYLE_MESSAGE_EVENT = "message-Event";
  public static final String STYLE_MESSAGE_CTCP = "message-CTCP";
  public static final String STYLE_MESSAGE_TOPIC = "message-Topic";

  private Element authorSpan;
  private Element messageSpan;

  private StatusIcon icon = new StatusIcon();

  public MessageLine() {
    setElement(DOM.createDiv());
    authorSpan = DOM.createSpan();
    DOM.appendChild(getElement(), authorSpan);
    messageSpan = DOM.createSpan();
    DOM.appendChild(getElement(), messageSpan);
    setStyleName(STYLE_MESSAGE_DIV);
    setAuthorStyle(STYLE_MESSAGE_AUTHOR);
    setMessageStyle(STYLE_MESSAGE_TEXT);
  }

  public MessageLine(String message) {
    this();
    setMessageText(message);
  }

  public MessageLine(IRCCommand command) {
    this();
    if (command instanceof NoticeCommand) {
      NoticeCommand notice = (NoticeCommand) command;
      icon.setType(StatusIcon.TYPE_NOTICE);
      // Some notices haven't got sender information
      if (notice.getSender() != null) {
        addAuthorStyle(STYLE_MESSAGE_AUTHOR_NOTICE);
        setMessageAuthor(notice.getSender().getNickname());
      }
      if (notice.getType() == MessageCommand.TYPE_CTCP) {
        String text = notice.getText();
        if (text.length() > notice.getCtcpCommand().length() + 1)
          text = text.substring(notice.getCtcpCommand().length() + 1);
        text = notice.getSender().getNickname() + ' ' +
               WebIRC.eventMessages.ctcpResponse(notice.getCtcpCommand(), text);
        setMessageText(text);
      }
      else
        setMessageText(notice.getText());
      addMessageStyle(STYLE_MESSAGE_TEXT_NOTICE);
    }
    // Message
    else if (command instanceof MessageCommand) {
      MessageCommand msg = (MessageCommand) command;
      if (msg.getType() == MessageCommand.TYPE_MESSAGE) {
        icon.setUserIconType(msg.getSender().getType());
        if (msg.getSender().equals(MainSystem.getInstance().getUser()))
          addAuthorStyle(STYLE_MESSAGE_AUTHOR_USER);
        setMessageAuthor(command.getSender().getNickname());
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_EVENT) {
        addMessageStyle(STYLE_MESSAGE_EVENT);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_ADDUSERMODE) {
        icon.setType(StatusIcon.TYPE_SYSTEM);
        addMessageStyle(STYLE_MESSAGE_MODE_ADDUSERTYPE);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_REMOVEUSERMODE) {
        icon.setType(StatusIcon.TYPE_SYSTEM);
        addMessageStyle(STYLE_MESSAGE_MODE_REMOVEUSERTYPE);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_BAN) {
        icon.setType(StatusIcon.TYPE_BAN);
        addMessageStyle(STYLE_MESSAGE_MODE_BAN);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_UNBAN) {
        icon.setType(StatusIcon.TYPE_UNBAN);
        addMessageStyle(STYLE_MESSAGE_MODE_UNBAN);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_MODE) {
        icon.setType(StatusIcon.TYPE_SYSTEM);
        addMessageStyle(STYLE_MESSAGE_MODE);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_ENTER) {
        icon.setType(StatusIcon.TYPE_ENTER);
        addMessageStyle(STYLE_MESSAGE_ENTER);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_EXIT) {
        icon.setType(StatusIcon.TYPE_EXIT);
        addMessageStyle(STYLE_MESSAGE_EXIT);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_KICK) {
        icon.setType(StatusIcon.TYPE_KICK);
        addMessageStyle(STYLE_MESSAGE_KICK);
        setMessageText(msg.getText());
      }
      else if (msg.getType() == MessageCommand.TYPE_CTCP) {
        addMessageStyle(STYLE_MESSAGE_CTCP);
        setMessageText(CTCPHandler.getCTCPMessage(msg));
      }
      else if (msg.getType() == MessageCommand.TYPE_TOPIC) {
        icon.setType(StatusIcon.TYPE_INFO);
        addMessageStyle(STYLE_MESSAGE_TOPIC);
        setMessageText(msg.getText());
      }
      else {
        setMessageText(msg.getText());
      }
    }
    // Else commands
    else if (command instanceof ModeCommand) {
      icon.setType(StatusIcon.TYPE_SYSTEM);
      setMessageText(command.getFullMessage());
    }
    else if (command instanceof ReplyCommand) {
      ReplyCommand reply = (ReplyCommand) command;
      setMessageText(reply.getReplyMessage());
      if (reply instanceof ConnectReplyCommand) {
        icon.setType(StatusIcon.TYPE_SYSTEM);
        icon.setTitle(WebIRC.mainMessages.connectReply() + reply.getReplyType());
        addMessageStyle(STYLE_MESSAGE_TEXT_CONNECTREPLY);
        addAuthorStyle(STYLE_MESSAGE_AUTHOR_CONNECTREPLY);
      }
      else if (reply instanceof CommandResponseReplyCommand) {
        icon.setType(StatusIcon.TYPE_INFO);
        icon.setTitle(WebIRC.mainMessages.commandResponseReply() + reply.getReplyType());
        addMessageStyle(STYLE_MESSAGE_TEXT_COMMANDRESPONSEREPLY);
        addAuthorStyle(STYLE_MESSAGE_AUTHOR_COMMANDRESPONSEREPLY);
      }
      else if (reply instanceof ErrorReplyCommand) {
        icon.setType(StatusIcon.TYPE_ERROR);
        icon.setTitle(WebIRC.mainMessages.errorReply() + reply.getReplyType());
        addMessageStyle(STYLE_MESSAGE_TEXT_ERRORREPLY);
        addAuthorStyle(STYLE_MESSAGE_AUTHOR_ERRORREPLY);
      }
      else if (reply instanceof UnknownReplyCommand) {
        icon.setType(StatusIcon.TYPE_UNKNOWN);
        icon.setTitle(WebIRC.mainMessages.unknownReply() + reply.getReplyType());
        addMessageStyle(STYLE_MESSAGE_TEXT_UNKNOWNREPLY);
        addAuthorStyle(STYLE_MESSAGE_AUTHOR_UNKNOWNREPLY);
      }
    }
    else if (command instanceof UnknownCommand) {
      icon.setType(StatusIcon.TYPE_UNKNOWN);
      icon.setTitle(WebIRC.mainMessages.unknownCommand() + ' ' + ((UnknownCommand) command).getName());
      setMessageText(command.getFullMessage());
      addMessageStyle(STYLE_MESSAGE_TEXT_UNKNOWNCOMMAND);
      addAuthorStyle(STYLE_MESSAGE_AUTHOR_UNKNOWNCOMMAND);
    }
    else
      setMessageText(command.getFullMessage());
  }

  public void setMessageAuthor(String author) {
    DOM.setInnerText(authorSpan, author + ": ");
  }

  public void setMessageText(String text) {
    DOM.setInnerHTML(messageSpan, MessageParser.parse(text));
  }

  public void setMessageStyle(String styleName) {
    DOM.setAttribute(messageSpan, "className", styleName);
  }

  public void setAuthorStyle(String styleName) {
    DOM.setAttribute(authorSpan, "className", styleName);
  }

  public void addMessageStyle(String styleName) {
    setStyleName(messageSpan, styleName, true);
  }

  public void addAuthorStyle(String styleName) {
    setStyleName(authorSpan, styleName, true);
  }

  public Widget getIcon() {
    return icon;
  }
}
