/*
 * Operator.java
 *
 * Created on 29 June 2005, 13:17
 */

package frg.spiro;

import java.awt.geom.Point2D;

/**
 * Roulette operator interface
 * @author  fgrebenicek
 */
public interface Operator {
  
  /** Do the operator transformation, depending on one parameter.
   * @param point input point
   * @param parameter transformation parameter, e.g. angle for rotation
   * @return transformed point (should return new instance)
   */
  public Point2D transform(Point2D point, double parameter);
}
