package webirc.client.gui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Ayzen
 * @version 1.0 15.09.2006 22:34:33
 */
public class LineLabel extends Label {

  public LineLabel() {
    super();
    setElement(DOM.createSpan());
  }

  public LineLabel(String text) {
    this();
    setText(text);
  }

  public LineLabel(String text, boolean wordWrap) {
    this(text);
    setWordWrap(wordWrap);
  }

}
