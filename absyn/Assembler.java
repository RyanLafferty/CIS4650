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

        //open file where prelude is stored
        succ = openPrelude();
        if(succ == false)
        {
            System.out.println("Error: Could not open prelude file");
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
    Desc: Creates the prelude reader to read the prelude from
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

            this.preludeReader = new BufferedReader(this.preludeFile);
            if(this.preludeReader == null)
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
}
