package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 12.07.2006 23:02:29
 */
public class CommandAdapter implements CommandListener {
  public void onNickCommand(NickCommand command) {}
  public void onMessageCommand(MessageCommand command) {}
  public void onNoticeCommand(NoticeCommand command) {}
  public void onQuitCommand(QuitCommand command) {}
  public void onJoinCommand(JoinCommand command) {}
  public void onPartCommand(PartCommand command) {}
  public void onKickCommand(KickCommand command) {}
  public void onModeCommand(ModeCommand command) {}
  public void onTopicCommand(TopicCommand command) {}
  public void onErrorCommand(ErrorCommand command) {}
  public void onUnknownCommand(UnknownCommand command) {}
  public void onConnectReplyCommand(ConnectReplyCommand command) {}
  public void onCommandResponseReplyCommand(CommandResponseReplyCommand command) {}
  public void onErrorReplyCommand(ErrorReplyCommand command) {}
  public void onUnknownReplyCommand(UnknownReplyCommand command) {}
}
