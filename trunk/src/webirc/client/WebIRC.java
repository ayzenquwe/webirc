package webirc.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import webirc.client.gui.dialogs.MessageBox;
import webirc.client.i18n.DialogMessages;
import webirc.client.i18n.ErrorMessages;
import webirc.client.i18n.EventMessages;
import webirc.client.i18n.MainMessages;
import webirc.client.utils.Utils;

import java.util.HashMap;

public class WebIRC implements EntryPoint {

  public static final String BUILD = "@BUILD@";
  public static final String VERSION = "0.4.5 Alpha";
  public static final String HOME = "http://l2w2new.wnet.ua:8080/WebIRC/WebIRC.html";
  public static final String DEFAULT_LOCALE = "en";

  public static boolean DEBUG = false;

  public static final DialogMessages dialogMessages = (DialogMessages) GWT.create(DialogMessages.class);
  public static final MainMessages mainMessages = (MainMessages) GWT.create(MainMessages.class);
  public static final ErrorMessages errorMessages = (ErrorMessages) GWT.create(ErrorMessages.class);
  public static final EventMessages eventMessages = (EventMessages) GWT.create(EventMessages.class);

  public void onModuleLoad() {
    Window.setTitle("WebIRC " + VERSION);
    Window.enableScrolling(false);
    Window.setMargin("0");

    // Set a handler for uncaught exceptions if it is not a shell
    if (GWT.isScript())
      GWT.setUncaughtExceptionHandler(new ExceptionHandler());

    // Start MainSystem class
    MainSystem.getInstance(initParams(Utils.getSearch()));
  }

  /**
   * Initialization of input parameters.
   *
   * @param params input parameters in URL
   * @return HashMap with all input parameters
   */
  private HashMap initParams(String params) {
    if (params == null || params.trim().length() == 0)
      return null;

    HashMap result = new HashMap();
    String[] allParams = params.substring(1).split("[&]");
    for (int i = 0; i < allParams.length; i++) {
      int index = allParams[i].indexOf('=');
      if (index != -1)
        result.put(allParams[i].substring(0, index), allParams[i].substring(index + 1));
    }
    return result;
  }

  private class ExceptionHandler implements GWT.UncaughtExceptionHandler {
    public void onUncaughtException(Throwable e) {
      MessageBox errorBox = new MessageBox("Exception", e.getMessage(), MessageBox.TYPE_ERROR);
      errorBox.show();
    }
  }

}