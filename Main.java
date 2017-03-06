/*
  Created by: Fei Song
  File Name: Main.java
  To Build: 
  After the scanner, tiny.flex, and the parser, tiny.cup, have been created.
    javac Main.java
  
  To Run: 
    java -classpath /usr/share/java/cup.jar:. Main gcd.tiny

  where gcd.tiny is an test input file for the tiny language.
*/
   
import java.io.*;
   

class Main {
   public static PrintWriter out = null;
   public static Boolean abs = false;

  static public void main(String argv[]) {    

    try {
      if(argv.length == 2) {
        if(argv[1].equals("-a")) {
          abs = true;
          String name = argv[0].substring(0, argv[0].lastIndexOf('.'));

          try {
            out = new PrintWriter(name+".ast");
          } catch (Exception e) {

          }
        }
      }
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      p.abs = abs;
      Object result = p.parse().value;

    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }

    if(out != null){
      out.close();
    }
  } 
}


