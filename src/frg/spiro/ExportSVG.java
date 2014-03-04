/*
 * ExportAI.java
 *
 * Created on 12 July 2005, 12:46
 */

package frg.spiro;

import java.awt.Rectangle;
import java.awt.geom.*;
import java.io.*;

import frg.util.NumberUtils;

/**
 * Export to Adobe Illustrator 3 format.
 * @author  fgrebenicek
 */
public class ExportSVG extends Exporter {
  
  public int PAGE_WIDTH = 200;   // only default values
  public int PAGE_HEIGHT = 200;  // recalculated during export
  java.text.DecimalFormat decimal;
  
  /** Creates a new instance of ExportAI */
  public ExportSVG() {
    decimal = new java.text.DecimalFormat("0.000");
    decimal.setDecimalFormatSymbols(new java.text.DecimalFormatSymbols(java.util.Locale.US));
  }
  
  public String getDescription() {
    return "Scalable Vector Grahics (*.svg)";
  }
  
  public String getExtension() {
    return ".svg";
  }
  
  ///////////////////////////////////////////////////////////////////////////
  
  /** Do the export. 
   * It is not thread safe. Do not use this method from two threads simultaneously.
   */  
  public void export(Drawing drawing, java.io.File out) throws java.io.IOException {
    FileOutputStream fos = new FileOutputStream(out);
    try {
      PrintWriter writer = new PrintWriter(fos, true);
      Rectangle bounds = drawing.shape.getBounds();
      PAGE_WIDTH = (int) (1.1*Math.max(bounds.width, bounds.height));
      PAGE_HEIGHT = PAGE_WIDTH;
      writeSvg(writer, drawing, out.getName(), 
        About.getName()+" version "+About.getVersion());
    }
    finally {
      fos.close();
    }
  }
  
  public void writeSvg(PrintWriter out, Drawing drawing, String title, String creator) {
    out.println("<?xml version=\"1.0\"?>");
    out.println("<svg width=\"10cm\" height=\"10cm\" viewBox=\"0 0 "+
      PAGE_WIDTH+" "+PAGE_HEIGHT+"\">");
    out.println("  <title>"+title+"</title>");
    out.println("  <desc>Creator: "+creator+"</desc>");
    writeBody(out, drawing);
    out.println("</svg>");
  }
  
  protected void writeBody(PrintWriter out, Drawing drawing) {
    out.print("  <path");
    addParameter(out, "fill", NumberUtils.colorToHexString(drawing.getFillColor()));
    addParameter(out, "fill-rule", "evenodd");
    addParameter(out, "stroke", NumberUtils.colorToHexString(drawing.getLineColor()));
    addParameter(out, "stroke-width", drawing.getLineWidth()+"");
    out.print("\n    d=\"");
    if (drawing.shape instanceof GeneralPath) {
      GeneralPath gp = (GeneralPath) drawing.shape;
      Rectangle2D bounds = gp.getBounds2D();
      AffineTransform tr = AffineTransform.getTranslateInstance(
        (PAGE_WIDTH - bounds.getWidth())/2 - bounds.getX(),
        (PAGE_HEIGHT - bounds.getHeight())/2 - bounds.getY());
      PathIterator iter = gp.getPathIterator(tr);
      double[] coords = new double[6];
      while (!iter.isDone()) {
        int type = iter.currentSegment(coords);
        switch (type) {
          case PathIterator.SEG_MOVETO:
            writeCoords(out, coords, 2, "M");
            break;
          case PathIterator.SEG_LINETO:
            writeCoords(out, coords, 2, "L");
            break;
          case PathIterator.SEG_CUBICTO:
            writeCoords(out, coords, 6, "C");
            break;
          case PathIterator.SEG_QUADTO:
            writeCoords(out, coords, 4, "Q");
            break;  
        }
        iter.next();
      } 
      out.println("z\"\n  />");
    }
  }
  
  protected void addParameter(PrintWriter out, String name, String value) {
    out.print(" "+name+"=\""+value+"\"");
  }
  
  protected void writeCoords(PrintWriter out, double[] coords, int count, String cmd) {
    out.print(cmd+" ");
    for (int i=0; i<count && i<coords.length; i++)
      out.print(decimal.format(coords[i])+" ");
  }
  
}
