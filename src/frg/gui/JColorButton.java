/*
 * JColorButton.java
 *
 * Created on 11 July 2005, 09:28
 */

package frg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Color chooser button.
 * @author  fgrebenicek
 */
public class JColorButton 
extends JButton 
implements ActionListener {
  
  /**
   * Holds value of property color.
   */
  private Color color = java.awt.SystemColor.controlText;
  private Dimension size = new Dimension(24, 24);
  private JColorChooser chooser;
  private JDialog dialog;
  private String title;
  private ActionListener selectionListener;
  
  /** Creates a new instance of JColorButton */
  public JColorButton(int w, int h, String title) {
    this.title = title;
    setToolTipText(title);
    size = new java.awt.Dimension(w, h);
    chooser = new JColorChooser(color);
    dialog = JColorChooser.createDialog(this, title, true, 
      chooser, new OkListener(), null);
    addActionListener(this);
  }
  
  public JColorButton(String title) {
    this(16, 16, title);
  }
  
  public JColorButton() {
    this("Choose color");
  }
  
  public void setSelectionListener(ActionListener listener) {
    this.selectionListener = listener;
  }
  
  public Dimension getPreferredSize() {
    java.awt.Dimension dim = super.getPreferredSize();
    dim.width  = Math.max(dim.width, size.width);
    dim.height = Math.max(dim.height, size.height);
    return dim;
  }
    
  /**
   * Getter for property color.
   * @return Value of property color.
   */
  public Color getColor() {
    return this.color;
  }  
  
  /**
   * Setter for property color.
   * @param color New value of property color.
   */
  public void setColor(Color color) {
    this.color = color;
    chooser.setColor(color);
    repaint();
  }  
  
  public void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);
    g.setColor(color);
    g.fillRect(0, 0, getWidth(), getHeight());
  }
  
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if (e.getSource() == this) {
      dialog.show();
    }
  }
  
  class OkListener implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent e) {
      color = chooser.getColor();
      repaint();
      if (selectionListener != null)
        selectionListener.actionPerformed(
          new ActionEvent(JColorButton.this, e.getID(), e.getActionCommand()));
    }
  };
  
}
