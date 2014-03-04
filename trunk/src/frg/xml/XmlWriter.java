/*
 * XmlWriter.java
 *
 * Created on 21 July 2005, 11:30
 */

package frg.xml;

import java.io.*;
import org.w3c.dom.*;

/**
 * Simple XML writer to take around Sun ignorance.
 * Based of org.apache code.
 * @author  fgrebenicek
 */
public class XmlWriter {
  
  Writer out;
  String encoding = "UTF8";  // Java encoding
  String indent = "  ";
  int indentLevel = 0;
  
  // package private (with jdk 1.1 'javac' bug workaround)
  static /* final */ String           eol;
  static {
      String  temp;
      try { temp = System.getProperty ("line.separator", "\n"); }
      catch (SecurityException e) { temp = "\n"; }
      eol = temp;
  }
  private static final char   tagStart [] = { '<', '/' };
  private static final char   tagEnd [] = { ' ', '/', '>' };
  
  /** Creates a new instance of XmlWriter */
  public XmlWriter(OutputStream os, String encoding) {
    try { 
      out = new OutputStreamWriter(os, encoding);
    }
    catch (UnsupportedEncodingException ex) {
      System.err.println("Unsupported encoding: "+encoding);
      out = new OutputStreamWriter(os);
    }
  }
  
  public XmlWriter(OutputStream os) {
    this(os, "UTF8");
  }
  
  /**
   * Saves DOM document to the given output stream.
   * <B>Note: should work with any DOM implementation.
   * @param org.w3c.dom document</CODE>
   * @throws IOException when I/O error occures <B>OR</B> doc class doesn't match
   */  
  public void write(Document doc) throws IOException {
    out.write ("<?xml version=\"1.0\"");
    if (encoding != null) {
        out.write (" encoding=\"");
        out.write (java2std(encoding));
        out.write ('\"');
    }
    out.write ("?>");
    out.write(eol);
    out.write(eol);
    
    indentLevel = 0;
    writeElement(doc.getDocumentElement());
    
    // out.write(eol);
    out.flush();
  }
  
  protected void writeElement (Element element) throws IOException {
    writeIndent();
    out.write (tagStart, 0, 1);   // "<"
    out.write (element.getNodeName());
    
    if (element.hasAttributes()) {
       NamedNodeMap attributes = element.getAttributes();
       int length = attributes.getLength();
       for (int i=0; i<length; i++) {
         Node item = attributes.item(i);
         if (item instanceof Attr) {
           Attr attr = (Attr) item;
           if (attr.getSpecified()) {
             out.write (' ');
             writeAttribute(attr);
           }  
         }
       }
    }

    //
    // Write empty nodes as "<EMPTY />" to make sure version 3
    // and 4 web browsers can read empty tag output as HTML.
    // XML allows "<EMPTY/>" too, of course.
    //
    if (!element.hasChildNodes()) 
      out.write (tagEnd, 0, 3);   // " />"
    else  {
      boolean doIndent = (indent.length()>0) && !isSimpleTextElement(element);
      out.write (tagEnd, 2, 1);   // ">"
      if (doIndent) out.write(eol);
      NodeList list = element.getChildNodes();
      int length = list.getLength();
      indentLevel++;
      for (int i=0; i<length; i++) 
        writeNode(list.item(i));
      indentLevel--;
      if (doIndent) writeIndent();
      out.write (tagStart, 0, 2);   // "</"
      out.write (element.getNodeName());
      out.write (tagEnd, 2, 1);   // ">"
    }
    if (indent.length() > 0)
      out.write(eol);
  } // writeElement
  
  protected boolean isSimpleTextElement(Element element) {
    Node text = element.getFirstChild();
    if (text != null && text instanceof Text)
      return text.getNodeValue().trim().length() > 0;
    return false;
  }
  
  public void writeAttribute(Attr attr) throws IOException {
    out.write (attr.getName());
    out.write ("=\"");
    // writeChildrenXml (context);
    String value = attr.getValue();
    for (int i = 0; i < value.length(); i++) {
      int c = value.charAt (i);
      switch (c) {
        // XXX only a few of these are necessary; we
        // do what "Canonical XML" expects
        case '<':  out.write ("&lt;"); continue;
        case '>':  out.write ("&gt;"); continue;
        case '&':  out.write ("&amp;"); continue;
        case '\'': out.write ("&apos;"); continue;
        case '"':  out.write ("&quot;"); continue;
        default:   out.write (c); continue;
      }
    }
    out.write ('"');
  }
  
  public void writeText(Text text) throws IOException {
    // System.out.println("Text: '"+text.getData()+"'");
    String buf = text.getData();
    if (indent.length() > 0) 
      buf = buf.trim();
    char[] data = buf.toCharArray();
    int start = 0, last = 0;

    while (last < data.length) {
      char c = data [last];
      //
      // escape markup delimiters only ... and do bulk
      // writes wherever possible, for best performance
      //
      // note that character data can't have the CDATA
      // termination "]]>"; escaping ">" suffices, and
      // doing it very generally helps simple parsers
      // that may not be quite correct.
      //
      if (c == '<') {         // not legal in char data
        out.write (data, start, last - start);
        start = last + 1;
        out.write ("&lt;");
      } else if (c == '>') {      // see above
        out.write (data, start, last - start);
        start = last + 1;
        out.write ("&gt;");
      } else if (c == '&') {      // not legal in char data
        out.write (data, start, last - start);
        start = last + 1;
        out.write ("&amp;");
      }
      last++;
    }
    out.write (data, start, last - start);
  }
  
  public void writeComment(Comment comment) throws IOException {
    out.write ("<!--");
    char[] data = comment.getData().toCharArray();
    if (data != null) {
      boolean   sawDash = false;
      int      length = data.length;

      // "--" illegal in comments, expand it
      for (int i=0; i < length; i++) {
        if (data [i] == '-') {
          if (sawDash)
            out.write (' ');
          else {
            sawDash = true;
          out.write ('-');
          continue;
          }
        }
        sawDash = false;
        out.write (data [i]);
        }
      if (data [data.length - 1] == '-')
      out.write (' ');
    }
    out.write ("-->");
  }
  
  public void writeCData(CDATASection cdata) throws IOException {
    char[] data = cdata.getData().toCharArray();
    out.write ("<![CDATA[");
    for (int i = 0; i < data.length; i++) {
      char c = data [i];
      // embedded "]]>" needs to be split into adjacent
      // CDATA blocks ... can be split at either point
      if (c == ']') {
        if ((i + 2) < data.length
          && data [i + 1] == ']'
          && data [i + 2] == '>') {
            out.write ("]]]><![CDATA[");
            continue;
        }
      }
      out.write (c);
    }
   out.write ("]]>");
  }
  
  public void writeNode(Node node) throws IOException {
    switch (node.getNodeType()) {
      case Node.ATTRIBUTE_NODE:     writeAttribute((Attr) node);     break;
      case Node.ELEMENT_NODE:       writeElement((Element) node);    break;
      case Node.TEXT_NODE:          writeText((Text) node);          break;
      case Node.COMMENT_NODE:       writeComment((Comment) node);    break;
      case Node.CDATA_SECTION_NODE: writeCData((CDATASection) node); break;
      default:
        System.err.println("Writing of node type "+node.getNodeType()+" is not implemented.");
        break;
    }
  }
  
  public void writeIndent() throws IOException {
    for (int i=0; i<indentLevel; i++)
      out.write(indent);
  }
  
  //
  // Try some of the common conversions from Java's internal names
  // (which must fit in class names) to standard ones understood by
  // most other code.  We use the IETF's preferred names; case is
  // supposed to be ignored, note.
  //
  // package private 
  static String java2std (String encodingName) {
      if (encodingName == null)
          return null;

      //
      // ISO-8859-N is a common family of 8 bit encodings;
      // N=1 is the eight bit subset of UNICODE, and there
      // seem to be at least drafts for some N >10.
      //
      if (encodingName.startsWith ("ISO8859_"))       // JDK 1.2
          return "ISO-8859-" + encodingName.substring (8);
      if (encodingName.startsWith ("8859_"))          // JDK 1.1
          return "ISO-8859-" + encodingName.substring (5);
      
      // Windows codepages
      if (encodingName.toLowerCase().startsWith("cp"))
        return "windows-" + encodingName.substring(2);

      // XXX seven bit encodings ISO-2022-* ...
      // XXX EBCDIC encodings ... 

      if ("ASCII7".equalsIgnoreCase (encodingName)
              || "ASCII".equalsIgnoreCase (encodingName))
          return "US-ASCII";

      //
      // All XML parsers _must_ support UTF-8 and UTF-16.
      // (UTF-16 ~= ISO-10646-UCS-2 plus surrogate pairs)
      //
      if ("UTF8".equalsIgnoreCase (encodingName))
          return "UTF-8";
      if (encodingName.startsWith ("Unicode"))
          return "UTF-16";

      //
      // Some common Japanese character sets.
      //
      if ("SJIS".equalsIgnoreCase (encodingName))
          return "Shift_JIS";
      if ("JIS".equalsIgnoreCase (encodingName))
          return "ISO-2022-JP";
      if ("EUCJIS".equalsIgnoreCase (encodingName))
          return "EUC-JP";

      // else we can't really do anything
      return encodingName;
    }
}
