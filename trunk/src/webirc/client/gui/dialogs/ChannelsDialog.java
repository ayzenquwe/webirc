package webirc.client.gui.dialogs;

import com.google.gwt.user.client.Window;
import webirc.client.Channel;
import webirc.client.MainSystem;
import webirc.client.WebIRC;
import webirc.client.commands.JoinCommand;
import webirc.client.gui.dialogs.panels.ChannelsPanel;

/**
 * @author Ayzen
 * @version 1.0 16.09.2006 16:51:40
 */
public class ChannelsDialog extends AbstractDialog {

  ChannelsPanel panel = new ChannelsPanel();

  public ChannelsDialog() {
    super(WebIRC.dialogMessages.listOfChannels());
    // Changing names of buttons
    firstButton.setText(WebIRC.dialogMessages.enter());
    secondButton.setText(WebIRC.dialogMessages.close());
    // Calculating initial size
    int width = Window.getClientWidth() / 2;
    int height = Window.getClientHeight() - 100;
    panel.setSize(width + "px", height + "px");
    add(panel);
  }

  public void addChannelInfo(String channelName, int users, String topic) {
    panel.addChannelInfo(channelName, users, topic);
  }


  public void fireOnFirstButtonPressed() {
    String selectedChannel = panel.getSelectedChannel();
    if (selectedChannel != null) {
      Channel channel = new Channel(selectedChannel);
      MainSystem.getInstance().sendCommand(new JoinCommand(channel));
    }
    super.fireOnFirstButtonPressed();
  }

  public void fireOnSecondButtonPressed() {
    hide();
    super.fireOnSecondButtonPressed();
  }
}
