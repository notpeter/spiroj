package frg.util;
import java.io.*;

/** CAPTURING STANDARD OUTPUT IN A LOG FILE.
 * <P>
 * This tip demonstrates how you can record in a log file everything
 * you print to standard out and standard error. This is especially useful
 * if you deploy an application and your users encounter problems.
 * You can have the users send you the log file for analysis.
 *
 * <P>
 * The following example code demonstrates how to capture standard output.
 * You can include the example code, as is, in your program.
 * The example implements a class called SaveOutput, with two static methods
 * - start() and stop(). Calling start() creates a new log file or empties
 * an existing log file. It copies into the log file characters printed
 * to standard output and standard error. Calling stop() closes the log file
 * and restores the behavior of standard output and standard error
 * (that is, their behavior before start() was called).
 *
 * <P>
 * <PRE>
 * import java.io.*;
 *
 * class Stdout {
 *  public static void main(String[] args) {
 *   try {
 *     // Start capturing characters
 *     //into the log file.
 *     SaveOutput.start("log.txt");
 *
 *     // Test it.
 *     System.out.println("Here's is some stuff to stdout.");
 *     System.err.println("Here's is some stuff to stderr.");
 *     System.out.println("Let's throw an exception...");
 *     new Exception().printStackTrace();
 * } catch (Exception e) {
 *     e.printStackTrace();
 * } finally {
 *     // Stop capturing characters
 *     //into the log file
 *     // and restore old setup.
 *     SaveOutput.stop();
 *    }
 *  }
 * }
 * </PRE>
 */
public class SaveOutput extends PrintStream {
  static OutputStream logfile;
  static PrintStream oldStdout;
  static PrintStream oldStderr;

  SaveOutput(PrintStream ps) {
    super(ps);
  }

  
/** Starts copying <CODE>stdout</CODE> and <CODE>stderr</CODE>
 * to the file <CODE>f</CODE>.
 *
 * @param f name of log file
 * @throws IOException when an I/0 error occurs
 */  
  public static void start(String f) throws IOException {
    // Save old settings.
    oldStdout = System.out;
    oldStderr = System.err;

    // Create/Open logfile.
    logfile = new PrintStream(
    new BufferedOutputStream(
    new FileOutputStream(f)));
    
    // Start redirecting the output.
    System.out.println("Redirect to logfile: "+f);
    System.setOut(new SaveOutput(System.out));
    System.setErr(new SaveOutput(System.err));
  }


/** Restores the original settings.
 *
 */  
  public static void stop() {
    if (oldStdout != null) {
      System.setOut(oldStdout);
      System.setErr(oldStderr);
      try { 
        logfile.close(); 
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("Logfile closed.");
    }  
  }


/** PrintStream override.
 * @param b byte to write
 */  
  public void write(int b) {
    try {
      logfile.write(b);
    } catch (Exception e) {
      e.printStackTrace();
      setError();
    }
  	super.write(b);
  }
 
  
/** PrintStream override.
 * @param buf data buffer
 * @param off data offset
 * @param len data length
 */  
  public void write(byte buf[], int off, int len) {
    try {
      logfile.write(buf, off, len);
    } catch (Exception e) {
      e.printStackTrace();
      setError();
    }
    super.write(buf, off, len);
  }
}


