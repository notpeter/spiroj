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

/** Konstruktor souborového filtru.
 * @param ext pøípona akceptovaných souborù
 */    
  public ExtensionFileFilter(String extension, String description) {
    this.extension   = extension.toLowerCase();
    this.description = description;
  }

/** Akceptuje adresáøe a soubory s pøíponou zadanou v konstruktoru.
 * @param dir adresáø
 * @param name jméno souboru
 * @return true, je-li soubor (adresáø) akceptován
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
