package webirc.client.utils;

import webirc.client.GUIController;
import webirc.client.WebIRC;
import webirc.client.MainSystem;

/**
 * @author Ayzen
 * @version 1.0 21.09.2006 22:43:10
 */
public class Alias {
  private String name;
  private String description;
  private String usage;
  private String[] aliases;
  private String string;
  private int params = 0;
  private int neededParams;

  public boolean isAlias(final String text) {
    String textU = text.toUpperCase();
    for (int i = 0; i < aliases.length; i++)
      if (textU.startsWith(aliases[i] + ' ') || textU.equals(aliases[i]))
        return true;
    return false;
  }

  public String replace(String text) throws AliasException {
    String[] paramsStr = text.split(" ", params + 1);
    if (paramsStr.length - 1 < neededParams)
      throw new AliasException(WebIRC.errorMessages.notEnoughParameters());

    String result;
    if (string.indexOf("<receiver>") != -1) {
      Object receiver = GUIController.getInstance().getMessagePanel().getSelectedTabEntity();
      if (receiver == null)
        throw new AliasException(WebIRC.errorMessages.receiverIsNotDefined());

      result = string.replaceAll("[<]receiver[>]", receiver.toString());
    }
    else
      result = string;

    int paramDelta = 0;

    if (result.indexOf("{channel}") != -1) {
      if (paramsStr.length - 1 == params)
        result = result.replaceAll("[{]channel[}]", "{0}");
      else {
        result = result.replaceAll("[{]channel[}]", MainSystem.getActiveChannel().toString());
        paramDelta = 1;
      }
    }

    for (int i = 0; i < params; i++) {
      if (i < paramsStr.length - 1)
        result = result.replaceAll("[{][" + (i + paramDelta) + "][}]", paramsStr[i + 1]);
      else
        result = result.replaceAll("[{][" + (i + paramDelta) + "][}]", "");
    }
    return result;
  }

  public int getNeededParams() {
    return neededParams;
  }

  public void setNeededParams(int neededParams) {
    this.neededParams = neededParams;
  }

  public String[] getAliases() {
    return aliases;
  }

  public void setAliases(String[] aliases) {
    this.aliases = aliases;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUsage() {
    return usage;
  }

  public void setUsage(String usage) {
    this.usage = usage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
    // Need to count params...
    boolean open = false;
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) == '{')
        open = true;
      else if (string.charAt(i) == '}' && open) {
        open = false;
        params++;
      }
    }
  }
}
