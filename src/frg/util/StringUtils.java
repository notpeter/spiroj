/*
 * StringUtils.java
 *
 * Created on 30. leden 2003, 13:48
 */

package frg.util;

import java.util.*;

/**
 * Some usefull string itilities
 * @author  Franta
 * @version 1.0
 */
public class StringUtils {
  
  /** Pomocná rutina pro zám?nu c --> seq.
   * Zam?ní znak <CODE>c</CODE> v ?et?zci <CODE>str</CODE> za ?et?zec <CODE>seq</CODE>.
   * @param str vstupní ?et?zec
   * @param c znak k nahrazení
   * @param seq sekvence, kterou se nahrazuje
   * @return ?et?zec s provedenou zám?nou
   */
  public static String replace(String str, char c, String seq) {
    String result = "";
    int start = 0;
    int end = str.indexOf(c);
    while (end >=0) {
      if (end > start)
        result += str.substring(start, end);
      start = end + 1;
      result += seq;
      end = str.indexOf(c, start);
    }
    if (start < str.length()) 
      result += str.substring(start);
    return result;
  }
  
  /** Pomocná rutina pro zám?nu seq --> c.
   * Zam?ní ?et?zec <CODE>seq</CODE> v ?et?zci <CODE>str</CODE> za znak <CODE>c</CODE>.
   * @param str vstupní ?et?zec
   * @param seq sekvence k nahrazení
   * @param c znak, kterým se nahrazuje 
   * @return ?et?zec s provedenou zám?nou
   */
  public static String replace(String str, String seq, char c) {
    String result = "";
    int start = 0;
    int end = str.indexOf(seq);
    while (end >=0) {
      if (end > start)
        result += str.substring(start, end);
      start = end + seq.length();
      result += c;
      end = str.indexOf(seq, start);
    }
    if (start < str.length()) 
      result += str.substring(start);
    return result;
  }

  /** Pomocná rutina pro zám?nu seq1 --> seq2.
   * Zam?ní ?et?zec <CODE>seq1</CODE> v ?et?zci <CODE>str</CODE> za ?et?zec <CODE>seq2</CODE>.
   * @param str vstupní ?et?zec
   * @param seq1 sekvence k nahrazení
   * @param seq2 ?et?zec, kterým se nahrazuje 
   * @return ?et?zec s provedenou zám?nou
   */
  public static String replace(String str, String seq1, String seq2) {
    String result = "";
    int start = 0;
    int end = str.indexOf(seq1);
    while (end >=0) {
      if (end > start)
        result += str.substring(start, end);
      start = end + seq1.length();
      result += seq2;
      end = str.indexOf(seq1, start);
    }
    if (start < str.length()) 
      result += str.substring(start);
    return result;
  }

  /**
   * Expands an expression. A subexpression like ${foo} will be replaced
   * with the "foo" system property.
   */
  public static String expand(String exp) {
    if (exp == null)
      return null;
    int index=0;
    while((index = exp.indexOf("${",index)) >=0 ) {
      int end = exp.indexOf("}",index);
      String key   = exp.substring(index+2, end);
      String value = System.getProperty(key);
      if (value == null) 
        value="${"+key+"}";
      exp = exp.substring(0,index) + value + exp.substring(end+1);
      index += value.length();
    }
    // replace the separator char
    //exp=exp.replace('/',File.separatorChar);
    return exp;
  }
  
  
  /** Fills spaces on the left side, if necessary.
   */
  public static String fill(String str, int length, char space) {
    int diff = length - str.length();
    if (diff <= 0)
      return str;
    char[] buf = new char[diff];
    for (int i=0; i<buf.length; i++)
      buf[i] = space;
    return new String(buf)+str;
  }
  
  /** Bounds the string to the quote chars
   */
  public static String quote(String str, char qchar) {
    return qchar + str + qchar;
  }
  
  /** Bounds the string to the quote chars
   */
  public static String quote(String str) {
    return quote(str, '"');
  }
  
  /** Removes bounding quotes if the string is quoted.
   */
  public static String unquote(String str, char qchar) {
    if (str == null || str.length() < 2)
      return str;
    if (str.charAt(0) == qchar && str.charAt(str.length()-1) == qchar)
      return str.substring(1, str.length()-1);
    else
      return str;
  }
  
  public static String unquote(String str) {
    return unquote(str, '"');
  }
  
  /** Makes the first letter upercase.
   */
  public static String toTitleCase(String str) {
    if (str != null && str.length() > 0)
      return Character.toUpperCase(str.charAt(0))+str.substring(1);
    else
      return str;
  }
  
    /** Split text to array of tokens according to given delimiters.
   * @param text string to split
   * @param delims delimiter characters
   * @return array of tokens
   */
  public static String[] split(String text, String delims) {
    StringTokenizer lex = new StringTokenizer(text, delims);
    String[] result = new String[lex.countTokens()];
    for (int i=0; i<result.length; i++) 
      result[i] = lex.nextToken();
    return result;
  }
  
  /** Split text to array of tokens using the default StringTokenizer.
   * @param text string to split
   * @return array of tokens
   */
  public static String[] split(String text) {
    StringTokenizer lex = new StringTokenizer(text);
    String[] result = new String[lex.countTokens()];
    for (int i=0; i<result.length; i++) 
      result[i] = lex.nextToken();
    return result;
  }
  
  /** Concatenate given tokens into a single string
   *  using given delimiter string.
   * @param tokens array of tokens
   * @param delim delimiter/separator
   * @return string of tokens separated by delimiter
   */
  public static String join(String[] tokens, String delim) {
    StringBuffer buf = new StringBuffer();
    for (int i=0; i<tokens.length; i++) {
      if (i > 0)
        buf.append(delim);
      buf.append(tokens[i]);
    }
    return buf.toString();
  }
  
  /** Concatenate given tokens into a single string.
   * @param tokens array of tokens
   * @return string of tokens separated by single space
   */
  public static String join(String[] tokens) {
    return join(tokens, " ");
  }
  
  /**
   * Lists a vector elements to a string.
   * @param vec vector to list
   * @param prefix string to append before each element
   * @param postfix string to append after each element
   * @return result string
   */
  public static String vectorToString(Vector vec, String prefix, String postfix) {
    StringBuffer buf = new StringBuffer();
    for (Enumeration enum=vec.elements(); enum.hasMoreElements(); ) {
      buf.append(prefix);
      buf.append(enum.nextElement());
      buf.append(postfix);
    }
    return buf.toString();
  }
}
