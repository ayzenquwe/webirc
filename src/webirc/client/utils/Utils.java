package webirc.client.utils;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import webirc.client.MainSystem;
import webirc.client.WebIRC;

/**
 * @author Ayzen
 * @version 1.0 24.09.2006 18:40:30
 */
public class Utils {

  public final static String INTERNET_EXPLORER_USER_AGENT = "MSIE";
  public final static String FIREFOX_USER_AGENT = "Firefox";
  public final static String OPERA_USER_AGENT = "Opera";
  public final static String OPERA8_USER_AGENT = "Opera/8";
  public final static String OPERA9_USER_AGENT = "Opera/9";

  /**
   * Gets value from xml node for current locale.
   *
   * @param node some node in xml, that contains tags with locales names
   * @return value for current locale
   */
  public static String getValueForLocale(Node node) {
    String locale = MainSystem.getLocale();
    String result = null;
    String defaultResult = null;

    NodeList nodes = node.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node child = nodes.item(i);
      String name = child.getNodeName();
      if (name.equalsIgnoreCase(WebIRC.DEFAULT_LOCALE))
        defaultResult = child.getChildNodes().item(0).getNodeValue();
      else if (locale != null && name.equalsIgnoreCase(locale))
        result = child.getChildNodes().item(0).getNodeValue();
    }

    if (result != null)
      return result;
    else if (defaultResult != null)
      return defaultResult;
    else
      return node.getChildNodes().item(0).getNodeValue();
  }

  /**
   * Check if given entity is channel.
   *
   * @param entity some name
   * @return true, if entity is channel
   */
  public static boolean isChannel(String entity) {
    return entity.charAt(0) == '#';
  }

  public static native String browserInfo() /*-{
    return navigator.appName + " " + navigator.appVersion + " on " + navigator.platform;
  }-*/;

  public static native String userAgent() /*-{
    return $wnd.navigator.userAgent;
  }-*/;

  public static void maximizeWidth(Widget widget) {
    if (widget == null || widget.getParent() == null || !widget.isAttached())
      return;
    widget.setWidth("" + getFreeWidth(widget));
  }

  public static void maximizeHeight(Widget widget) {
    if (widget == null || widget.getParent() == null || !widget.isAttached())
      return;
    widget.setHeight("" + getFreeHeight(widget));
  }

  public static void maximize(Widget widget) {
    if (widget == null || widget.getParent() == null || !widget.isAttached())
      return;
    widget.setSize("" + getFreeWidth(widget), "" + getFreeHeight(widget));
  }

  public static void maximizeA(Widget widget) {
    if (widget == null || !widget.isAttached())
      return;

    Element parent = DOM.getParent(widget.getElement());
    if (parent == null)
      return;

    widget.setSize("" + getClientWidthImpl(parent), "" + getClientHeightImpl(parent));
  }

  public static int getFreeWidth(Widget widget) {
    return getClientWidthImpl(widget.getParent().getElement());
  }

  public static int getFreeHeight(Widget widget) {
    return getClientHeightImpl(widget.getParent().getElement());
  }

  public static int getClientWidth(Widget widget) {
    return getClientWidthImpl(widget.getElement());
  }

  public static int getClientHeight(Widget widget) {
    return getClientHeightImpl(widget.getElement());
  }

  public static native int getClientWidthImpl(Element element) /*-{
    return element.clientWidth;
  }-*/;

  public static native int getClientHeightImpl(Element element) /*-{
    return element.clientHeight;
  }-*/;

  public static native String getSearch() /*-{
    return $wnd.location.search;
  }-*/;

  public static native String unescape(String text) /*-{
    return unescape(text);
  }-*/;

  public static boolean isInternetExplorer() {
    return userAgent().indexOf(INTERNET_EXPLORER_USER_AGENT) != -1;
  }

  public static boolean isFireFox() {
    return userAgent().indexOf(FIREFOX_USER_AGENT) != -1;
  }

  public static boolean isOpera() {
    return userAgent().indexOf(OPERA_USER_AGENT) != -1;
  }

  public static boolean isOpera8() {
    return userAgent().indexOf(OPERA8_USER_AGENT) != -1;
  }

  public static boolean isOpera9() {
    return userAgent().indexOf(OPERA9_USER_AGENT) != -1;
  }

}
