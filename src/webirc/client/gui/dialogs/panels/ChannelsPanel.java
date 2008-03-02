package webirc.client.gui.dialogs.panels;

import com.google.gwt.user.client.ui.*;
import webirc.client.WebIRC;
import webirc.client.gui.ContainerPanel;

/**
 * @author Ayzen
 * @version 1.0 16.09.2006 18:22:11
 */
public class ChannelsPanel extends Composite {

  public static final String STYLE_HEADER = "gwt-ChannelsPanel-Header";
  public static final String STYLE_HEADERLABEL = "gwt-ChannelsPanel-Header-Label";
  public static final String STYLE_CONTENT = "gwt-ChannelsPanel-Content";
  public static final String STYLE_SELECTED = "gwt-ChannelsPanel-Selected-Row";

  private final int CHANNEL_NAME = 0;
  private final int CHANNEL_USERS = 1;
  private final int CHANNEL_TOPIC = 2;

  HorizontalPanel header = new HorizontalPanel();
  FlexTable channelsTable = new FlexTable();
  VerticalPanel container = new VerticalPanel();
  ScrollPanel scrollPanel = new ScrollPanel(container);
  ContainerPanel scrollContainer = new ContainerPanel(scrollPanel);

  VerticalPanel mainPanel = new VerticalPanel();
  ContainerPanel mainContainer = new ContainerPanel(mainPanel);

  private int selectedRow = -1;

  public ChannelsPanel() {
    initWidget(mainContainer);
    scrollPanel.setSize("100%", "100%");
    mainPanel.setSize("100%", "100%");
    mainPanel.add(header);
    mainPanel.add(scrollContainer);
    mainPanel.setCellHeight(scrollContainer, "100%");

    header.setSize("100%", "100%");
    header.setStyleName(STYLE_HEADER);
    header.setSpacing(1);
    Label lblName = new Label(WebIRC.dialogMessages.channelName());
    Label lblUsers = new Label(WebIRC.dialogMessages.users());
    Label lblTopic = new Label(WebIRC.dialogMessages.channelTopic());
    lblName.setStyleName(STYLE_HEADERLABEL);
    lblUsers.setStyleName(STYLE_HEADERLABEL);
    lblTopic.setStyleName(STYLE_HEADERLABEL);
    header.add(lblName);
    header.add(lblUsers);
    header.add(lblTopic);
    header.setCellWidth(lblName, "35%");
    header.setCellWidth(lblUsers, "10%");
    header.setCellWidth(lblTopic, "45%");

    channelsTable.setWidth("100%");
    channelsTable.getCellFormatter().setWidth(0, CHANNEL_NAME, "35%");
    channelsTable.getCellFormatter().setWidth(0, CHANNEL_USERS, "10%");
    channelsTable.getCellFormatter().setWidth(0, CHANNEL_TOPIC, "45%");
    channelsTable.addTableListener(new TableEventHandler());

    scrollContainer.setStyleName(STYLE_CONTENT);
    container.setSize("100%", "100%");
    container.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
    container.add(channelsTable);
    Label empty = new Label();
    container.add(empty);
    container.setCellHeight(empty, "100%");
  }

  public void addChannelInfo(String channelName, int users, String topic) {
    int row = channelsTable.getRowCount();
    channelsTable.setText(row, CHANNEL_NAME, channelName);
    channelsTable.setText(row, CHANNEL_USERS, "" + users);
    channelsTable.setText(row, CHANNEL_TOPIC, topic);
  }

  public String getSelectedChannel() {
    String channel;
    if (selectedRow != -1)
      channel = channelsTable.getText(selectedRow, CHANNEL_NAME);
    else
      channel = null;
    return channel; 
  }

  public void setSize(String width, String height) {
    scrollPanel.setSize("10px", "10px");
    scrollContainer.setSize("20px", "20px");
    scrollPanel.setSize(width, height);
  }

  private class TableEventHandler implements TableListener {
    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
      if (selectedRow != -1)
        channelsTable.getRowFormatter().removeStyleName(selectedRow, STYLE_SELECTED);
      channelsTable.getRowFormatter().addStyleName(row, STYLE_SELECTED);
      selectedRow = row;
    }
  }

}
