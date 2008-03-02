/**
 * 
 *
 * @author Ayzen
 * @version 1.0 21.09.2006 22:20:39 
 */
package webirc.client.utils;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import java.util.Vector;

public class Aliases {
  private static Aliases ourInstance = new Aliases();

  public static Aliases getInstance() {
    return ourInstance;
  }

  private static final String ALIASES_URL = "Aliases.xml";

  private Vector aliases;

  private Aliases() {
    HTTPRequest.asyncGet(ALIASES_URL, new ResponseTextHandler() {
      public void onCompletion(final String responseText) {
        DeferredCommand.add(new Command() {
          public void execute() {
            parse(responseText);
          }
        });
      }
    });
  }

  public String parseAlias(String text) throws AliasException {
    for (int i = 0; i < aliases.size(); i++) {
      Alias alias = (Alias) aliases.get(i);
      if (alias.isAlias(text))
        text = parseAlias(alias.replace(text));
    }
    return text;
  }

  private void parse(String xml) {
    aliases = new Vector();
    Document doc = XMLParser.parse(xml);
    if (doc != null) {
      NodeList aliasesNodes = doc.getChildNodes().item(0).getChildNodes();
      for (int i = 0; i < aliasesNodes.getLength(); i++) {
        Node nodeAlias = aliasesNodes.item(i);
        if (!nodeAlias.getNodeName().equalsIgnoreCase("alias"))
          continue;
        
        Alias alias = new Alias();
        Vector namedAliases = new Vector();

        NodeList attributes = nodeAlias.getChildNodes();
        for (int j = 0; j < attributes.getLength(); j++) {
          Node attrNode = attributes.item(j);
          String attrName = attrNode.getNodeName();
          if (attrName.equalsIgnoreCase("alias"))
            namedAliases.add(attrNode.getChildNodes().item(0).getNodeValue().toUpperCase());
          else if (attrName.equalsIgnoreCase("name"))
            alias.setName(Utils.getValueForLocale(attrNode));
          else if (attrName.equalsIgnoreCase("description"))
            alias.setDescription(Utils.getValueForLocale(attrNode));
          else if (attrName.equalsIgnoreCase("usage"))
            alias.setUsage(Utils.getValueForLocale(attrNode));
          else if (attrName.equalsIgnoreCase("string")) {
            int paramsInt;
            Node param = attrNode.getAttributes().getNamedItem("params");
            if (param != null) {
              String params = param.getNodeValue();
              try {
                paramsInt = Integer.parseInt(params);
              }
              catch (Exception e) {
                paramsInt = 0;
              }
            }
            else
              paramsInt = 0;  
            alias.setString(Utils.getValueForLocale(attrNode).replaceAll("@1", "\u0001"));
            alias.setNeededParams(paramsInt);
          }
        }
        if (!namedAliases.isEmpty()) {
          String[] aliasesArray = new String[namedAliases.size()];
          for (int j = 0; j < namedAliases.size(); j++)
            aliasesArray[j] = (String) namedAliases.get(j);
          alias.setAliases(aliasesArray);
          aliases.add(alias);
        }
      }
    }
  }

}
