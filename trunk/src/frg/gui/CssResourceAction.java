/*
 * ResourceAction.java
 *
 * Created on 15. ríjen 2001, 11:15
 */

package frg.gui;

import java.util.*;
import javax.swing.*;

/**
 * Extension of AbstractAction.
 * Uses resource bundle
 * @author  Franta
 * @version 3.0 (2004/04/06)
 */
public abstract class CssResourceAction extends AbstractAction {

  public CssResourceAction() {
    init(getClass().getName());
  }
  
  
  /** Creates new ResourceAction */
  public CssResourceAction(String id) {
    init(id);
  }
  
  protected void init(String id) {
    CssResourceMan resource = new CssResourceMan(id);
    try {
      putValue(Action.NAME,              resource.getString("name"));
      putValue(Action.SHORT_DESCRIPTION, resource.getString("tip"));
      String key = resource.getString("key");
      putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key));
      String icon = resource.getString("icon");
      java.net.URL url = getClass().getResource(icon);
      if (url == null)
        throw new MissingResourceException("Icon not found!", id, icon);
      putValue(Action.SMALL_ICON, new ImageIcon(url));
    } catch (MissingResourceException ex) {
      System.out.println("Warning: "+ex.getMessage()+" class "+ex.getClassName()+
        " key "+ex.getKey());
    }
  }

  
  /*
  public ResourceAction(String name, String icon, String tooltip) {
    this(name, icon);
    putValue(Action.SHORT_DESCRIPTION, getResourceString(tooltip));
  }
  
  public ResourceAction(String name, String icon, String tooltip, String keystroke) {
    this(name, icon, tooltip);
    setAccelerator(getResourceString(keystroke));
  }
  */
  
  public void setAccelerator(String keystroke) {
    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(keystroke));
  }
  
  public KeyStroke getAccelerator() {
    return (KeyStroke) getValue(Action.ACCELERATOR_KEY);
  }
  
  public ImageIcon getIcon() {
    return (ImageIcon) getValue(Action.SMALL_ICON);
  }
  
  /*
  public static String getResourceString(String name) {
    return resource.getString(name);
  }
  
  public static ImageIcon getResourceIcon(Object instance, String name) {
    return resource.getIcon(instance, name);
  }
  
  public static void loadComboEnum(JComboBox combo, String rsName) {
    resource.loadComboEnum(combo, rsName);
  }
   */
}
