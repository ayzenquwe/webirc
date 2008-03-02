package webirc.client.gui.tabbedpanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import webirc.client.MainSystem;
import webirc.client.gui.ImageButton;
import webirc.client.gui.decorators.NoSuchDecoratorException;
import webirc.client.gui.decorators.TabDecorator;

/**
 * @author Ayzen
 * @version 1.0 15.07.2006 23:22:48
 */
public class TabbedBarItem extends FocusPanel implements HasText {

  private static final String STYLE = "tab";
  private static final String STYLE_TITLE = "tab-title";
  private static final String STYLE_TITLE_SELECTED = "tab-title-selected";
  private static final String STYLE_TITLE_HOVER = "tab-title-hover";
  private static final String STYLE_TITLE_BLINK = "tab-title-blink";
  private static final String STYLE_TITLE_CHANGED = "tab-title-changed";

  private TabDecorator tabDecorator;
  private HorizontalPanel contentPanel = new HorizontalPanel();

  private boolean selected = false;
  private boolean notified = false;
  private boolean changed = false;

  private Label titleLabel;
  private ImageButton closeButton = new ImageButton();

  private CloseListenerCollection closeListeners;

  public TabbedBarItem() {
    super();

    boolean decoratorAttached = false;
    try {
      tabDecorator = new TabDecorator();
      decoratorAttached = true;
    }
    catch (NoSuchDecoratorException e) {
      MainSystem.showError("Couldn't found tab decorator with id - '" + e.getDecoratorId() + "'");
    }
    if (decoratorAttached) {
      tabDecorator.getContentPanel().add(contentPanel);
      add(tabDecorator);
    }
    else
      add(contentPanel);

    contentPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    closeButton.defineStyle("CloseButton");
    DOM.setStyleAttribute(contentPanel.getElement(), "whiteSpace", "nowrap");
    addMouseListener(new MouseEventsHandler());

    // Handling pressing close button
    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        closeListeners.fireClose(TabbedBarItem.this);
      }
    });

    setStyleName(STYLE);
  }

  public TabbedBarItem(String title) {
    this();

    titleLabel = new HTML(title);
    titleLabel.setWordWrap(false);
    titleLabel.setStyleName(STYLE_TITLE);
    contentPanel.add(titleLabel);
    contentPanel.add(new HTML("&nbsp;&nbsp;"));
    contentPanel.add(closeButton);
  }

  public void addCloseListener(CloseListener listener) {
    if (closeListeners == null)
      closeListeners = new CloseListenerCollection();
    closeListeners.add(listener);
  }

  public void removeCloseListener(CloseListener listener) {
    closeListeners.remove(listener);
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    if (selected) {
      if (changed)
        tabDecorator.removeStyleType(TabDecorator.TYPE_CHANGED);
      if (notified)
        tabDecorator.removeStyleType(TabDecorator.TYPE_BLINK);
      tabDecorator.removeStyleType(TabDecorator.TYPE_HOVER);
      tabDecorator.addStyleType(TabDecorator.TYPE_SELECTED);
      titleLabel.setStyleName(STYLE_TITLE_SELECTED);
      notified = false;
      changed = false;
    }
    else {
      tabDecorator.removeStyleType(TabDecorator.TYPE_SELECTED);
      titleLabel.setStyleName(STYLE_TITLE);
    }
    this.selected = selected;
  }

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    if (notified) {
      titleLabel.addStyleName(STYLE_TITLE_BLINK);
      tabDecorator.addStyleType(TabDecorator.TYPE_BLINK);
    }
    else {
      titleLabel.removeStyleName(STYLE_TITLE_BLINK);
      tabDecorator.removeStyleType(TabDecorator.TYPE_BLINK);
    }
    this.notified = notified;
  }

  public boolean isChanged() {
    return changed;
  }

  public void setChanged(boolean changed) {
    if (changed) {
      titleLabel.addStyleName(STYLE_TITLE_CHANGED);
      tabDecorator.addStyleType(TabDecorator.TYPE_CHANGED);
    }
    else {
      titleLabel.removeStyleName(STYLE_TITLE_CHANGED);
      tabDecorator.removeStyleType(TabDecorator.TYPE_CHANGED);
    }
    this.changed = changed;
  }

  public String getText() {
    return titleLabel.getText();
  }

  public void setText(String text) {
    titleLabel.setText(text);
  }

  private class MouseEventsHandler extends MouseListenerAdapter {

    public void onMouseEnter(Widget sender) {
      if (notified)
        tabDecorator.removeStyleType(TabDecorator.TYPE_BLINK);
      if (!selected) {
        titleLabel.addStyleName(STYLE_TITLE_HOVER);
        tabDecorator.addStyleType(TabDecorator.TYPE_HOVER);
      }
    }

    public void onMouseLeave(Widget sender) {
      tabDecorator.removeStyleType(TabDecorator.TYPE_HOVER);
      titleLabel.removeStyleName(STYLE_TITLE_HOVER);
      if (notified)
        tabDecorator.addStyleType(TabDecorator.TYPE_BLINK);
    }
  }
}
