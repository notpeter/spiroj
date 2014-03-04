/*
 * Rotor.java
 *
 * Created on 29 June 2005, 13:20
 */

package frg.spiro;

import java.awt.geom.Point2D;
import frg.xml.ElementAdapter;
import frg.util.NumberUtils;

/**
 * Flexible rotation operator.
 * @author  fgrebenicek
 */
public class Rotor implements Operator {
  
  private double radiusX = 100.0;  
  private double radiusY = radiusX;
  private double frequencyX = 1.0;
  private double frequencyY = frequencyX;
  
  private ElementAdapter config;
  
  /** Creates a new instance of Rotor */
  public Rotor() {}
  
  public Rotor(ElementAdapter config) {
    this();
    setConfig(config);
  }
  
  /** Do the rotation with the given point.
   * @param point input point
   * @angle rotation angle
   * @return new point with transformed coordinates
   */
  public Point2D transform(Point2D point, double angle) {
    double x = point.getX() + radiusX * Math.cos(frequencyX*angle);
    double y = point.getY() + radiusY * Math.sin(frequencyY*angle);
    return new Point2D.Double(x, y);
  }
  
  public void setConfig(ElementAdapter config) {
    this.config = config;
    radiusX = NumberUtils.parseDouble(config.getAttribute("radius", "x"), radiusX);
    radiusY = NumberUtils.parseDouble(config.getAttribute("radius", "y"), radiusY);
    frequencyX = NumberUtils.parseDouble(config.getAttribute("frequency", "x"), frequencyX);
    frequencyY = NumberUtils.parseDouble(config.getAttribute("frequency", "y"), frequencyY);
  }
  
  public ElementAdapter getConfig() {
    return config;
  }

  
  /**
   * Getter for property radiusX.
   * @return Value of property radiusX.
   */
  public double getRadiusX() {
    return radiusX;
  }
  
  /**
   * Setter for property radiusX.
   * @param radiusX New value of property radiusX.
   */
  public void setRadiusX(double radiusX) {
    this.radiusX = radiusX;
    if (config != null)
      config.setAttribute("radius", "x", Double.toString(radiusX));
  }
  
  /**
   * Getter for property radiusY.
   * @return Value of property radiusY.
   */
  public double getRadiusY() {
    return this.radiusY;
  }
  
  /**
   * Setter for property radiusY.
   * @param radiusY New value of property radiusY.
   */
  public void setRadiusY(double radiusY) {
    this.radiusY = radiusY;
    if (config != null)
      config.setAttribute("radius", "y", Double.toString(radiusY));
  }
  
  /**
   * Getter for property frequencyX.
   * @return Value of property frequencyX.
   */
  public double getFrequencyX() {
    return this.frequencyX;
  }
  
  /**
   * Setter for property frequencyX.
   * @param frequencyX New value of property frequencyX.
   */
  public void setFrequencyX(double frequencyX) {
    this.frequencyX = frequencyX;
    if (config != null)
      config.setAttribute("frequency", "x", Double.toString(frequencyX));
  }
  
  /**
   * Getter for property frequencyY.
   * @return Value of property frequencyY.
   */
  public double getFrequencyY() {
    return this.frequencyY;
  }
  
  /**
   * Setter for property frequencyY.
   * @param frequencyY New value of property frequencyY.
   */
  public void setFrequencyY(double frequencyY) {
    this.frequencyY = frequencyY;
    if (config != null)
      config.setAttribute("frequency", "y", Double.toString(frequencyY));
  }
}
