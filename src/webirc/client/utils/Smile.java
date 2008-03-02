package webirc.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Ayzen
 * @version 1.0 24.09.2006 18:36:06
 */
public class Smile {
  
  private String name;
  private String mask;
  private String paste;
  private String path;

  private JavaScriptObject regExp;

  public String parse(String text) {
    boolean result = GWT.isScript() ? match(text) : text.matches(mask);
    if (result)
      return "<img style=\"vertical-align: middle; cursor: help;\"" +
             " src=\"" + path + "\" alt=\"" + text + "\" title=\"" + text + "\">";
    else
      return text;
  }

  public String getMask() {
    return mask;
  }

  public void setMask(String mask) {
    this.mask = mask;
    if (GWT.isScript())
      initRegExp(mask);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPaste() {
    return paste;
  }

  public void setPaste(String paste) {
    this.paste = paste;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  private native void initRegExp(String expr) /*-{
    this.@webirc.client.utils.Smile::regExp = new RegExp(expr);
  }-*/;

  private native boolean match(String text) /*-{
    var matchObj = this.@webirc.client.utils.Smile::regExp.exec(text);
    return (matchObj == null) ? false : (text == matchObj[0]);
  }-*/;

}
