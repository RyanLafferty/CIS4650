package absyn;
import java.io.*;
import java.util.*;

public class Assembler
{   
    /*Object References*/

    /*Object Variables*/
    String fileName = "";
    private PrintWriter out = null;
    private FileReader preludeFile = null;
    private BufferedReader preludeReader = null;
    private FileReader finaleFile = null;
    private BufferedReader finaleReader = null;
    private int currentLine = 12;


    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    public Assembler(String fileName)
    {
        this.fileName = fileName;
    }


    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    public boolean run()
    {
        boolean succ = false;

        //open files where prelude/finale is stored
        succ = openPrelude();
        if(succ == false)
        {
            System.out.println("Error: Could not open prelude and finale files");
        }

        //create file to output assembly code
        succ = createFile();
        if(succ == false)
        {
            System.out.println("Error: Could not create file");
            return false;
        }

        //output the prelude to the file
        outputPrelude();
        if(succ == false)
        {
            System.out.println("Error: Did not successfully write prelude");
            return false;
        }

        //TODO - Code generation stuff goes here

        //output the finale to the file
        outputFinale();
        if(succ == false)
        {
            System.out.println("Error: Did not successfully write finale");
            return false;
        }

        //close the assembly file
        try
        {
            this.out.close();
            this.out = null;
        }
        catch(Exception e)
        {
            System.out.println("Error: Could not close assembly file");
        }

        return true;
    }

    /*
    Desc: Creates the tm file where the assembly code will be written to
    Args: none
    Ret: On success, returns true; on failure, returns false (boolean)
    */
    private boolean createFile()
    {
        try
        {
            this.out = new PrintWriter((this.fileName + ".tm"));
            if(this.out == null)
            {
                return false;
            }

            return true;
        }
        catch(Exception e)
        {

            return false;
        }
    }


    /*
    Desc: Creates the prelude/finale reader to read the prelude/finale from
    Args: none
    Ret: On success, returns true; on failure, returns false (boolean)
    */
    private boolean openPrelude()
    {
        try
        {
            this.preludeFile = new FileReader("tm.prelude");
            if(this.preludeFile == null)
            {
                return false;
            }

            /*this.finaleFile = new FileReader("tm.finale");
            if(this.finaleFile == null)
            {
                return false;
            }*/

            this.preludeReader = new BufferedReader(this.preludeFile);
            if(this.preludeReader == null)
            {
                return false;
            }

            /*this.finaleReader = new BufferedReader(this.finaleFile);
            if(this.finaleReader == null)
            {
                return false;
            }*/

            return true;
        }
        catch(Exception e)
        {

            return false;
        }
    }


    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private boolean outputPrelude() 
    {
        String line = "";

        //read from the prelude file and write prelude to the
        //assembly file
        while(line != null)
        {
            try
            {
                line = this.preludeReader.readLine();
            }
            catch(Exception e)
            {
                return false;
            }

            if(line != null)
            {
                this.out.println(line);
            }
        }

        //close the file
        try
        {
            this.preludeReader.close();
            this.preludeReader = null;
            this.preludeFile.close();
            this.preludeFile = null;
        }
        catch(Exception e)
        {
            return false;
        }

        return true;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private boolean outputFinale() 
    {
        String line = "";

        line = this.currentLine + ":" + "     ST  5,-1(5)    push ofp";
        this.out.println(line);
        this.currentLine++;
        line = this.currentLine + ":" + "    LDA  5,-1(5)    push frame";
        this.out.println(line);
        this.currentLine++;
        line = this.currentLine + ":" + "    LDA  0,1(7)     load ac with ret ptr";
        this.out.println(line);
        this.currentLine++;
        line = this.currentLine + ":" + "    LDA  7,-35(7)   jump to main loc";
        this.out.println(line);
        this.currentLine++;
        line = this.currentLine + ":" + "* End of execution.";
        this.out.println(line);
        this.currentLine++;
        line = this.currentLine + ":" + "   HALT  0,0,0";
        this.out.println(line);
        this.currentLine++;

        return true;
    }
}
