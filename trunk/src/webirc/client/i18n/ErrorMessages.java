package webirc.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author Ayzen
 * @version 1.0 20.08.2006 14:43:57
 */
public interface ErrorMessages extends Messages {
  public String synchError();
  public String connectError();
  public String incorrectResponse();
  public String notConnectedError();
  public String noHandler();
  public String internalSynchError();
  public String internalFatalSynchError();
  public String disconnectedError();

  public String notEnoughParameters();
  public String receiverIsNotDefined();
  public String notCorrectParam(String parameter);
  public String nicknameInUse(String nickname);
}
