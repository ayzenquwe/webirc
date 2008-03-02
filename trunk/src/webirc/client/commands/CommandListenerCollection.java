package webirc.client.commands;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 16.07.2006 15:39:37
 */
public class CommandListenerCollection extends Vector {

  public void notifyListeners(Iterator it) {
    while (it.hasNext()) {
      IRCCommand command = (IRCCommand) it.next();
      for (Iterator mIt = iterator(); mIt.hasNext();) {
        CommandListener listener = (CommandListener) mIt.next();
        if (command instanceof NoticeCommand)
          listener.onNoticeCommand((NoticeCommand) command);
        else if (command instanceof MessageCommand)
          listener.onMessageCommand((MessageCommand) command);
        else if (command instanceof NickCommand)
          listener.onNickCommand((NickCommand) command);
        else if (command instanceof QuitCommand)
          listener.onQuitCommand((QuitCommand) command);
        else if (command instanceof JoinCommand)
          listener.onJoinCommand((JoinCommand) command);
        else if (command instanceof PartCommand)
          listener.onPartCommand((PartCommand) command);
        else if (command instanceof KickCommand)
          listener.onKickCommand((KickCommand) command);
        else if (command instanceof ModeCommand)
          listener.onModeCommand((ModeCommand) command);
        else if (command instanceof TopicCommand)
          listener.onTopicCommand((TopicCommand) command);
        else if (command instanceof ErrorCommand)
          listener.onErrorCommand((ErrorCommand) command);
        else if (command instanceof ConnectReplyCommand)
          listener.onConnectReplyCommand((ConnectReplyCommand) command);
        else if (command instanceof CommandResponseReplyCommand)
          listener.onCommandResponseReplyCommand((CommandResponseReplyCommand) command);
        else if (command instanceof ErrorReplyCommand)
          listener.onErrorReplyCommand((ErrorReplyCommand) command);
        else if (command instanceof UnknownReplyCommand)
          listener.onUnknownReplyCommand((UnknownReplyCommand) command);
        else if (command instanceof UnknownCommand)
          listener.onUnknownCommand((UnknownCommand) command);
      }
    }
  }

}
