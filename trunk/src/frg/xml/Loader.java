/*
 * Loader.java
 *
 * Created on 26 January 2005, 13:21
 */

package frg.xml;

import java.io.*;
import java.lang.reflect.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;


/**
 * Maximally simplified XML loading.
 * Also supports saving, but in deprecated way.
 * @author  FGrebenicek
 */
public class Loader {
  
  DocumentBuilderFactory factory;
  DocumentBuilder builder;
  
  /** 
   * Creates instance of Loader.
   * Initializes internal factory and builder.
   */
  public Loader() { 
    try {
      factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      builder = factory.newDocumentBuilder();
    }
    catch (Exception ex) {
      System.err.println("XML Loader: "+ex);
    }
  }
  
  /**
   * Loads XML document from a given URI.
   * @param uri xml document location
   * @throws IOException if an I/O error occurs
   * @throws SAXException if an SAX parser error occurs
   * @return DOM document tree
   */  
  public Document load(String uri) throws IOException, SAXException {
    Document doc = builder.parse(uri);
    doc.normalize();
    return doc;
  }
  
  /**
   * Loads XML document from a given InputStream.
   * @param is input stream
   * @param encoding forced encoding; <code>null</code> value means default encoding
   * @throws IOException if an I/O error occurs
   * @throws SAXException if an SAX parser error occurs
   * @return DOM document tree
   */  
  public Document load(InputStream is, String encoding) throws IOException, SAXException {
    InputSource input = new InputSource(is);
    if (encoding != null)
      input.setEncoding(encoding);
    Document doc = builder.parse(input);
    doc.normalize();
    return doc;
  }
  
  /**
   * Loads XML document from a given file.
   * @param file document file
   * @throws IOException if an I/O error occurs
   * @throws SAXException if an SAX parser error occurs
   * @return DOM document tree
   */  
  public Document load(File file) throws IOException, SAXException {
    Document doc = builder.parse(file);
    doc.normalize();
    return doc;
  }
  
  /**
   * Saves DOM document to the given output stream.
   * <B>Note:</B> works only with <CODE>org.apache.crimson.tree.XmlDocument</CODE>
   * @param doc must be instance of <CODE>org.apache.crimson.tree.XmlDocument</CODE>
   * @param os output stream
   * @throws IOException when I/O error occures <B>OR</B> doc class doesn't match
   */  
  public void save(Document doc, OutputStream os) throws IOException {
    /*/
    try {
      Method write = doc.getClass().getDeclaredMethod("write", new Class[] {OutputStream.class});
      write.invoke(doc, new Object[] {os});
    }
    catch (NoSuchMethodException noMethod) {
      throw new IOException("Write method not implemented!");
    }
    catch (InvocationTargetException ite) {
      if (ite.getTargetException() instanceof IOException)
        throw (IOException) ite.getTargetException();
      else
        throw new IOException(ite.toString());
    }
    catch (IllegalAccessException iax) {} 
    /*
    if (doc instanceof org.apache.crimson.tree.XmlDocument) {
      org.apache.crimson.tree.XmlDocument xdoc = 
        (org.apache.crimson.tree.XmlDocument) doc;
      org.apache.crimson.tree.XmlWriteContext context = 
        xdoc.createWriteContext(new OutputStreamWriter(os, "UTF8"));
      xdoc.writeXml(context);
      context.getWriter().flush();
      // xdoc.write(new OutputStreamWriter(os, "UTF-8"), "UTF-8"); // too many blank lines!
    }
    else {
      throw new IOException("Sorry, save not supported for: "+doc.getClass().getName());
    }
    /**/
    XmlWriter writer = new XmlWriter(os);
    writer.write(doc);
    /**/
  }
  
  /**
   * Saves DOM document to the given output stream.
   * <B>Note:</B> works only with <CODE>org.apache.crimson.tree.XmlDocument</CODE>
   * @param doc must be instance of <CODE>org.apache.crimson.tree.XmlDocument</CODE>
   * @param file output file
   * @throws IOException when I/O error occures <B>OR</B> doc class doesn't match
   */  
  public void save(Document doc, File file) throws IOException {
    FileOutputStream out = new FileOutputStream(file);
    try { save(doc, out); }
    finally { out.close(); }
  }
  
  /**
   * XML Loader autotest
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Loader loader = new Loader();
    System.out.println(loader.getClass().getName());
    if (args.length > 0) {
      System.out.println("Loading: "+args[0]);
      try {
        Document doc = loader.load(new File(args[0]));
        System.out.println(doc.getClass().getName());
        if (args.length > 1) {
          System.out.println("Saving: "+args[1]);
          loader.save(doc, new File(args[1]));
        }
        else {
          System.out.println();
          loader.save(doc, System.out);
        }
      }
      catch (Exception ex) {
        System.err.println(ex);
      }
    }
  }
}
