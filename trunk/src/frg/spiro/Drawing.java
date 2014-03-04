/*
 * Drawing.java
 *
 * Created on 30 June 2005, 12:20
 */

package frg.spiro;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import frg.xml.ElementAdapter;
import frg.util.NumberUtils;

/**
 * Displays a shape provided by Project.
 * @author  fgrebenicek
 */
public class Drawing extends JComponent {
  ElementAdapter config;
  float lineWidth = 1;
  Color lineColor = Color.BLACK;
  Color fillColor = Color.WHITE;
  Shape shape;
  
  /** Creates a new instance of Drawing */
  public Drawing() {
    setBackground(Color.WHITE);
    setMinimumSize(new Dimension(100,100));
    setPreferredSize(new Dimension(400, 400));
  }
  
  public void setConfig(ElementAdapter config) {
    this.config = config;
    lineWidth = (float) NumberUtils.parseDouble(config.getAttribute("linewidth"), lineWidth);
    lineColor = NumberUtils.parseRgbColor(config.getAttribute("linecolor"), lineColor);
    fillColor = NumberUtils.parseRgbColor(config.getAttribute("fillcolor"), fillColor);
  }
  
  /**
   * Getter for property lineWidth.
   * @return Value of property lineWidth.
   */
  public float getLineWidth() {
    return this.lineWidth;
  }  
  
  /**
   * Setter for property lineWidth.
   * @param lineWidth New value of property lineWidth.
   */
  public void setLineWidth(float lineWidth) {
    this.lineWidth = lineWidth;
    config.setAttribute("linewidth", Float.toString(lineWidth));
    repaint();
  }
  
  /**
   * Getter for property lineColor.
   * @return Value of property lineColor.
   */
  public Color getLineColor() {
    return this.lineColor;
  }
  
  /**
   * Setter for property lineColor.
   * @param lineColor New value of property lineColor.
   */
  public void setLineColor(Color lineColor) {
    this.lineColor = lineColor;
    config.setAttribute("linecolor", NumberUtils.colorToHexString(lineColor));
    repaint();
  }
  
  /**
   * Getter for property fillColor.
   * @return Value of property fillColor.
   */
  public Color getFillColor() {
    return this.fillColor;
  }  
  
  /**
   * Setter for property fillColor.
   * @param fillColor New value of property fillColor.
   */
  public void setFillColor(Color fillColor) {
    this.fillColor = fillColor;
    config.setAttribute("fillcolor", NumberUtils.colorToHexString(fillColor));
    repaint();
  }
  
  public void setShape(Shape shape) {
    this.shape = shape;
    repaint();
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // Painting
  
  protected Graphics2D setupGraphics(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    // g2.setBackground(getBackground());
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setStroke(new BasicStroke(lineWidth));
    return g2;
  }
  
  protected AffineTransform computeTransform() {
    AffineTransform trans = new AffineTransform();
    Rectangle2D bounds = shape.getBounds();
    double width = bounds.getWidth() * 1.1;
    double height = bounds.getHeight() * 1.1;
    Dimension dim = getSize();
    Insets insets = getInsets();
    dim.width -= insets.left + insets.right + 1;
    dim.height -= insets.top + insets.bottom + 1;
    double scale = Math.min(dim.width/width, dim.height/height);
    trans.scale(scale, scale);
    // trans.translate(insets.left*trans.getScaleX(), insets.top*trans.getScaleY());
    trans.translate(width/2, height/2);
    return trans;
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (shape != null) {
      Graphics2D g2 = setupGraphics(g);
      AffineTransform saveTrans = g2.getTransform();
      g2.transform(computeTransform());
      g2.setColor(fillColor);
      g2.fill(shape);
      if (lineWidth > 0) {
        g2.setColor(lineColor);
        g2.draw(shape);
      }
      g2.setTransform(saveTrans);
    }
  }
}
