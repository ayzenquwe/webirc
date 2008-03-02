package webirc.server.exceptions;

/**
 * Server throws this exception if some threads works incorrect.
 *
 * @author Ayzen
 * @version 1.0 25.01.2007 23:05:51
 */
public class ThreadException extends Exception {

  public ThreadException(String s) {
    super(s);
  }

}
