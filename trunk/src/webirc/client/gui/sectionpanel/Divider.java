package webirc.client.gui.sectionpanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;
import webirc.client.MainSystem;

/**
 * @author Ayzen
 * @version 1.0 14.08.2006 10:33:53
 */
public class Divider extends HTML {

  private static final String STYLE = "gwt-Divider";

  private boolean resizing = false;
  private int offsetY = 0;
  private int resizeY = 0;

  Section upper;
  int upperHeight;
  Section downer;
  int downerHeight;
  Widget parentPanel;
  int panelHeight;

  public Divider() {
    super();
    setStyleName(STYLE);
    setWidth("90%");

    addMouseListener(new MouseListenerAdapter(){
      public void onMouseDown(Widget sender, int x, int y) {
        if (sender == Divider.this) {
          DOM.setCapture(getElement());
          resizing = true;
          resizeY = y;
          offsetY = y;

          IndexedPanel parent = (IndexedPanel) getParent();
          int position = parent.getWidgetIndex(sender);
          upper = (Section) parent.getWidget(position - 1);
          upperHeight = upper.getOffsetHeight();
          downer = (Section) parent.getWidget(position + 1);
          downerHeight = downer.getOffsetHeight();
          parentPanel = upper.getParent();
          panelHeight = parentPanel.getOffsetHeight();
        }
      }

      public void onMouseMove(Widget sender, int x, int y) {
        if (resizing) {
          int delta = resizeY - y;
          resizeY = delta + offsetY;

          if (delta == 0 || (upperHeight - delta > 20) &&
              (downerHeight + delta > 20)) {
            if (delta > 0) {
              upper.setHeight((upperHeight - delta) + "");
              downer.setHeight((downerHeight + delta) + "");
            }
            else {
              downer.setHeight((downerHeight + delta) + "");
              upper.setHeight((upperHeight - delta) + "");
            }
          }
          else
            return;
          // Extra parent panel correction
          int newPanelHeight = parentPanel.getOffsetHeight();
          if (panelHeight != newPanelHeight) {
            upper.setHeight((upperHeight - delta - (newPanelHeight - panelHeight) + ""));
            parentPanel.setHeight("" + panelHeight);
          }  
        }
      }

      public void onMouseUp(Widget sender, int x, int y) {
        DOM.releaseCapture(getElement());
        resizing = false;
      }
    });
  }

}
