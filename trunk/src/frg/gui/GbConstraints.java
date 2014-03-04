/*
 * GbConstraints.java
 *
 * Created on 6. prosinec 2002, 14:02
 */

package frg.gui;

import java.awt.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * GridBagConstraints loadable from resources.
 * @author Franta
 * @version 1
 */
public class GbConstraints extends java.awt.GridBagConstraints {

  /** Creates new GbConstraints */
  public GbConstraints() {
    super();
  }
  
  public GbConstraints(String spec) {
    super();
    parse(spec);
  }

  public void parse(String spec) {
    StringTokenizer lex = new StringTokenizer(spec);
    while (lex.hasMoreTokens()) {
      String token = lex.nextToken();
      String name = token;
      String value = "";
      int ddot = token.indexOf(':');
      if (ddot >= 0) {
        name = token.substring(0, ddot);
        value = token.substring(ddot+1);
      }
      ////
      String methodName = "parse"+Character.toUpperCase(name.charAt(0))+name.substring(1);
      try {
        Method method = getClass().getMethod(methodName, new Class[] { String.class });
        method.invoke(this, new Object[] { value });
        continue;
      } catch (Throwable _) {}
      try {
        parseInt(name, value);
      } catch (Throwable _) {}
    }
  }
  
  public void parseInt(String name, String value) throws Exception {
    // System.out.println("parseInt "+name+" "+value);
    Field field = getClass().getField(name);
    try { 
      field.setInt(this, Integer.parseInt(value)); 
      return;
    }
    catch (NumberFormatException nfe) {}
    Field constant = getClass().getField(value);
    field.setInt(this, constant.getInt(this));
  }
  
  public void parseInsets(String value) {
    // System.out.println("parseInsets "+value);
    StringTokenizer lex = new StringTokenizer(value, "[,]");
    try { 
      insets.top    = Integer.parseInt(lex.nextToken()); 
      insets.left   = Integer.parseInt(lex.nextToken()); 
      insets.bottom = Integer.parseInt(lex.nextToken()); 
      insets.right  = Integer.parseInt(lex.nextToken()); 
    }
    catch (Exception _) {}
  }
  
  public void parseWeightx(String value) {
    try { weightx = Double.parseDouble(value); }
    catch (NumberFormatException _) {}
  }
  
  public void parseWeighty(String value) {
    try { weighty = Double.parseDouble(value); }
    catch (NumberFormatException _) {}
  }
  
  public String toString() {
    try {
      StringBuffer buf = new StringBuffer();
      Field[] fields = getClass().getFields();
      for (int i=0; i<fields.length; i++) {
        // String type = fields[i].getType().toString();
        int mod = fields[i].getModifiers();
        if (!Modifier.isStatic(mod)) {
          buf.append(fields[i].getName());
          buf.append(':');
          if (fields[i].getName().equals("insets"))
            buf.append("["+insets.top+","+insets.left+","+insets.bottom+","+insets.right+"]");
          else
            buf.append(fields[i].get(this));
          buf.append(' ');
        }
      }
      return buf.toString();
    } catch (Throwable _) {}
    return "<error>";
  }
  
  /** Tests
   /
  public static void main(String[] args) {
    System.out.println("Test");
    System.out.println(new GbConstraints("anchor:WEST gridwidth:REMAINDER insets:[1,2,3,4] weightx:1.0 weighty:2.1"));
  }
  /* */
}
