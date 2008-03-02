package webirc.client.gui.messagepanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Ayzen
 * @version 1.0 30.08.2006 21:36:12
 */
public class HistoryLines extends CellPanel {

  private static final String STYLE_TIMESTAMP = "gwt-MessagePanel-Timestamp-Container";
  private static final String STYLE_LINE = "gwt-MessagePanel-Line-Container";

  public HistoryLines() {
    DOM.setAttribute(getTable(), "cellSpacing", "0");
    DOM.setAttribute(getTable(), "cellPadding", "0");
  }

  public void add(Widget timeStamp, MessageLine line) {
    Widget icon = line.getIcon(); 

    Element tr = DOM.createTR();
    Element tdTime = DOM.createTD();
    Element tdIcon = DOM.createTD();
    Element tdLine = DOM.createTD();

    DOM.setAttribute(tdTime, "className", STYLE_TIMESTAMP);
    DOM.setAttribute(tdLine, "className", STYLE_LINE);

    DOM.appendChild(getBody(), tr);
    DOM.appendChild(tr, tdTime);
    DOM.appendChild(tr, tdIcon);
    DOM.appendChild(tr, tdLine);

    super.add(timeStamp, tdTime);
    super.add(icon, tdIcon);
    super.add(line, tdLine);

    setCellHorizontalAlignment(timeStamp, HasHorizontalAlignment.ALIGN_CENTER);
    setCellVerticalAlignment(timeStamp, HasVerticalAlignment.ALIGN_TOP);
    setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_CENTER);
    setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_TOP);
    setCellHorizontalAlignment(line, HasHorizontalAlignment.ALIGN_LEFT);
  }
}
