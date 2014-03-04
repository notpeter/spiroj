/*
 * ResourcePane.java
 *
 * Created on 26. øíjen 2001, 13:45
 */

package frg.gui;

import java.awt.Component;
import java.text.MessageFormat;
import javax.swing.*;

/**
 * Extension of JOptionPane features
 * @author Franta
 * @version 2.0
 */

public class CssResourcePane extends JOptionPane {
  
  public static boolean internal = true;  // default implicit type of dialogs.
  public static final int HELP_OPTION  = NO_OPTION;
  public static final int ABORT_OPTION = NO_OPTION;
  
  public static void setInternal(boolean set) { internal = set; }
  public static boolean getInternal() { return internal; }
  
  public static int showYesNo(Component parent, String resPrefix, Object[] msgParams) {
    return showYesNo(parent, resPrefix, msgParams, internal);
  }
  
  public static int showYesNo(Component parent, String resPrefix, Object[] msgParams, boolean inFrame) {
    CssResourceMan rsc = new CssResourceMan(resPrefix);
    String title = rsc.getString("title");
    String msg = rsc.getString("msg");
    String yes = rsc.getString("yes");
    String no  = rsc.getString("no");
    if (msgParams != null) {
      MessageFormat fmt = new MessageFormat(msg);
      msg = fmt.format(msgParams);
    }
    Object[] options = {yes, no};
    if (inFrame)
      return showInternalOptionDialog(parent, msg, title, 
        YES_NO_OPTION, QUESTION_MESSAGE, null, 
        options, options[1]);
    else
      return showOptionDialog(parent, msg, title, 
        YES_NO_OPTION, QUESTION_MESSAGE, null, 
        options, options[1]);
  }
  
  public static int showYesNoCancel(Component parent, String resPrefix, Object[] msgParams) {
    return showYesNoCancel(parent, resPrefix, msgParams, internal);
  }
  
  public static int showYesNoCancel(Component parent, String resPrefix, Object[] msgParams, boolean inFrame) {
    CssResourceMan rsc = new CssResourceMan(resPrefix);
    String title  = rsc.getString("title");
    String msg    = rsc.getString("msg");
    String yes    = rsc.getString("yes");
    String no     = rsc.getString("no");
    String cancel = rsc.getString("cancel");
    if (msgParams != null) {
      MessageFormat fmt = new MessageFormat(msg);
      msg = fmt.format(msgParams);
    }
    Object[] options = {yes, no, cancel};
    if (inFrame)
      return showInternalOptionDialog(parent, msg, title, 
        YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null, 
        options, options[2]);
    else 
      return showOptionDialog(parent, msg, title, 
        YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null, 
        options, options[2]);
  }

  public static void showError(Component parent, String resId, Object[] msgParams) {
    showError(parent, resId, msgParams, internal);
  }
  
  public static void showError(Component parent, String resId, Object[] msgParams, boolean inFrame) {
    CssResourceMan rsc = new CssResourceMan(resId);
    String title = rsc.getString("title");
    String msg = rsc.getString("msg");
    if (msgParams != null) {
      MessageFormat fmt = new MessageFormat(msg);
      msg = fmt.format(msgParams);
    }
    System.err.println(msg);
    if (inFrame)
      showInternalMessageDialog(parent, msg, title, ERROR_MESSAGE);
    else 
      showMessageDialog(parent, msg, title, ERROR_MESSAGE);
  }
  
  public static int showErrorHelp(Component parent, String resId, Object[] msgParams) {
    return showErrorHelp(parent, resId, msgParams, internal);
  }
  
  public static int showErrorHelp(Component parent, String resPrefix, Object[] msgParams, boolean inFrame) {
    CssResourceMan rsc = new CssResourceMan(resPrefix);
    String title = rsc.getString("title");
    String msg   = rsc.getString("msg");
    String ok    = rsc.getString("ok");
    String help  = rsc.getString("help");
    if (msgParams != null) {
      MessageFormat fmt = new MessageFormat(msg);
      msg = fmt.format(msgParams);
    }
    Object[] options = {ok, help};
    if (inFrame)
      return showInternalOptionDialog(parent, msg, title, 
        OK_CANCEL_OPTION, ERROR_MESSAGE, null, 
        options, options[0]);
    else
      return showOptionDialog(parent, msg, title, 
        OK_CANCEL_OPTION, ERROR_MESSAGE, null, 
        options, options[0]);
  }
  
  public static int showMessageHelp(Component parent, String resId, Object[] msgParams) {
    return showMessageHelp(parent, resId, msgParams, internal);
  }
  
  public static int showMessageHelp(Component parent, String resPrefix, Object[] msgParams, boolean inFrame) {
    CssResourceMan rsc = new CssResourceMan(resPrefix);
    String title = rsc.getString("title");
    String msg   = rsc.getString("msg");
    String ok    = rsc.getString("ok");
    String help  = rsc.getString("help");
    if (msgParams != null) {
      MessageFormat fmt = new MessageFormat(msg);
      msg = fmt.format(msgParams);
    }
    Object[] options = {ok, help};
    if (inFrame)
      return showInternalOptionDialog(parent, msg, title, 
        YES_NO_OPTION, INFORMATION_MESSAGE, null, 
        options, options[0]);
    else
      return showOptionDialog(parent, msg, title, 
        YES_NO_OPTION, INFORMATION_MESSAGE, null, 
        options, options[0]);
  }
}
