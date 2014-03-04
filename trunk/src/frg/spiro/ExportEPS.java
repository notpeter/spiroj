/*
 * ExportEPS.java
 *
 * Created on 12 July 2005, 14:38
 */

package frg.spiro;

import java.io.PrintWriter;

/**
 * Adobe Illustrator EPS (Encapsulated PostScript)
 * @author  fgrebenicek
 */
public class ExportEPS extends ExportAI {
  
  /** Creates a new instance of ExportEPS */
  public ExportEPS() { }
  
  public String getDescription() {
    return "Adobe Encapsulated PostScript (*.eps)";
  }
  
  public String getExtension() {
    return ".eps";
  }
  
  protected void writeSetup(PrintWriter out) {
    out.println("%%BeginSetup");
    out.println("/xd {exch def} bind def");
    out.println("/k{/_k xd/_y xd/_m xd/_c xd/p{_c _m _y _k setcmykcolor} def} def");
    out.println("/K{/_K xd/_Y xd/_M xd/_C xd/P{_C _M _Y _K setcmykcolor} def} def");
    out.println("/g{/_g xd/p{_g setgray} def} def");
    out.println("/G{/_G xd/P{_G setgray} def} def");
    out.println("/m {moveto} def /l {lineto} def /c {curveto} def");
    out.println("/S {P stroke} def /F {p eofill} def");
    out.println("/s {closepath S} def /f {closepath F} def");
    out.println("/B {gsave F grestore S} def /b {closepath B} def"); 
    out.println("/q {gsave} def /Q {grestore} def /W {clip} def"); 
    out.println("/i {setflat} def /J {setlinecap} def /j {setlinejoin} def");
    out.println("/w {setlinewidth} def /M {setmiterlimit} def /d {setdash} def");
    out.println("/u {gsave} def /U {grestore} def"); 
    out.println("/N {newpath} def /n {closepath N} def");
    out.println("/x {pop pop k} def /X {x} def");
    out.println("/H {} def /h {H closepath} def /D {pop} def");
    out.println("/*u { /N {/spth 0 def}def /S{/spth 1 def}def /F {/spth 2 def} def} def");
    out.println("/*U { spth 0 eq {newpath} if spth 1 eq {stroke} if spth 2 eq {fill} if");
    out.println("/N {newpath} def /S {stroke} def /F {fill} def  } def");
    out.println("/TC {pop pop pop} def /Tr {pop} def");
    out.println("/To {pop gsave} def /TO {grestore} def");
    out.println("/Tp {pop matrix astore concat} def  /TP {0 0 moveto} def");
    out.println("/a_str 40 string def /cnt 0 def /h_str (X) def /undsc (_) 0 get def");
    out.println("/fntfix {a_str cvs dup length 1 sub /f_str exch string def");
    out.println("{dup undsc eq {pop}{f_str cnt 3 -1 roll put /cnt cnt 1 add def");
    out.println("} ifelse } forall flush /cnt 0 def f_str cvn } bind def");
    out.println("/Tf {exch fntfix findfont exch scalefont setfont} def /Tx {show} def");
    out.println("%%EndSetup"); 
  }
  
}
