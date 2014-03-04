/*
 * Walker.java
 *
 * Created on 2002-06-07, 11:14
 * Modified on 2005-01-04
 *   moved to merten package and revisioned
 */

package frg.xml;

import org.w3c.dom.*;


/**
 * XML helper for browsing XML DOM.
 * @author Franta <grebenic@cln.cz>
 * @version 2.0
 */

public class Walker extends Object {
  /** XML document defined in org.w3c.dom */
  public Document document;  

  /**
   * Creates new Walker object.
   * @param doc document you want to browse (walk)
   */
  public Walker(Document doc) {
    this.document = doc;
  }

  /**
   * Derived from Object.
   * @return short description
   */  
  public String toString() {
    return "XML helper";
  }
    

  /**
   * Searches for the first sub-element of given element with given name.
   * @param elem parent xml element
   * @param name name of child
   * @return found element or null
   */  
  public static Element getSubElement(Element elem, String name) {
    return searchSibling(elem.getFirstChild(), name);
  }
  
  /**
   * Searches for sub-element with given name and given attribute value.
   * Usefull when you a looking for xml tag with given ID.
   * @param elem parent element
   * @param name name of child element
   * @param attribute name of child attribute
   * @param value requested value of attribute
   * @return found element or <B>null</B>
   */  
  public static Element getSubElement(Element elem, String name, 
                                      String attribute, String value) {
    elem = getSubElement(elem, name);
    while (elem != null) {
      if (elem.getAttribute(attribute).equals(value))
        return elem;
      elem = getNextSibling(elem, name);
    }
    return null;
  }
  
  /**
   * Get next sibling element of given element.
   * @param elem starting element
   * @param name name of sibling element
   * @return found element or <B>null</B>
   */  
  public static Element getNextSibling(Node elem, String name) {
    return searchSibling(elem.getNextSibling(), name);
  }
  
  /**
   * Get next namesake of given element.
   * Searches for next sibling with the same name.
   * @param elem start element
   * @return found element or null
   */  
  public static Element getNextNamesake(Node elem) {
    return searchSibling(elem.getNextSibling(), elem.getNodeName());
  }
  
  /**
   * The core of walking methods. Searches for first occurrance of an element
   * starting from given node.
   * @param elem starting node (element); can be <B>null</B>
   * @param name name of searched element
   * @return found element or <B>null</B>
   */  
  public static Element searchSibling(Node elem, String name) {
    while (elem != null) {
      if ((elem.getNodeType() == Node.ELEMENT_NODE) && 
          (elem.getNodeName().equals(name)))
        return (Element) elem;
      elem = elem.getNextSibling();
    }
    return null;
  }
  
  /**
   * Get text of given element. Looks for first text subnode.
   * @param elem should be element
   * @return text of given element. If there is no text, it returns empty string.
   */  
  public static String getText(Element elem) {
    Node text = elem.getFirstChild();
    if (text != null && text instanceof Text)
      return text.getNodeValue();
    else
      return "";
  }
  
  /**
   * Sets the element text. Tries to update first textual sub-node.
   * @param elem target element
   * @param val text to set
   */  
  public static void setText(Element elem, String val) {
    Node text = elem.getFirstChild();
    if (text != null && text instanceof Text)
      text.setNodeValue(val);
    else {
      Text newText = elem.getOwnerDocument().createTextNode(val);
      if (text == null)
        elem.appendChild(newText);
      else
        elem.insertBefore(newText, text);
    }
  }
  
  /**
   * Create tag element using a given document.
   * @param doc document creating the tag
   * @param name name of tag (element)
   * @param text text for the tag (element)
   * @return new element
   */  
  public static Element createTag(Document doc, String name, String text) {
    Element tag = doc.createElement(name);
    tag.appendChild(doc.createTextNode(text));
    return tag;
  }
  
  /**
   * Create tag element using the parent document.
   * @param name name of tag (element)
   * @param text text for the tag (element)
   * @return new element
   */
  
  public Element createTag(String name, String text) {
    Element tag = document.createElement(name);
    tag.appendChild(document.createTextNode(text));
    return tag;
  }
  
  /**
   * Create tag element using the parent document.
   * @param name name of tag (element)
   * @return new element
   */
  
  public Element createTag(String name) {
    Element tag = document.createElement(name);
    return tag;
  }

  /**
   * Finds the first element with given name.
   * @param name name of element searched
   * @return found element or null
   */  
  public Element findElement(String name) {
    NodeList list = document.getElementsByTagName(name);
    for (int i=0; i<list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE)
        return (Element) node;
    }
    return null;
  }

}


