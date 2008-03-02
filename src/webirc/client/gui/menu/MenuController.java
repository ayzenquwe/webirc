/**
 *
 *
 * @author Ayzen
 * @version 1.0 25.02.2007 19:44:26 
 */
package webirc.client.gui.menu;

import webirc.client.MainSystem;
import webirc.client.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.*;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Node;

public class MenuController {
  private static MenuController ourInstance = new MenuController();

  private HashMap menus = new HashMap();
  private HashMap parameters = null;

  public static MenuController getInstance() {
    return ourInstance;
  }

  private static final String MENUS_URL = "Menus.xml";

  private MenuController() {
    HTTPRequest.asyncGet(MENUS_URL, new ResponseTextHandler() {
      public void onCompletion(final String responseText) {
        DeferredCommand.add(new Command() {
          public void execute() {
            parse(responseText);
          }
        });
      }
    });
  }

  private void parse(String xml) {
    try {
      Document doc = XMLParser.parse(xml);
      if (doc != null) {
        NodeList menuNodes = doc.getChildNodes().item(0).getChildNodes();
        for (int i = 0; i < menuNodes.getLength(); i++) {
          Node nodeMenu = menuNodes.item(i);
          if (!nodeMenu.getNodeName().equalsIgnoreCase("item"))
            continue;

          // Getting the name of menu
          Node name = nodeMenu.getAttributes().getNamedItem("name");
          if (name == null || name.getNodeValue() == null || name.getNodeValue().trim().length() == 0)
            throw new MenuParsingException("Item without name");

          // Creating menu with specified name
          MenuBar menuBar = new MenuBar(true);
          menuBar.setAutoOpen(true);
          menus.put(name.getNodeValue(), menuBar);

          // Adding item structure to a menu
          NodeList attributes = nodeMenu.getChildNodes();
          for (int j = 0; j < attributes.getLength(); j++) {
            Node attrNode = attributes.item(j);
            String attrName = attrNode.getNodeName();
            if (attrName.equalsIgnoreCase("item"))
              addMenuBar(attrNode, menuBar);
          }
        }
      }
    }
    catch (Exception e) {
      MainSystem.showError(e.getMessage());
    }
  }

  private void addMenuBar(Node node, MenuBar menu) throws Exception {
    String name = null;
    String message = null;
    MenuBar subMenu = null;

    NodeList attributes = node.getChildNodes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Node attrNode = attributes.item(i);
      String attrName = attrNode.getNodeName();
      if (attrName.equalsIgnoreCase("send"))
        message = attrNode.getChildNodes().item(0).getNodeValue();
      else if (attrName.equalsIgnoreCase("name"))
        name = Utils.getValueForLocale(attrNode);
      else if (attrName.equalsIgnoreCase("item")) {
        if (subMenu == null)
          subMenu = new MenuBar(true);
        subMenu.setAutoOpen(true);
        addMenuBar(attrNode, subMenu);
      }
    }

    if (name == null)
      throw new MenuParsingException("Item without name");

    Node icon = node.getAttributes().getNamedItem("icon");

    String iconStyle;
    if (icon != null)
      iconStyle = icon.getNodeValue();
    else
      iconStyle = "gwt-Icon";

    String html = "<table cellpadding='0' cellspacing='0'><tr><td align='center' valign='middle'>" +
                  "<div class='gwt-Icon " + iconStyle +
                  "' style='cursor: default;'></div></td><td style='white-space: nowrap;'>&nbsp;" + name +
                  "</div></td><td width='100%' align='right' valign='middle'>";

    final String msg = message;
    // If we haven't subMenu we just create an item with command
    if (subMenu == null) {
      Command cmd =  new Command() {
        public void execute() {
          sendDispatcher(msg);
        }
      };
      html += "</td></tr></table>";
      menu.addItem(html, true, cmd);
    }
    else {
       html += "<div class='menu-item-exp'></div></td></tr></table>";
      menu.addItem(html, true, subMenu);
    }
  }

  private void sendDispatcher(String message) {
    String msg = message;
    if (parameters != null)
      for (Iterator it = parameters.entrySet().iterator(); it.hasNext();) {
        Map.Entry paramEntry = (Map.Entry) it.next();
        msg = msg.replaceAll("[{]" + paramEntry.getKey().toString() + "[}]", paramEntry.getValue().toString());
      }
    MainSystem.sendMessage(msg);
  }

  public ContextMenu getContextMenu(String menuName) throws MenuNotFoundException {
    MenuBar menuBar = (MenuBar) menus.get(menuName);
    if (menuBar == null)
      throw new MenuNotFoundException(menuName);

    return new ContextMenu(menuBar);
  }

  public HashMap getParameters() {

    return parameters;
  }

  public void setParameters(HashMap parameters) {
    this.parameters = parameters;
  }

  public void setParameter(String name, Object value) {
    if (parameters == null)
      parameters = new HashMap();

    parameters.put(name, value);
  }

  public Object getParameter(String name) {
    if (parameters != null)
      return parameters.get(name);
    else
      return null;
  }

}
