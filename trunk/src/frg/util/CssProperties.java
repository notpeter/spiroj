/*
 * CssProperties.java
 *
 * Created on March 31, 2004, 2:31 PM
 */

package frg.util;

import java.io.*;
import java.util.*;

/**
 * Cascading Style Sheet support.
 * @author  Franta
 * @version 1
 */
public class CssProperties extends Object {

  Properties tags = new Properties();
  StreamTokenizer lex;
  String lastError;
  
  /** Creates new CssProperties */
  public CssProperties() {
  }
  
  public Properties getTag(String tag) {
    return (Properties) tags.get(tag);
  }
  
  public String getTagProperty(String tag, String name, String defval) {
    Properties prop = getTag(tag);
    if (prop == null)
      return defval;
    else
      return prop.getProperty(name, defval);
  }
  
  public String getTagProperty(String tag, String name) {
    return getTagProperty(tag, name, null);
  }
  
  public String getCascadeProperty(String cascade, String name, String defval) {
    StringTokenizer stk = new StringTokenizer(cascade, ",");
    String result = defval;
    while (stk.hasMoreTokens()) 
      result = getTagProperty(stk.nextToken(), name, result);
    return result;
  }
  
  public String getCascadeProperty(String cascade, String name) {
    return getCascadeProperty(cascade, name, null);
  }
  
  public Enumeration getTags() {
    return tags.keys();
  }
  /////////////////////////////////////////////////////////////////////////////
  // Parser
  
  public void load(InputStream is) throws IOException {
    parse(new BufferedReader(new InputStreamReader(is)));
  }

  public void parse(Reader reader) throws IOException {
    lex = new StreamTokenizer(reader);
    lex.resetSyntax();
    lex.wordChars('a', 'z');
	  lex.wordChars('A', 'Z');
	  lex.wordChars('0', '9');
    lex.wordChars(33, 47);
    lex.wordChars('-', '-');
    lex.wordChars('.', '.');
    lex.wordChars('#', '#');
    lex.wordChars('$', '$');
    lex.wordChars('_','_');
    lex.wordChars(128 + 32, 255);
	  lex.whitespaceChars(0, ' ');
    lex.ordinaryChar('{');
    lex.ordinaryChar('}');
    lex.ordinaryChar(';');
    lex.ordinaryChar(',');
    lex.ordinaryChar(':');
    lex.quoteChar('"');
    lex.commentChar('/');
    lex.slashStarComments(true); // does not work when '/' is not comment char
    // lex.slashSlashComments(true);
    parse();
    // reader.close();
  }
  
  protected void parse() throws IOException {
    tags.clear();
    for (lex.nextToken(); lex.ttype != lex.TT_EOF; lex.nextToken()) {
      lex.pushBack();
      parseTag();
    }
  }
  
  protected void parseTag() throws IOException {
    String selector = aggregate('{');
    Properties prop = new Properties();
    while (lex.ttype != '}') 
      parseNameNext(prop);
    StringTokenizer stk = new StringTokenizer(selector, ",");
    while (stk.hasMoreTokens()) 
      putTag(stk.nextToken(), prop);
  }
  
  protected void putTag(String tag, Properties prop) {
    Properties val = (Properties) tags.get(tag);
    if (val == null)
      tags.put(tag, prop);
    else
      val.putAll(prop);
  }
  
  protected void parseNameNext(Properties prop) throws IOException {
    String name = null, val = null;
    for (;;) {
      lex.nextToken(); // skipComment();
      if (val == null) {
        switch (lex.ttype) {
          case StreamTokenizer.TT_WORD:
            if (name == null)
              name = lex.sval;
            else 
              name += lex.sval;
            break;
          case StreamTokenizer.TT_EOL:
            break;
          case StreamTokenizer.TT_EOF:
            error("Unexpected end of file");
            break;
          case ':':
            val = "";
            break;
          case '{':
          case '}':
            if (name == null)
              return;
            else
              error("Unexpected ordinal, name="+name);
            break;
          default:
            if (name == null)
              name = ""+((char)lex.ttype);
            else
              name += ""+((char)lex.ttype);
            break;
        } // switch
      } // if val == null
      else { // parse value
        switch (lex.ttype) {
          case StreamTokenizer.TT_WORD:
            if (val.length() == 0)
              val = lex.sval;
            else
              val += " "+lex.sval;
            break;
          case StreamTokenizer.TT_EOL:
            break;
          case StreamTokenizer.TT_EOF:
            error("Unexpected end of file");
            break;
          case '"':  
              val += '"'+lex.sval+'"';
            break;
          case '}':
          case ';':
            prop.put(name, val);
            // name = val = null;
            return;
          case '{':
            error("Unexpected ordinal");
            break;
          default:
            val += ""+((char)lex.ttype);
            break;
        } // switch
      }
    }
  }
  
  protected void look(int token) throws IOException {
    do {
      lex.nextToken(); // skipComment();
    } while (lex.ttype != token);
  }
  
  protected String aggregate(int token) throws IOException {
    StringBuffer buf = new StringBuffer();
    for(;;) {
      lex.nextToken(); // skipComment();
      if (lex.ttype == token)
        break;
      switch (lex.ttype) {
        case StreamTokenizer.TT_WORD: buf.append(lex.sval); break;
        case StreamTokenizer.TT_NUMBER: buf.append((int)lex.nval); break;
        case '"': buf.append("\""+lex.sval+"\""); break;
        default: buf.append((char)lex.ttype); break;
      }
    } 
    return buf.toString();
  }

  protected void expect(int token) throws IOException {
    lex.nextToken(); // skipComment();
    check(token);
  }
  
  protected void check(int token) throws IOException {
    if (token != lex.ttype) {
      String tstr = "";
      switch (token) {
        case StreamTokenizer.TT_EOF: tstr += "End of file"; break;
        case StreamTokenizer.TT_EOL: tstr += "End of line"; break;
        case StreamTokenizer.TT_NUMBER: tstr += "Number"; break;
        case StreamTokenizer.TT_WORD: tstr += "Identifier"; break;
        default:
          tstr += "'"+((char) token)+"'";
          break;
      }
      error(tstr+" expected.");
    }
  }

  public void error(String msg) throws IOException {
    lastError = 
      "CSS parser error ("+lex.toString()+"): "+msg;
    // System.out.println(lastError);
    throw new IOException(lastError);
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // Writer
 
  public void store(OutputStream os) throws IOException {
    store(new PrintStream(os, true));
  }
  
  public void store(PrintStream writer) throws IOException {
    Vector tagList = new Vector(tags.keySet());
    Collections.sort(tagList);
    for (Enumeration enum=tagList.elements(); enum.hasMoreElements(); ) {
      String selector = enum.nextElement().toString();
      writer.println(selector + " {");
      storeTag(writer, (Properties) tags.get(selector));
      writer.println('}');
    }
  }
  
  protected void storeTag(PrintStream writer, Properties tag) {
    if (tag == null)
      return;
    Vector list = new Vector(tag.keySet());
    Collections.sort(list);
    for (Enumeration enum=list.elements(); enum.hasMoreElements(); ) {
      String key = enum.nextElement().toString();
      String val = tag.getProperty(key);
      writer.println("  "+key+": "+val+";");
    }
  }

  public void list(PrintStream writer) {
    try { store(writer); }
    catch (Exception _) {}
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // Validator
  
  public static void main(String args[]) throws IOException {
    InputStream is = System.in;
    PrintStream out = System.out;
    if (args.length > 0)
      is = new FileInputStream(args[0]);
    if (args.length > 1)
      out = new PrintStream(new FileOutputStream(args[1]));
    CssProperties css = new CssProperties();
    css.load(is);
    css.list(out);
  }
}
