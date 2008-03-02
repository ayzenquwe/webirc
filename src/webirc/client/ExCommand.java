package webirc.client;

import com.google.gwt.user.client.Command;

/**
 * @author Ayzen
 * @version 1.0 15.09.2006 23:57:46
 */
public abstract class ExCommand implements Command {

  public void execute() {}

  public abstract void execute(Object response);
}
