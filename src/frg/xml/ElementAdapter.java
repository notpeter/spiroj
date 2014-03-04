/*
 * ElementAdapter.java
 *
 * Created on 2002-06-07 14:00
 * Modified on 2005-01-04
 *  adapted to JRE 1.4
 *
 */

package frg.xml;

import java.awt.datatransfer.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
// import com.sun.xml.tree.XmlDocument;
import org.apache.crimson.tree.XmlDocument;

/**
 * Adapter for DOM Element handling.
 * @author Franta
 * @version 1.0
 * @todo Replace calling of XmlDocument methods, etc. <CODE>changeNodeOwner()</CODE>
 */

public class ElementAdapter extends java.lang.Object {
  
  public Element element;
  
  /** Creates new ElementAdapter */
  public ElementAdapter(Element elem) {
    element = elem;
  }
  
  public String getText(String tagName) {
    Element tag = Walker.getSubElement(element, tagName);
    if (tag != null)
      return Walker.getText(tag);
    else
      return "";
  }
  
  public void setText(String tagName, String value) {
    Element tag = Walker.getSubElement(element, tagName);
    if (tag == null) {
      tag = element.getOwnerDocument().createElement(tagName);
      element.appendChild(tag);
    }
    Walker.setText(tag, value);  
  }
  
  public boolean hasText(String tagName) {
    Element tag = Walker.getSubElement(element, tagName);
    return (tag != null);
  }

  public String getAttribute(String attName) {
    return element.getAttribute(attName);
  }
  
  public String getAttribute(String tagName, String attName) {
    Element tag = Walker.getSubElement(element, tagName);
    if (tag != null)
      return tag.getAttribute(attName);
    else
      return "";
  }
  
  public void setAttribute(String attName, String val) {
    element.setAttribute(attName, val);
  }
  
  public void setAttribute(String tagName, String attName, String val) {
    Element tag = Walker.getSubElement(element, tagName);
    if (tag == null) {
      tag = element.getOwnerDocument().createElement(tagName);
      element.appendChild(tag);
    }
    tag.setAttribute(attName, val);
  }
  
  public boolean hasAttribute(String attName) {
    String val = element.getAttribute(attName);
    return (val != null) && (val.trim().length() > 0);
  }
  
  public boolean hasAttribute(String tagName, String attName) {
    Element tag = Walker.getSubElement(element, tagName);
    if (tag != null) {
      String val = tag.getAttribute(attName);
      return (val != null) && (val.trim().length() > 0);
    }
    return false;
  }
  
  //////////////////////
  // List edit methods
  
  public void moveUp() {
    Node parent = element.getParentNode();
    if (parent == null) return;
    Node sibling = element.getPreviousSibling();
    while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE)
      sibling = sibling.getPreviousSibling();  // search for an Element
    if (sibling != null) {
      parent.removeChild(element);
      parent.insertBefore(element, sibling);
    }
  }
  
  public void moveDown() {
    Node parent = element.getParentNode();
    if (parent == null) return;
    Node sibling = element.getNextSibling();
    while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE)
      sibling = sibling.getNextSibling();  // search for an Element
    if (sibling != null) {
      parent.removeChild(sibling);
      parent.insertBefore(sibling, element);
    }
  }
  
  public void remove() {
    Node parent = element.getParentNode();
    if (parent != null) {
      parent.removeChild(element);
    }
  }
  
  public void insertNew(Node newNode) {
    Node parent = element.getParentNode();
    if (parent == null) return;
    Node sibling = element.getNextSibling();
    while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE)
      sibling = sibling.getNextSibling();  // search for an Element
    ((XmlDocument)element.getOwnerDocument()).changeNodeOwner(newNode);
    parent.insertBefore(newNode, sibling);
  }
  
  public static void insertNew(Node parent, Node newNode) {
    if (parent == null) return;
    ((XmlDocument)parent.getOwnerDocument()).changeNodeOwner(newNode);
    parent.appendChild(newNode);
  }
  
  
  public void appendNew(Node newNode) {
    Node parent = element.getParentNode();
    if (parent != null) {
      ((XmlDocument)element.getOwnerDocument()).changeNodeOwner(newNode);
      parent.appendChild(newNode);
    }
  }  
 
  public static Element createElement(String tag) {
    // XmlDocument tempDoc = new XmlDocument();
    // return tempDoc.createElement(tag);
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document tempDoc = builder.newDocument();
      return tempDoc.createElement(tag);
    }
    catch (ParserConfigurationException e) {
      return null;
    }
  }
  
  public ElementAdapter getParent() {
    Node parent = element.getParentNode();
    if (parent != null && parent instanceof Element) 
      return new ElementAdapter((Element) parent);
    else
      return null;
  }

  /////////////////////
  // Clipboard utils
  
  public static Element getElement(Transferable content) 
  throws UnsupportedFlavorException, java.io.IOException, org.xml.sax.SAXException {
    String str = (String) content.getTransferData(DataFlavor.stringFlavor);
    java.io.StringReader reader = new java.io.StringReader(str);
    // XmlDocument tempDoc = 
    //  XmlDocument.createXmlDocument(new InputSource(reader), false);
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document tempDoc = builder.parse(new InputSource(reader));
      Element root = tempDoc.getDocumentElement();
      root.normalize();
      return root;
    }
    catch (ParserConfigurationException e) {
      return null;
    }
  }
  
  
  public static String clipboardItemList(String list, String name) {
    return "<clipboard:"+name+".list>"+list+"</clipboard:"+name+".list>";
  }
  
  public boolean isValid(String name) {
    return element.getNodeName().equals(name);
  }
  
  public boolean isValidItemList(String name) {
    return element.getNodeName().equals("clipboard:"+name+".list");
  }
  
  public Element getFirstListItem() {
    Node sibling = element.getFirstChild();
    while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE)
      sibling = sibling.getNextSibling();  // search for an Element
    return (Element) sibling;
  }
  
  public Element getNextListItem(Element item) {
    Node sibling = item.getNextSibling();
    while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE)
      sibling = sibling.getNextSibling();  // search for an Element
    return (Element) sibling;
  }
}
