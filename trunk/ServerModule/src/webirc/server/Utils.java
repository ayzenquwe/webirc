package webirc.server;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Some utils functions for server side.
 *
 * @author Ayzen
 * @version 1.0 23.01.2007 20:20:30
 */
public class Utils {

  /**
   * Name of a session attribute which stores Logger object.
   */
  public static final String ATTR_LOGGER = "logger";

  /**
   * Returns a session logger, and creates it if logger not exists.
   *
   * @param name a name of a logger
   * @param session the session in which logger will be created
   * @param request request for getting remote IP address
   * @return SynchLogger
   */
  public static Logger getLogger(String name, HttpSession session, HttpServletRequest request) {
    Logger logger = (Logger) session.getAttribute(Utils.ATTR_LOGGER);
    if (logger == null) {
      String loggerName = "synch." + name + '.' + request.getRemoteAddr() + '.' + session.getId(); 
      logger = Logger.getLogger(loggerName);
      session.setAttribute(Utils.ATTR_LOGGER, logger);
      logger.trace("Logger '"+ loggerName +"' has been created");
    }

    return logger;
  }

  /**
   * Generate hexed form of IP address.
   *
   * @param ip IP address
   * @return hexed IP address
   */
  public static String getHexedIP(final String ip) {
    if (ip == null)
      return null;

    String[] ipDiv = ip.split("[.]");
    if (ipDiv.length == 1)
      ipDiv = ip.split("[:]");
    StringBuffer hexedIp = new StringBuffer();
    for (int i = 0; i < ipDiv.length; i++) {
      String hex = Integer.toHexString(Integer.parseInt(ipDiv[i]));
      if (hex.length() == 1)
        hexedIp.append("0");
      hexedIp.append(hex.toLowerCase());
    }
    return hexedIp.toString();
  }

  /**
   * Returns the User Agent parameter from the request.
   *
   * @param request client's request
   * @return User Agent parameter
   */
  public static String getUserAgent(HttpServletRequest request) {
    return request.getHeader("user-agent");
  }

  /**
   * Checks if client uses Opera browser.
   *
   * @param request client's request
   * @return true, if client uses Opera browser
   */
  public static boolean isOpera(HttpServletRequest request) {
    return getUserAgent(request).toLowerCase().startsWith("opera");
  }

}
