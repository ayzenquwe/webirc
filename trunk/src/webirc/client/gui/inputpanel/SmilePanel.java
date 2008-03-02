package webirc.client.gui.inputpanel;

import com.google.gwt.user.client.ui.*;
import webirc.client.gui.DecoratedPopup;
import webirc.client.utils.Smile;
import webirc.client.utils.Smileys;

import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 28.09.2006 21:42:54
 */
public class SmilePanel extends DecoratedPopup {

  private TextArea textArea;
  private Vector smileys;
  private int cursorPos;

  private static SmilePanel instance;

  public static SmilePanel getInstance() {
    if (instance == null)
      instance = new SmilePanel();
    return instance;
  }

  private SmilePanel() {
    super(true);
    init();
  }

  public void setCursorPos(int cursorPos) {
    this.cursorPos = cursorPos;
  }

  public void setTextArea(TextArea textArea) {
    this.textArea = textArea;
  }

  public void init() {
    smileys = Smileys.getInstance().getSmiles();
    long cols = Math.round(Math.sqrt(smileys.size()));
    
    FlexTable smileysTable = new FlexTable();

    smileysTable.addTableListener(new TableListener() {
      public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        hide();
        FlexTable table = (FlexTable) sender;
        int index = table.getCellCount(0) * row + cell;
        if (index < smileys.size()) {
          Smile smile = (Smile) smileys.get(index);
          StringBuffer text = new StringBuffer(textArea.getText());
          String smileStr = smile.getPaste();
          if (cursorPos != 0 && text.charAt(cursorPos - 1) != ' ')
            smileStr = ' ' + smileStr;
          if (cursorPos < text.length() && text.charAt(cursorPos) != ' ')
            smileStr += ' ';
          
          text.insert(cursorPos, smileStr);
          textArea.setText(text.toString());
          textArea.setFocus(true);
          textArea.setCursorPos(cursorPos + smileStr.length());
        }
      }
    });

    for (int i = 0, row = 0, col = 0; i < smileys.size(); i++, col++) {
      if (col == cols) {
        row++;
        col = 0;
      }
      Smile smile = (Smile) smileys.get(i);
      smileysTable.setHTML(row, col, "<img src='" + smile.getPath() + "' title='" + smile.getName() + "'>");
      smileysTable.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER,
                                                   HasVerticalAlignment.ALIGN_MIDDLE);
    }
    
    add(smileysTable);
  }
}
