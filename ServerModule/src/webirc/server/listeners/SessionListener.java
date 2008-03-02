package webirc.server.listeners;

import org.apache.log4j.Logger;
import webirc.server.IRCConnectHandler;
import webirc.server.Utils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class SessionListener implements HttpSessionListener {

  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
  }

  public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    HttpSession session = httpSessionEvent.getSession();
    Logger logger = (Logger) session.getAttribute(Utils.ATTR_LOGGER);

    logger.info("Session has been destroyed");

    IRCConnectHandler handler = (IRCConnectHandler) session.getAttribute("irc");
    if (handler != null)
      handler.close();
    else
      logger.warn("Session was without an IRC handler");

    Thread thread = (Thread) session.getAttribute("ajax");
    if (thread != null) {
      logger.info("Interrupting AJAX continuation thread");
      thread.interrupt();
    }
  }

}
