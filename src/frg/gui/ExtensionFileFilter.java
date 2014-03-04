package frg.gui;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A convenience implementation of FileFilter that filters out
 * all files except for those type extensions that it knows about.
 * @version 1 2001
 * @author Franta
 */

public class ExtensionFileFilter extends FileFilter {
  String extension;
  String description;

/** Konstruktor souborov�ho filtru.
 * @param ext p��pona akceptovan�ch soubor�
 */    
  public ExtensionFileFilter(String extension, String description) {
    this.extension   = extension.toLowerCase();
    this.description = description;
  }

/** Akceptuje adres��e a soubory s p��ponou zadanou v konstruktoru.
 * @param dir adres��
 * @param name jm�no souboru
 * @return true, je-li soubor (adres��) akceptov�n
 */    
  public boolean accept(File file) {
    if (file.isDirectory() || file.getName().toLowerCase().endsWith(extension))
      return true;
    return false;
  }

  public String getDescription() {
    return description;
  }
}
