/*
 * Record.java
 *
 * Created on 20 July 2005, 15:31
 */

package frg.xar;

/**
 * Record adapter
 * @author fgrebenicek
 */
public class Record {
  
  // General tags
  public static final int TAG_UNDEFINED   = 0xffffffff;      // Special tag that is never used.  Used to detect errors
  public static final int TAG_UP          = 0;
  public static final int TAG_DOWN        = 1;
  public static final int TAG_FILEHEADER  = 2;
  public static final int TAG_ENDOFFILE   = 3;

  // Document tags
  public static final int TAG_DOCUMENT          = 40;
  public static final int TAG_CHAPTER           = 41;
  public static final int TAG_SPREAD            = 42;
  public static final int TAG_LAYER             = 43;
  public static final int TAG_PAGE              = 44;
  public static final int TAG_SPREADINFORMATION = 45;
 
  /////////////////////
  
  int tag;
  byte[] data = new byte[0];
  
  /** Creates a new instance of Record */
  public Record(int tag) {
    this.tag = tag;
  }
  
  public int getTag() {
    return tag;
  }
  
  public int getSize() {
    return data.length;
  }
  
  public byte[] getData() {
    return data;
  }
  
  public void refine() { }

}
