/*
 * Exporter.java
 *
 * Created on 11 July 2005, 16:02
 */

package frg.spiro;

import java.io.*;

/**
 * Exporter interface.
 * @author  fgrebenicek
 */
public abstract class Exporter extends javax.swing.filechooser.FileFilter {
  
  /** Returns file format extension.
   * @return extension, e.g. "EPS"
   */
  public abstract String getExtension();
  
  /** Returns file format description.
   * @return description, e.g. "Encapsulated PostScript"
   */
  public abstract String getDescription();
  
  /** Main export method.
   * @param drawing shape to export
   * @param out output file
   */
  public abstract void export(Drawing drawing, File out)
  throws IOException;
  
  public boolean accept(File file) {
    if (file.isDirectory() || file.getName().toLowerCase().endsWith(getExtension()))
      return true;
    return false;
  }
  
}
