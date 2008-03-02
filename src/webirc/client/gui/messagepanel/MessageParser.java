package webirc.client.gui.messagepanel;

import webirc.client.utils.Smileys;

/**
 * @author Ayzen
 * @version 1.0 24.09.2006 0:26:39
 */
public class MessageParser {

  private static final String LT = "&lt;";
  private static final String GT = "&gt;";

  private static final char FONT_BOLD = '\u0002';
  private static final char FONT_ITALIC = '\u0016';
  private static final char FONT_UNDERSTRIKED = '\u001F';
  private static final char FONT_COLOR = '\u0003';

  private static final String[] colors = {"#FFFFFF", "#000000", "#00007F", "#009300", "#FF0000", "#7F0000",
                                          "#9C009C", "#FC7F00", "#FFFF00", "#00FC00", "#009393", "#00FFFF",
                                          "#0000FC", "#FF00FF", "#7F7F7F", "#D2D2D2", "transparent"};

  private static final int defaultColor = 1;
  private static final int defaultBGColor = 16;

  private boolean openedB = false;
  private boolean openedI = false;
  private boolean openedU = false;
  private boolean openedFont = false;

  private int fontColor = defaultColor;
  private int fontBGColor = defaultBGColor;

  private String text;
  private int textIndex = 0;

  public static String parse(final String text) {
    return new MessageParser().parseImpl(text);
  }

  private String parseImpl(final String text) {
    this.text = text;
    String result = "";
    boolean wasSpace = true;

    if (text != null && text.trim().length() > 0)
      for (textIndex = 0; textIndex < text.length(); textIndex++) {
        char nextChar = text.charAt(textIndex);
        if (isFontStyle(nextChar))
          result += parseFontStyle(nextChar);
          // For smile, anchors there must be a space before
        else if (wasSpace) {
          String smileyed = parseSmile(nextChar);
          if (smileyed.length() == 1) {
            if (nextChar == 'h' || nextChar == 'H' || nextChar == 'f' || nextChar == 'F' ||
                nextChar == 'w' || nextChar == 'W')
              result += parseAnchor(nextChar);
            else
              result += checkChar(nextChar);
          }
          else
            result += smileyed;
        }
        else
          result += checkChar(nextChar);
        wasSpace = (nextChar == ' ' || nextChar == ' ');
      }

    return result + closeTags();
  }

  private String checkChar(char aChar) {
    if (aChar == '<')
      return LT;
    else if (aChar == '>')
      return GT;
    else
      return String.valueOf(aChar);
  }

  private String parseSmile(char firstChar) {
    String result = String.valueOf(firstChar);

    int endIndex = text.indexOf(' ', textIndex);
    if (endIndex == -1)
      endIndex = text.length();

    String someText = text.substring(textIndex, endIndex);

    String withSmileys = Smileys.getInstance().parseSmile(someText);
    if (someText.length() != withSmileys.length()) {
      result = withSmileys;
      textIndex = endIndex - 1;
    }

    return result;
  }

  private String parseAnchor(char firstChar) {
    String result = String.valueOf(firstChar);

    int endIndex = text.indexOf(' ', textIndex);
    if (endIndex == -1)
      endIndex = text.length();
    String anchor = text.substring(textIndex, endIndex);
    anchor = anchor.replaceAll("<", LT).replaceAll(">", GT);
    String anchorL = anchor.toLowerCase();

    if (anchorL.startsWith("http://") || anchorL.startsWith("ftp://") ||
        anchorL.startsWith("www.")) {
      if (anchorL.startsWith("www."))
        anchor = "http://" + anchor;
      result = "<a href='" + anchor + "' target='_blank'>" + anchor + "</a>";
      textIndex = endIndex - 1;
    }

    return result;
  }

  private String parseFontStyle(char type) {
    String result = closeTags();
    switch (type) {
      case FONT_BOLD:
        openedB = !openedB;
        break;
      case FONT_ITALIC:
        openedI = !openedI;
        break;
      case FONT_UNDERSTRIKED:
        openedU = !openedU;
        break;
      case FONT_COLOR:
        openedFont = true;
        parseFontColor();
        break;
    }
    return result + openTags();
  }

  /**
   * Parses font color and background color.
   */
  private void parseFontColor() {
    boolean trueColor = false;
    int index = 1;
    char c;
    String firstColorStr = "";
    String secondColorStr = "";
    try {
      // Trying to get the first digit of color
      c = text.charAt(textIndex + index);
      if (Character.isDigit(c)) {
        index++;
        firstColorStr += c;
        // Trying to get the second digit of color
        c = text.charAt(textIndex + index);
        if (Character.isDigit(c)) {
          index++;
          firstColorStr += c;
          c = text.charAt(textIndex + index);
        }
        // If there is background color...
        if (c == ',') {
          index++;
          // Trying to get the second digit of background color
          c = text.charAt(textIndex + index);
          if (Character.isDigit(c)) {
            index++;
            secondColorStr += c;
            // Trying to get the second digit of background color
            c = text.charAt(textIndex + index);
            if (Character.isDigit(c)) {
              index++;
              secondColorStr += c;
            }
          }
        }
      }
      // Parsing succed
      trueColor = true;
    }
    catch (Exception e) {
      textIndex = text.length();
    }

    // Finishing parsing...
    int color = fontColor;
    int bgColor = fontBGColor;
    if (trueColor) {
      textIndex += index - 1;
      if (firstColorStr.trim().length() > 0)
        color = Integer.parseInt(firstColorStr);
      else
        color = defaultColor;
      if (secondColorStr.length() != 0)
        bgColor = Integer.parseInt(secondColorStr);
      else
        bgColor = defaultBGColor;
      if (color >= colors.length)
        color = defaultColor;
      if (bgColor >= colors.length)
        bgColor = defaultBGColor;
    }
    fontColor = color;
    fontBGColor = bgColor;
  }

  private String closeTags() {
    String result = "";
    if (openedB)
      result += "</b>";
    if (openedI)
      result += "</i>";
    if (openedU)
      result += "</u>";
    if (openedFont)
      result += "</span>";
    return result;
  }

  private String openTags() {
    String result = "";
    if (openedB)
      result += "<b>";
    if (openedI)
      result += "<i>";
    if (openedU)
      result += "<u>";
    if (openedFont)
      result += "<span style='color: " + colors[fontColor] + "; background-color: " + colors[fontBGColor] + ";'>";
    return result;
  }

  private boolean isFontStyle(char c) {
    return c == FONT_BOLD || c == FONT_COLOR || c == FONT_ITALIC || c == FONT_UNDERSTRIKED;
  }

}
