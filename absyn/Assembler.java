package absyn;
import java.io.*;
import java.util.*;

public class Assembler
{   
    /*Object References*/

    /*Object Variables*/
    String fileName = "";
    private PrintWriter out = null;


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
            this.out = new PrintWriter((this.fileName + ".tm"), "w");
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
    Desc: TODO
    Args: 
    Ret: 
    */
    private void outputPrelude() 
    {

    }
}
