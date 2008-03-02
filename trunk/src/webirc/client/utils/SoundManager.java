package webirc.client.utils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Ayzen
 * @version 1.0 05.01.2007 22:14:02
 */
public class SoundManager {

  private static HTML soundTag = null;

  public static void playSound(String sound) {
    if (sound == null)
      return;

    if (soundTag == null)
      initSound();

    soundTag.setHTML("<embed src='" + sound + "' hidden='true' autostart='true' loop='false' width='0' height='0'>");
  }

  private static void initSound() {
    soundTag = new HTML();
    soundTag.setSize("0", "0");
    RootPanel.get().add(soundTag);
  }

}
