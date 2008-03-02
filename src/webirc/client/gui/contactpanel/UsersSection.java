package webirc.client.gui.contactpanel;

import webirc.client.WebIRC;
import webirc.client.gui.sectionpanel.Section;

/**
 * @author Ayzen
 * @version 1.0 17.08.2006 0:20:16
 */
public class UsersSection extends Section {

  public UsersSection() {
    super();
    setTitle(WebIRC.mainMessages.users());
    setShowCounter(true);
  }

}
