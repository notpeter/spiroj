/*
 * ExportAI.java
 *
 * Created on 12 July 2005, 12:46
 */

package frg.spiro;

import java.awt.Rectangle;
import java.awt.geom.*;
import java.io.*;

/**
 * Export to Adobe Illustrator 3 format.
 * @author  fgrebenicek
 */
public class ExportAI extends Exporter {
  
  public static final int PAGE_WIDTH = 595;
  public static final int PAGE_HEIGHT = 842;
  java.text.DecimalFormat decimal;
  
  /** Creates a new instance of ExportAI */
  public ExportAI() {
    decimal = new java.text.DecimalFormat("0.000");
    decimal.setDecimalFormatSymbols(new java.text.DecimalFormatSymbols(java.util.Locale.US));
  }
  
  public String getDescription() {
    return "Adobe Illustrator 3.0 (*.ai)";
  }
  
  public String getExtension() {
    return ".ai";
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
      bounds.width += Math.floor(2*drawing.getLineWidth());
      bounds.height += Math.floor(2*drawing.getLineWidth());
      bounds.x = (PAGE_WIDTH - bounds.width)/2;
      bounds.y = (PAGE_HEIGHT - bounds.height)/2;
      writeHeader(writer, out.getName(), 
        About.getName()+" version "+About.getVersion(), bounds);
      writeProlog(writer);
      writeSetup(writer);
      writeBody(writer, drawing);
      writeTrailer(writer);
    }
    finally {
      fos.close();
    }
  }
  
  public void writeHeader(PrintWriter out, String title, String creator, Rectangle bounds) {
    out.println("%!PS-Adobe-3.0 EPSF-3.0");
    out.println("%%BoundingBox: "+bounds.x+" "+bounds.y+" "
      +(bounds.x+bounds.width)+" "+(bounds.y+bounds.height));
    out.println("%%Title: "+title);
    out.println("%%Creator: "+creator);
    out.println("%%EndComments");
  }
  
  protected void writeProlog(PrintWriter out) {
    out.println("%%EndProlog");
  }
  
  protected void writeSetup(PrintWriter out) {
    out.println("%%BeginSetup");
    out.println("%%EndSetup"); 
  }
  
  protected void writeTrailer(PrintWriter out) {
    out.println("%%Trailer");
    out.println("%%EOF");
  }
  
  protected void writeBody(PrintWriter out, Drawing drawing) {
    writeCmykColor(out, drawing.getFillColor(), "k");
    writeCmykColor(out, drawing.getLineColor(), "K");
    out.println(drawing.getLineWidth()+" w");
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
            writeCoords(out, coords, 2, "m");
            break;
          case PathIterator.SEG_LINETO:
            writeCoords(out, coords, 2, "l");
            break;
          case PathIterator.SEG_CUBICTO:
            writeCoords(out, coords, 6, "c");
            break;
          case PathIterator.SEG_QUADTO:
            writeCoords(out, coords, 4, "v");
            break;  
        }
        iter.next();
      } 
      out.println("b");
    }
  }
  
  protected void writeCoords(PrintWriter out, double[] coords, int count, String cmd) {
    for (int i=0; i<count && i<coords.length; i++)
      out.print(decimal.format(coords[i])+" ");
    out.println(cmd);
  }
  
  protected void writeCmykColor(PrintWriter out, java.awt.Color color, String cmd) {
    out.print(decimal.format(1 - color.getRed()/255.0) + " ");
    out.print(decimal.format(1 - color.getGreen()/255.0) + " ");
    out.print(decimal.format(1 - color.getBlue()/255.0) + " ");
    out.println("0 "+cmd);
  }
  
}
