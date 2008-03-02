/**
 * 
 *
 * @author Ayzen
 * @version 1.0 24.09.2006 18:37:46 
 */
package webirc.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import java.util.Vector;

public class Smileys {
  private static Smileys ourInstance = new Smileys();

  public static Smileys getInstance() {
    return ourInstance;
  }

  private static final String SMILES_URL = "Smileys.xml";

  private Vector smiles;

  private Smileys() {
    HTTPRequest.asyncGet(SMILES_URL, new ResponseTextHandler() {
      public void onCompletion(final String responseText) {
        DeferredCommand.add(new Command() {
          public void execute() {
            parse(responseText);
          }
        });
      }
    });
  }

  public String parseSmile(final String text) {
    String result = text;
    for (int i = 0; i < smiles.size(); i++) {
      Smile smile = (Smile) smiles.get(i);
      result = smile.parse(text);
      if (text.length() != result.length())
        break;
    }
    return result;
  }

  private void parse(String xml) {
    smiles = new Vector();
    Document doc = XMLParser.parse(xml);
    // If xml file parsed succesfully...
    if (doc != null) {
      String path = "";
      // Getting the main entry in file
      Node mainNode = doc.getElementsByTagName("smiles").item(0);
      // Getting the "path" attribute
      Node node = mainNode.getAttributes().getNamedItem("path");
      if (node != null)
        path = node.getNodeValue();

      // Parsing all smiles in the xml file
      NodeList smilesNodes = mainNode.getChildNodes();
      for (int i = 0; i < smilesNodes.getLength(); i++) {
        Smile smile = new Smile();
        Node smileNode = smilesNodes.item(i);
        if (!smileNode.getNodeName().equalsIgnoreCase("smile"))
          continue;
        // Parsing smile's parameters
        NodeList smileParameters = smileNode.getChildNodes();
        for (int j = 0; j < smileParameters.getLength(); j++) {
          Node parameterNode = smileParameters.item(j);
          String parameterName = parameterNode.getNodeName();
          // The "name" tag
          if (parameterName.equalsIgnoreCase("name"))
            smile.setName(Utils.getValueForLocale(parameterNode));
          // The "text" tag
          else if (parameterName.equalsIgnoreCase("text")) {
            // Has attributes "paste" and "mask"
            Node paste = parameterNode.getAttributes().getNamedItem("paste");
            if (paste != null)
              smile.setPaste(paste.getNodeValue());
            Node mask = parameterNode.getAttributes().getNamedItem("mask");
            if (mask != null) {
              String maskStr = mask.getNodeValue();
              if (!GWT.isScript())
                maskStr = maskStr.replaceAll("[+]", "++");
              smile.setMask(maskStr);
            }
          }
          // The "image" tag
          else if (parameterName.equalsIgnoreCase("image")) {
            // Has attribute "src"
            Node imgSrc = parameterNode.getAttributes().getNamedItem("src");
            if (imgSrc != null)
              smile.setPath(path + imgSrc.getNodeValue());
          }
        }
        smiles.add(smile);
      }
    }
  }

  public final Vector getSmiles() {
    return smiles;
  }
}
