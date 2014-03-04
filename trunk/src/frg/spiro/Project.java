/*
 * Project.java
 *
 * Created on 29 June 2005, 14:20
 */

package frg.spiro;

import java.awt.geom.*;
import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import frg.xml.*;
import frg.util.NumberUtils;

/**
 * Spiroman project configuration
 * @author  fgrebenicek
 */
public class Project implements Operator {
  protected File file;
  Loader loader = new Loader();
  Walker walker = null;
  ElementAdapter generator;
  ElementAdapter shape;
  ElementAdapter editor;
  
  Vector opChain = new Vector();
  int steps = 360;
  
  /** Creates a new instance of Configuration */
  public Project(File projectFile) {
    this.file = projectFile;
  }
  
  public void load() throws IOException {
    try {
      walker = new Walker(loader.load(file));
      init();
    }
    catch (org.xml.sax.SAXException sax) {
      throw new IOException(sax.getMessage());
    }
  }
  
  public void loadResource(String uri) throws IOException {
    try {
      walker = new Walker(loader.load(getClass().getResourceAsStream(uri), null));
      init();
    }
    catch (org.xml.sax.SAXException sax) {
      throw new IOException(sax.getMessage());
    }
  }
  
  private void init() {
    // common settings
    shape  = findAdapter("shape");
    generator  = findAdapter("generator");
    editor = findAdapter("editor");
    if (generator != null)
      steps = NumberUtils.parseInt(generator.getAttribute("steps"), steps);
    // operators
    opChain.removeAllElements();
    Element op = walker.findElement("operator");
    while (op != null) {
      ElementAdapter elem = new ElementAdapter(op);
      if (elem.getAttribute("type").equals("rotor"))
        opChain.add(new Rotor(elem));
      op = Walker.getNextNamesake(op);
    }
  }
  
  public ElementAdapter findAdapter(String tag) {
    if (walker != null) {
      Element elem = walker.findElement(tag);
      if (elem == null) {
        elem = walker.createTag(tag);
        walker.document.getDocumentElement().appendChild(elem);
      }
      return new ElementAdapter(elem);
    }
    return null;
  }
  
  public void save() throws IOException {
    loader.save(walker.document, file);
  }
  
  public void saveAs(File file) throws IOException {
    this.file = file;
    save();
  }
  
  public int getSteps() {
    return steps;
  }
  
  public void setSteps(int num) {
    this.steps = num;
    if (generator != null)
      generator.setAttribute("steps", Integer.toString(num));
  }
  
  public String getEditorType() {
    if (editor != null)
      return editor.getAttribute("type");
    else
      return "?";
  }
  
  public void setEditorType(String type) {
    if (editor != null)
      editor.setAttribute("type", type);
  }
  
  public ElementAdapter getShapeConfig() {
    return shape;
  }
  
  public Operator getOperator(int index) {
    if (0 <= index && index < opChain.size())
      return (Operator) opChain.elementAt(index);
    else
      return null;
  }
  
  public int getOperatorCount() {
    return opChain.size();
  }
  
  public Point2D transform(Point2D point, double parameter) {
    Point2D result = point;
    for (Enumeration en=opChain.elements(); en.hasMoreElements(); ) {
      Operator op = (Operator) en.nextElement();
      result = op.transform(result, parameter);
    }
    return result;
  }
  
  
  
  public GeneralPath generatePath() {
    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, steps);
    double twoPi = 2*Math.PI; 
    double step = twoPi / steps;
    Point2D origin = new Point2D.Double(0,0);
    Point2D pen = transform(origin, 0);
    path.moveTo((float) pen.getX(), (float) pen.getY());
    for (double angle=step; angle < twoPi; angle += step) {
      pen = transform(origin, angle);
      path.lineTo((float) pen.getX(), (float) pen.getY());
    }
    pen = transform(origin, 0); // forced close path
    path.lineTo((float) pen.getX(), (float) pen.getY());
    path.closePath();
    return path;
  }
}
