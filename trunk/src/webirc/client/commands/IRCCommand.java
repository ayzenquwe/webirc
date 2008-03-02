package webirc.client.commands;

import webirc.client.User;

/**
 * @author Ayzen
 * @version 1.0 12.07.2006 22:32:10
 */
public abstract class IRCCommand {

  protected String name = "";

  protected String prefix;
  protected String command;
  protected String params;

  protected User sender = null;

  protected int paramsIndex = 0;

  public IRCCommand() {
  }

  public IRCCommand(String prefix, String command, String params) {
    this.command = command;
    this.params = params;
    this.prefix = prefix;
    if (prefix != null && prefix.length() > 0)
      parseSender();
  }

  private void parseSender() {
    String[] names = prefix.split("[!@]");
    if (names.length > 0)
      sender = new User(names[0]);
    if (names.length > 2) {
      sender.setUserName(names[1]);
      sender.setHostName(names[2]);
    }
    else if (names.length == 2)
      sender.setHostName(names[1]);
  }

  protected String nextParam() {
    if (paramsIndex + 1 >= params.length() || params.charAt(paramsIndex) != ' ')
      return null;
    // Skipping space
    paramsIndex++;
    int endIndex = params.indexOf(' ', paramsIndex);
    String result;
    if (endIndex == -1) {
      result = params.substring(paramsIndex);
      paramsIndex = params.length();
    }
    else {
      result = params.substring(paramsIndex, endIndex);
      paramsIndex = endIndex;
    }
    return result;
  }

  protected String lastParam() {
    if (paramsIndex + 1 >= params.length() || params.charAt(paramsIndex) != ' ')
      return null;
    int index = paramsIndex;
    if (params.charAt(++index) == ':')
      index++;
    return params.substring(index);
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public String getFullMessage() {
    return prefix + ": " + name + params;
  }

  public String toString() {
    return name + params;
  }
}