package webirc.client.gui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import webirc.client.ExCommand;
import webirc.client.MainSystem;
import webirc.client.WebIRC;
import webirc.client.commands.JoinCommand;
import webirc.client.commands.ListCommand;

/**
 * @author Ayzen
 * @version 1.0 03.01.2007 16:30:59
 */
public class ActionPanel extends Composite {

  private ImageButton enterBtn;

  public ActionPanel() {
    HorizontalPanel actions = new HorizontalPanel();

    enterBtn = new ImageButton(WebIRC.mainMessages.enter());
    enterBtn.defineStyle("EnterButton");
    enterBtn.addClickListener(new EnterChannelAction());

    ImageButton listBtn;
    listBtn = new ImageButton(WebIRC.mainMessages.list());
    listBtn.defineStyle("ListButton");
    listBtn.addClickListener(new ListChannelsAction());

    actions.setWidth("100%");
    actions.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    actions.add(enterBtn);
    actions.add(new ActionDivider());
    actions.add(listBtn);
    actions.setCellWidth(enterBtn, "50%");
    actions.setCellWidth(listBtn, "50%");

    initWidget(actions);
  }

  private class ActionDivider extends Widget {

    public static final String STYLE = "gwt-Action-Divider";

    public ActionDivider() {
      setElement(DOM.createDiv());
      setStyleName(STYLE);
    }

  }

  private class EnterChannelAction implements ClickListener {
    public void onClick(Widget sender) {
      int x = enterBtn.getAbsoluteLeft() + enterBtn.getOffsetWidth() / 2;
      int y = enterBtn.getAbsoluteTop() + enterBtn.getOffsetHeight() / 2;

      Popups.showInput(x, y, WebIRC.mainMessages.channelName(), new ExCommand() {
        public void execute(Object response) {
          String channel = (String) response;
          if (channel != null && channel.length() != 0) {
            JoinCommand joinCmd = new JoinCommand(channel);
            MainSystem.getInstance().sendCommand(joinCmd);
          }
        }
      });
    }
  }

  private class ListChannelsAction implements ClickListener {
    public void onClick(Widget sender) {
      MainSystem.getInstance().sendCommand(new ListCommand());
    }
  }

}