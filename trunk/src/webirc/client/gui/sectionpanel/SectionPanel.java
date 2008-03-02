package webirc.client.gui.sectionpanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import webirc.client.MainSystem;
import webirc.client.utils.Utils;
import webirc.client.gui.ContainerPanel;

/**
 * @author Ayzen
 * @version 1.0 21.07.2006 12:32:25
 */
public class SectionPanel extends Composite {

  public static final String STYLE = "gwt-SectionPanel";
  public static final String STYLE_FIXED = "gwt-SectionPanel-MainPanel";

  private ContainerPanel container = new ContainerPanel();
  private SimplePanel fixedPanel = new SimplePanel(){};
  private VerticalPanel mainPanel = new VerticalPanel();

  private int sectionCount = 0;

  public SectionPanel() {
    DOM.setStyleAttribute(fixedPanel.getElement(), "overflow", "hidden");
    container.add(fixedPanel);
    fixedPanel.add(mainPanel);
    initWidget(container);

    container.setStyleName(STYLE);
    mainPanel.setStyleName(STYLE_FIXED);
  }

  protected void onLoad() {
    DeferredCommand.add(new Command() {
      public void execute() {
        Utils.maximize(fixedPanel);
        Utils.maximize(mainPanel);
        MainSystem.log(fixedPanel.getOffsetHeight() + "," + mainPanel.getOffsetHeight());
      }
    });
  }

  public void addSection(Section section, String height) {
    if (sectionCount > 0) {
      Divider d = new Divider();
      mainPanel.add(d);
      mainPanel.setCellHeight(d, "1%");
      mainPanel.setCellVerticalAlignment(d, VerticalPanel.ALIGN_MIDDLE);
      mainPanel.setCellHorizontalAlignment(d, VerticalPanel.ALIGN_CENTER);
    }
    mainPanel.add(section);
    mainPanel.setCellWidth(section, "100%");
    mainPanel.setCellHeight(section, height);
    mainPanel.setCellVerticalAlignment(section, VerticalPanel.ALIGN_TOP);
    mainPanel.setCellHorizontalAlignment(section, VerticalPanel.ALIGN_CENTER);
    sectionCount++;
  }

}
