/*
 * Test.java
 *
 * Created on 22 July 2005, 14:12
 */

package frg.xar;

import java.io.*;

/**
 *
 * @author  fgrebenicek
 */
public class Test {
  
  /** Creates a new instance of Test */
  public Test() {
  }
  
  public static void createEmptyFile() throws IOException {
    String file = "test.xar";
    System.out.println("Create emtpy file "+file);
    FileOutputStream fos = new FileOutputStream(file);
    RecordWriter writer = new RecordWriter(fos);
    writer.writeBegin();
    writer.writeEnd();
    fos.close();
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws IOException {
    // TODO code application logic here
    createEmptyFile();
  }
  
}
