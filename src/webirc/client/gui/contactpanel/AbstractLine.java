package webirc.client.gui.contactpanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import webirc.client.gui.ImageButton;
import webirc.client.gui.StatusIcon;
import webirc.client.gui.menu.ContextMenu;
import webirc.client.gui.menu.MenuBar;
import webirc.client.utils.Utils;

/**
 * @author Ayzen
 * @version 1.0 08.07.2006 19:18:58
 */
public abstract class AbstractLine extends Composite {

  public static final String STYLE_NORMAL = "gwt-Line";
  public static final String STYLE_MOUSE_OVER = "gwt-Line-mouseOver";
  public static final String STYLE_PUSHED = "gwt-Line-pushed";
  public static final String STYLE_CONTENT = "gwt-Line-Content";
  public static final String STYLE_PROPS = "gwt-Line-Props";

  HorizontalPanel lineContent = new HorizontalPanel();
  FocusPanel line = new FocusPanel(lineContent);

  ImageButton optionsBtn = new ImageButton();
  boolean optionsAdded = false;
  boolean optionsFocused = false;

  StatusIcon icon = new StatusIcon();
  Label content = new Label();

  private boolean pushed = false;

  public AbstractLine() {
    initWidget(line);
    line.setWidth("100%");
    line.addMouseListener(new MouseEventsHandler());
    line.setStyleName(STYLE_NORMAL);

    optionsBtn.defineStyle("OptionsButton");
    DOM.setStyleAttribute(optionsBtn.getElement(), "position", "absolute");

    content.setWidth("100%");
    content.setWordWrap(false);
    content.setStyleName(STYLE_CONTENT);

    lineContent.setWidth("100%");
    lineContent.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    lineContent.add(icon);
    lineContent.add(content);
    lineContent.setCellHorizontalAlignment(content, HorizontalPanel.ALIGN_LEFT);
    lineContent.setCellWidth(content, "100%");

    line.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (!optionsFocused)
          lineClicked();
      }
    });
    optionsBtn.addMouseListener(new MouseListenerAdapter() {
      public void onMouseEnter(Widget sender) {
        optionsFocused = true;
      }
      public void onMouseLeave(Widget sender) {
        optionsFocused = false;
      }
    });
    optionsBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        optionsClicked();
      }
    });
  }

  public void addClickListener(ClickListener listener) {
    line.addClickListener(listener);
  }

  public void setContentText(String text) {
    content.setText(text);
  }

  public String getContentText() {
    return content.getText();
  }

  public abstract void lineClicked();

  public abstract void optionsClicked();

  private class MouseEventsHandler extends MouseListenerAdapter {
    public void onMouseDown(Widget sender, int x, int y) {
      line.addStyleName(STYLE_PUSHED);
      pushed = true;
    }

    public void onMouseUp(Widget sender, int x, int y) {
      if (pushed) {
        line.removeStyleName(STYLE_PUSHED);
        pushed = false;
      }
    }

    public void onMouseEnter(Widget sender) {
      line.addStyleName(STYLE_MOUSE_OVER);
      if (!optionsAdded) {
        lineContent.add(optionsBtn);
        int x, y;
        // Fix for IE
        if (Utils.isInternetExplorer()) {
          x = getAbsoluteLeft() - getParent().getAbsoluteLeft() + getOffsetWidth() - optionsBtn.getOffsetWidth();
          y = getAbsoluteTop() - getParent().getAbsoluteTop() + getOffsetHeight() - optionsBtn.getOffsetHeight();
        }
        else {
          x = getAbsoluteLeft() + getOffsetWidth() - optionsBtn.getOffsetWidth();
          y = getAbsoluteTop() + getOffsetHeight() - optionsBtn.getOffsetHeight();
        }
        DOM.setStyleAttribute(optionsBtn.getElement(), "left", "" + x);
        DOM.setStyleAttribute(optionsBtn.getElement(), "top", "" + y);
        optionsAdded = true;
      }
    }

    public void onMouseLeave(Widget sender) {
      pushed = false;
      if (optionsAdded) {
        lineContent.remove(optionsBtn);
        optionsAdded = false;
      }
      line.removeStyleName(STYLE_MOUSE_OVER);
      line.removeStyleName(STYLE_PUSHED);
    }
  }
  
}
