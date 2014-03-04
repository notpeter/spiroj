/*
 * About.java
 *
 * Created on May 31, 2004, 11:50 AM
 */

package frg.spiro;

import java.text.MessageFormat;
import javax.swing.JOptionPane;
import frg.gui.*;

/**
 * Stub for About dialog.
 * @author  Franta
 * @version 1.0
 */
public class About extends Object {

  public static final String name = "SpiroJ";
  public static final String version = "1.0.2 beta";
  java.awt.Component parent;
  
  /** Creates new About */
  public About(java.awt.Component parent) {
    this.parent = parent;
  }
  
  public void show() {
    CssResourceMan rsc = new CssResourceMan(getClass().getName());
    MessageFormat fmt = new MessageFormat(rsc.getString("msg"));
    String msg   = fmt.format(new Object[] { name, version });
    fmt = new MessageFormat(rsc.getString("title"));
    String title = fmt.format(new Object[] {name, version});
    JOptionPane.showMessageDialog(parent, msg, title, 
      JOptionPane.INFORMATION_MESSAGE,
      rsc.getIcon("icon"));
  }

  public static String getVersion() {
    return version;
  }
  
  public static String getName() {
    return name;
  }
}
