/*
 * FileHeaderRecord.java
 *
 * Created on 22 July 2005, 13:46
 */

package frg.xar;

import java.io.*;

/**
 *
 * @author  fgrebenicek
 */
public class FileHeaderRecord extends Record {
  
  byte[] fileType = "CXN".getBytes();
  int fileSize = 0;
  int webLink = 0;
  int refinementFlags;
  String producer = "frg.xar";
  String producerVersion = "1.0";
  String producerBuild = "1";
  
  /** Creates a new instance of FileHeaderRecord */
  public FileHeaderRecord() {
    super(TAG_FILEHEADER);
  }
  
  public void refine() {
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    LittleEndianOutputStream out = new LittleEndianOutputStream(buf);
    try {
      out.write(fileType);
      out.writeInt(fileSize);
      out.writeInt(webLink);
      out.writeInt(refinementFlags);
      out.write(producer.getBytes());
      out.write(0);
      out.write(producerVersion.getBytes());
      out.write(0);
      out.write(producerBuild.getBytes());
      out.write(0);
    } catch (IOException _) {}
    data = buf.toByteArray();
  }
  
}
