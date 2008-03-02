package webirc.server.exceptions;

/**
 * Exception which could occures while synchronizing.
 *
 * @author Ayzen
 * @version 1.0 09.02.2007 11:34:19
 */
public class SynchException extends Exception {

  private int errorType;

  public SynchException(String message, int errorType) {
    super(message);
    this.errorType = errorType;
  }

  public int getErrorType() {
    return errorType;
  }

}
