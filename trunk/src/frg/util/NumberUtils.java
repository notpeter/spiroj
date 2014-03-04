/*
 * Utils.java
 *
 * Created on 29 June 2005, 14:04
 */

package frg.util;

/**
 * Utilities
 * @author  fgrebenicek
 */
public class NumberUtils {

  public static double parseDouble(String str, double defValue) {
    try { 
      return Double.parseDouble(str); 
    }
    catch (NumberFormatException _) {
      return defValue;
    }
  }
  
  public static int parseInt(String str, int defValue) {
    try {
      return Integer.parseInt(str);
    }
    catch (NumberFormatException _) {
      return defValue;
    }
  }
  
  public static java.awt.Color parseRgbColor(String str, java.awt.Color defValue) {
    try {
      if (str.startsWith("#")) 
        return java.awt.Color.decode(str);
      java.util.StringTokenizer lex = new java.util.StringTokenizer(str, "(,)");
      lex.nextToken();
      int red   = Integer.parseInt(lex.nextToken().trim());
      int green = Integer.parseInt(lex.nextToken().trim());
      int blue  = Integer.parseInt(lex.nextToken().trim());
      return new java.awt.Color(red, green, blue);
    }
    catch (Exception ex) { }
    return defValue;
  }
  
  public static String colorToHexString(java.awt.Color color) {
    return "#" +
      byteToHexString(color.getRed()) +
      byteToHexString(color.getGreen()) +
      byteToHexString(color.getBlue());
  }
  
  public static char[] HEX_TAB = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
  
  public static String byteToHexString(int num) {
    char buf[] = new char[2];
    buf[0] = HEX_TAB[0x0F & num];
    buf[1] = HEX_TAB[0x0F & (num >> 4)];
    return new String(buf);
  }
}
