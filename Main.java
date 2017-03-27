/*
  Created by: Chris Katsaras and Ryan Lafferty
  File Name: Main.java
*/
   
import java.io.*;
   

class Main {
   public PrintWriter out = null;
   public Boolean abs = false;
   public Boolean sym = false;
   public Boolean asm = false;
   public String name = "";

   public Main()
   {

   }

  public void run(String argv[])
  {
    try {
      if(argv.length == 2) {
        name = argv[0].substring(0, argv[0].lastIndexOf('.'));
        if(argv[1].equals("-a")) {
          abs = true;

          try {
            out = new PrintWriter(name+".abs");
            System.out.println("======\nErrors\n=====\n");
            out.println("======\nErrors\n======\n");
          } catch (Exception e) {

          }
        } 
        else if(argv[1].equals("-s")) {
          sym = true;
          //name = argv[0].substring(0, argv[0].lastIndexOf('.'));
          try{
            out = new PrintWriter(name+".sym");
            System.out.println("======\nErrors\n=====\n");
            out.println("======\nErrors\n======\n");
          } catch(Exception e) {

          }
        }
        else if(argv[1].equals("-c")) {
          asm = true;
          //name = argv[0].substring(0, argv[0].lastIndexOf('.')); 
          try {
            out = new PrintWriter(name+".ast");
            System.out.println("======\nErrors\n=====\n");
            out.println("======\nErrors\n======\n");
          } catch (Exception e) {

          } 
        }
      }
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      p.abs = abs;
      p.sym = sym;
      p.asm = asm;
      p.out = out;

      if(name == null)
      {
        System.out.println("Error: Name is null");
      }
      else
      {
        //System.out.println(name);
        p.fileName = name;
      }

      Object result = p.parse().value;

    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }

    if(out != null){
      out.close();
    }
  }

  static public void main(String argv[]) {    
    Main m = new Main();
    m.run(argv);
    
  } 
}


