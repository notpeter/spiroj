/*
 * CssResourceBundle.java
 *
 * Created on April 5, 2004, 2:18 PM
 */

package frg.util;

import java.io.*;
import java.util.*;

/**
 * CSS properties resource bundle. 
 * Similar to PropertyResourceBundle.
 * @author Franta
 * @version 1.0
 */
public class CssResourceBundle extends java.util.ResourceBundle {
  
  public CssProperties css = new CssProperties();
  
  protected CssResourceBundle() {
    super();
  }
  
  /** Creates new CssResourceBundle */
  public CssResourceBundle(InputStream stream) throws IOException {
    super();
    css.load(stream);
  }
  
  public CssResourceBundle(String name, String locale, String encoding) 
  throws IOException {
    this();
    load(name, locale, encoding);
  }
  
  public CssResourceBundle(String name, Locale locale, String encoding) 
  throws IOException {
    this();
    try { 
      load(name, locale.toString(), encoding); 
      return;
    } catch (Exception a) {}
    try { 
      load(name, locale.getLanguage(), encoding); 
      return;
    } catch (Exception b) {}
    load(name, null, encoding); 
  }
  
  public CssResourceBundle(String name, String encoding) throws IOException {
    this(name, Locale.getDefault(), encoding);
  }
  
  public CssResourceBundle(String name) throws IOException {
    this(name, System.getProperty("file.encoding"));
  }
  
  private void load(String name, String locale, String encoding) throws IOException {
    String path = name;
    if (locale != null)
      path += "_"+locale; 
    path += ".css";
    ////
    // System.out.println("Looking for: "+path);
    InputStream is = getClass().getResourceAsStream(path);
    if (is == null)
      throw new IOException("Resource not found: "+path);
    try {
      InputStreamReader reader = new InputStreamReader(is, encoding);
      css.parse(reader);
    }
    finally {
      is.close();
    }
  }
  
  public java.util.Enumeration getKeys() {
    Vector keys = new Vector();
    for (Enumeration en=css.getTags(); en.hasMoreElements();) {
      String tag = en.nextElement().toString();
      Properties prop = css.getTag(tag);
      for (Enumeration ep=prop.keys(); ep.hasMoreElements();)
        keys.addElement(tag+'.'+ep.nextElement());
    }
    return keys.elements();
  }

  /*
  public java.util.Locale getLocale() {
  }
  protected void setParent(java.util.ResourceBundle resourceBundle) {
  } 
  */
  
  protected java.lang.Object handleGetObject(java.lang.String str) 
  throws java.util.MissingResourceException {
    for (int i=str.lastIndexOf('.'); i >= 0; i=str.lastIndexOf('.', i-1)) {
      String tag = str.substring(0, i);
      String ext = str.substring(i+1);
      String res = css.getTagProperty(tag, ext);
      if (res != null)
        return res;
    }
    Object obj = css.getTag(str);
    if (obj == null)
      throw new MissingResourceException("Tag not found", getClass().getName(), str);
    return obj;
  }

  /////////////////////////////////////////////////////////////////////////////
  // CSS specific
  
  public Properties getTag(String tag) {
    return css.getTag(tag);
  }
  
  public String getTagProperty(String tag, String name) {
    return css.getTagProperty(tag, name);
  }
  
  public String getTagProperty(String tag, String name, String defval) {
    return css.getTagProperty(tag, name, defval);
  }
}
