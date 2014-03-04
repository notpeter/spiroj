/*
 * RecordWriter.java
 *
 * Created on 20 July 2005, 15:02
 */

package frg.xar;

import java.io.*;

/**
 *
 * @author  fgrebenicek
 */
public class RecordWriter {
   RawWriter raw;
  /** Creates a new instance of RecordWriter */
  
   public RecordWriter(OutputStream os) {
     raw = new RawWriter(os);
   }
   
   public RecordWriter(RawWriter raw) {
     this.raw = raw;
   }
   
   public void writeBegin() throws IOException {
     raw.writeID();
     writeRecord(new FileHeaderRecord());
   }
  
   public void writeEnd() throws IOException {
     writeRecord(new EndOfFileRecord());
   }
   
   public void writeRecord(Record rec) throws IOException {
     rec.refine();
     raw.writeRawRecord(rec);
   }
}
