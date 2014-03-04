/*
 * RawWriter.java
 *
 * Created on 20 July 2005, 15:08
 */

package frg.xar;

import java.io.*;

/**
 *
 * @author  fgrebenicek
 */
public class RawWriter {
  LittleEndianOutputStream dataOut;
  
  /** Creates a new instance of RawWriter */
  public RawWriter(OutputStream os) {
    dataOut = new LittleEndianOutputStream(os);
  }
  
  public void writeID() throws IOException {
    dataOut.writeInt(0x41524158);
    dataOut.writeInt(0x0a0dA3A3);
  }
  
  public void writeRawRecord(Record rec) throws IOException {
    dataOut.writeInt(rec.getTag());
    dataOut.writeInt(rec.getSize());
    dataOut.write(rec.getData());
  }
  
}
