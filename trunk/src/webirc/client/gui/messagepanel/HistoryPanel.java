package webirc.client.gui.messagepanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import webirc.client.commands.IRCCommand;

import java.util.Date;

/**
 * @author Ayzen
 * @version 1.0 06.07.2006 19:53:54
 */
public class HistoryPanel extends Composite {

  private HistoryLines rootPane = new HistoryLines();
  private ScrollPanel scrollPane = new ScrollPanel(rootPane);

  public HistoryPanel() {
    initWidget(scrollPane);
  }

  public void addMessage(String message) {
    addMessage(new MessageLine(message));
  }

  public void addMessage(IRCCommand message) {
    addMessage(new MessageLine(message));
  }

  private void addMessage(MessageLine line) {
    Date date = new Date();
    String hours = validateTime(date.getHours());
    String minutes = validateTime(date.getMinutes());
    String seconds = validateTime(date.getSeconds());
    Label timeStamp = new Label(hours + ":" + minutes + ":" + seconds);
    rootPane.add(timeStamp, line);
  }

  public ComplexPanel getRootPane() {
    return rootPane;
  }

  public void scrollToBottom() {
    scrollToBottomImpl(scrollPane.getElement());
  }

  private String validateTime(int number) {
    return number >= 10 ? "" + number : "0" + number;
  }

  private native void scrollToBottomImpl(Element element) /*-{
    element.scrollTop = element.scrollHeight;     
  }-*/;
}
