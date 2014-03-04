/*
 * ResourceMan.java
 *
 * Created on 28. listopad 2002, 14:58
 */

package frg.gui;

import java.util.*;
import javax.swing.*;

import frg.util.*;

/**
 * Resource Manager.
 * @author  Franta
 * @version 1
 */
public class CssResourceMan extends java.lang.Object {
  
  private static volatile String resourceName = "/resources/main";
  private static volatile CssResourceBundle resource = null;
  private static Object resourceLock = new Object();
  
  protected String tagName = null;
  protected Properties tag = null;
  
  /** Creates new ResourceMan */
  public CssResourceMan() {
    setResource(resourceName);
  }
  
  public CssResourceMan(String tag) {
    this();
    setTag(tag);
  }
  
  public CssResourceBundle getBundle() {
    return resource;
  }
  
  public static void setResource(String name) {
    synchronized (resourceLock) {
      if (resource == null || resourceName != name || !resourceName.equals(name)) {
        resourceName = name;
        resource = null;
        try { resource = new CssResourceBundle(resourceName); }
        catch (Exception ex) {
          System.err.println("Resource Manager -- resource loading error: "+resourceName);
          System.out.println("  "+ex.getMessage());
        }
      }
    }  
  }
  
  public void setTag(String tagName) {
    this.tagName = tagName;
    this.tag = resource.getTag(tagName);
  }
  
  public Enumeration getKeys() {
    return resource.getKeys();
  }
  
  public boolean contains(String name) {
    try {
      getString(name);
      return true; 
    } catch (Exception _) {}
    return false;
  }
  
  public String getString(String name) {
    String result = "";
    try {
      if (tag == null)
        result = resource.getString(name);
      else {
        String val = tag.getProperty(name);
        if (val == null) 
          throw new MissingResourceException("Tag not found", tagName, name);
        result = val;
      }
    } catch (MissingResourceException ex) {
      System.out.println("Warning: "+ex.getMessage()+" class "+tagName+
        " key "+ex.getKey());
    }
    return StringUtils.unquote(result);
  }
  
  public int getInt(String name) {
    try {
      return Integer.parseInt(getString(name));
    } catch (Exception ex) {
      System.out.println("Warning: "+ex.getMessage()+" key "+name);
    }
    return 0;
  }
  
  public ImageIcon getIcon(String name) {
    return getIcon(this, name);
  }
  
  public ImageIcon getIcon(Object instance, String name) {
    try {
      String icon = getString(name);
      java.net.URL url = instance.getClass().getResource(icon);
      if (url == null)
        throw new MissingResourceException("Icon not found!", instance.getClass().getName(), icon);
      return new ImageIcon(url);
    } catch (MissingResourceException ex) {
      System.out.println("Warning: "+ex.getMessage()+" class "+ex.getClassName()+
        " key "+ex.getKey());
    }
    return null;
  }
  
  public java.awt.Image getIconImage(String name) {
    ImageIcon icon = getIcon(name);
    if (icon != null)
      return icon.getImage();
    else
      return null;
  }
  
  public void loadComboEnum(JComboBox combo, String rsName) {
    String types = getString(rsName);
    StringTokenizer lex = new StringTokenizer(types);
    while (lex.hasMoreTokens()) 
      combo.addItem(lex.nextToken());
  }
  
  public void loadComboEnum(JComboBox combo, String rsName, String separators) {
    String types = getString(rsName);
    StringTokenizer lex = new StringTokenizer(types, separators);
    while (lex.hasMoreTokens()) 
      combo.addItem(lex.nextToken());
  }
  
  public void setPreferredWidths(JTable table, String rsName) {
    String types = getString(rsName);
    int count = table.getColumnCount();
    StringTokenizer lex = new StringTokenizer(types);
    for (int i=0; i<count && lex.hasMoreTokens(); i++) {
      javax.swing.table.TableColumn column = table.getColumnModel().getColumn(i);
      try {
        int width = Integer.parseInt(lex.nextToken());
        column.setPreferredWidth(width);
      } catch (NumberFormatException ex) {}
    }
  }
  
  public java.awt.GridBagConstraints getConstraints(String name) {
    return new GbConstraints(getString(name));
  }
  
  public java.awt.Color getColor(String name) throws NumberFormatException {
    String str = getString(name);
    if (str.startsWith("#")) 
      return java.awt.Color.decode(str);
    StringTokenizer lex = new StringTokenizer(str, "(,)");
    lex.nextToken();
    int red   = Integer.parseInt(lex.nextToken());
    int green = Integer.parseInt(lex.nextToken());
    int blue  = Integer.parseInt(lex.nextToken());
    return new java.awt.Color(red, green, blue);
  }

  /** debug
   */
  public void list() {
    resource.css.list(System.out);
  }
}
