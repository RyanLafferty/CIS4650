package absyn;
import java.io.*;
import java.util.*;

public class Assembler
{   
    /*Constants*/
    private final static int DSIZE = 1024;
    private final static int ISIZE = 1024;
    private final static int PC = 7;
    private final static int GP = 6;
    private final static int FP = 5;
    private final static int AC = 0;
    private final static int AC1 = 1;

    private final static int ofpFO = 0;
    private final static int retFO = -1;

    //private final static int 

    /*Object References*/

    /*Object Variables*/
    protected String fileName = "";
    private PrintWriter out = null;
    private FileReader preludeFile = null;
    private BufferedReader preludeReader = null;
    private FileReader finaleFile = null;
    private BufferedReader finaleReader = null;
    private int currentLine = 12; //ioffset
    private ArrayList <Symbol> symbolTable = null;
    private int currentDataOffset = 0; //global offset
    private int globalPointer = 0;
    private int globalOffset = 0;
    private int currentFrameOffset = 0; //frame offset
    private int entry = 13;
    private int instructionCnt = 0;
    private String functionOutput = "";
    private Function testFunction = null;
    private Function testFunction2 = null;
    private ArrayList <variable> globalVars = new ArrayList <variable>();


    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    public Assembler(String fileName, ArrayList <Symbol> symbolTable)
    {
        this.fileName = fileName;
        this.symbolTable = symbolTable;
    }

    public void createTempMain()
    {
        testFunction = new Function("main");
        testFunction.iCnt = 17 + 13;
        testFunction.symbolList.add(new variable("a"));
        testFunction.symbolList.add(new variable("aa"));
        testFunction.symbolList.add(new variable("aaa"));
    }

    public void createTempFun()
    {
        testFunction2 = new Function("fun");
        testFunction2.iCnt = 4;
        testFunction2.symbolList.add(new variable("c"));
        testFunction2.symbolList.add(new variable("cc"));
        testFunction2.symbolList.add(new variable("ccc"));
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
        succ = outputPrelude();
        if(succ == false)
        {
            System.out.println("Error: Did not successfully write prelude");
            return false;
        }

        //TODO - Code generation stuff goes here

        //update data offsets
        succ = loadGlobalDataOffsets();
        if(succ == false)
        {
            System.out.println("Error: Did not successfully update data offsets");
            return false;
        }

        //TESTING
        /*this.instructionCnt = 2 + 2 + 2; //calculate size of main
        jumpAround(instructionCnt); //jump around main
        this.entry = this.currentLine;
        emitRM("ST", AC, retFO, FP, "* store return address"); // b
        
        assignConstant(5, getDataOffset("x")); // + 2
        assignConstant(1, getDataOffset("y")); // + 2
        emitRM("LD", PC, retFO, FP, "* return to caller"); // c*/

        createTempMain();
        createTempFun();
        outputFunction(this.testFunction2);
        outputFunction(this.testFunction);

        //returnSequence(); // +2
        /*outputLogicalExpr(7, 
                             getArrayDataOffset("bbb", 0), 
                             getDataOffset("y"),
                             OpExp2.EQ,
                             0,
                             0);*/
        /*assignConstant(22, getArrayDataOffset("bbb", 1));
        assignVariable(getDataOffset("x"), getDataOffset("y"));
        assignVariable(getArrayDataOffset("bbb", 0), getDataOffset("y"));
        outputArithmeticExpr(getDataOffset("x"), 
                             getArrayDataOffset("bbb", 0), 
                             getArrayDataOffset("bbb", 1),
                             OpExp2.PLUS,
                             0,
                             0);*/


        //output the finale to the file
        succ = outputFinale();
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

        /*emitRM( "ST", fp, globalOffset+ofpFO, fp, "push ofp" ); 
        emitRM( "LDA", fp, globalOffset, fp, "push frame" ); 
        emitRM( "LDA", ac, 1, pc, "load ac with ret ptr" ); 
        emitRM_Abs( "LDA", pc, entry, "jump to main loc" ); 
        emitRM( "LD", fp, ofpFO, fp, "pop frame" );
        emitRM( "HALT", 0, 0, 0, "" );*/

        //line = this.currentLine + ": " + "ST " + 
        //line = this.currentLine + ": HALT 0, 0, 0";
        //out.println(line);
        //this.currentLine++;

        //emitRM( "ST", fp, globalOffset+ofpFO, fp, "push ofp" );
        emitRM( "ST", FP, globalOffset + ofpFO, FP, "finale: push ofp" );
        emitRM( "LDA", FP, globalOffset, FP, "push frame" ); 
        emitRM( "LDA", AC, 1, PC, "load ac with ret ptr" ); 
        emitRM_Abs( "LDA", PC, entry, "jump to main loc" ); 
        emitRM( "LD", FP, ofpFO, FP, "pop frame" );
        emitRO( "HALT", 0, 0, 0, "" );

        return true;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private boolean loadDataOffsets()
    {
        int i = 0;
        Symbol s = null;

        if(symbolTable == null)
        {
            return false;
        }


        System.out.println("Variables");
        System.out.println("=========");
        for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            if(s.isFunction == false)
            {
                System.out.println(s.sID + ": " + currentDataOffset);
                s.offset = currentDataOffset;
                if(s.arrSize > 0)
                {
                    currentDataOffset -= s.arrSize + 1; // we will store the size of the array at the end of the array
                }
                else
                {
                    currentDataOffset--;
                }
            }
        }

        System.out.println("\nFunctions");
        System.out.println("=========");
        for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            if(s.isFunction == true)
            {
                System.out.println(s.sID + ": " + currentDataOffset);
                s.offset = currentDataOffset;
                currentDataOffset--;
            }
        }



        /*for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            s.offset = currentDataOffset;
            if(s.isFunction == false)
            {
                System.out.println(s.sID + ": " + s.offset);
                //currentDataOffset--;
            }   
        }*/

        globalPointer = currentDataOffset;
        globalOffset = globalPointer;

        return true;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private boolean loadGlobalDataOffsets()
    {
        int i = 0;
        Symbol s = null;

        if(symbolTable == null)
        {
            return false;
        }


        System.out.println("Variables");
        System.out.println("=========");
        for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            if(s.isFunction == false && s.depth == 0)
            {
                System.out.println(s.sID + ": " + currentDataOffset);
                s.offset = currentDataOffset;
                globalVars.add(new variable(s.sID, s.offset, true));
                if(s.arrSize > 0)
                {
                    currentDataOffset -= s.arrSize + 1; // we will store the size of the array at the end of the array
                }
                else
                {
                    currentDataOffset--;
                }
            }
        }

        System.out.println("\nFunctions");
        System.out.println("=========");
        for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            if(s.isFunction == true)
            {
                System.out.println(s.sID + ": " + currentDataOffset);
                s.offset = currentDataOffset;
                currentDataOffset--;
            }
        }

        

        /*for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            s.offset = currentDataOffset;
            if(s.isFunction == false)
            {
                System.out.println(s.sID + ": " + s.offset);
                //currentDataOffset--;
            }   
        }*/

        globalPointer = currentDataOffset;
        globalOffset = globalPointer;

        return true;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private int getDataOffset(String id)
    {
        int i = 0;
        Symbol s = null;

        if(symbolTable == null)
        {
            return DSIZE;
        }

        for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            if(s.sID.equals(id))
            {
                return s.offset;
            }
        }

        return DSIZE;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private int getArrayDataOffset(String id, int index)
    {
        int i = 0;
        Symbol s = null;

        if(symbolTable == null)
        {
            return DSIZE;
        }

        for(i = 0; i < symbolTable.size(); i++)
        {
            s = symbolTable.get(i);
            if(s.sID.equals(id))
            {
                if(index >= s.arrSize)
                {
                    System.out.println("id:" + id + ", size:" + s.arrSize);
                    return DSIZE;
                }

                return s.offset - index;
            }
        }

        return DSIZE;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private boolean assignConstant(int constant, int offset)
    {
        String line = "";

        line  = this.currentLine + ": LDC 0, " + constant + "(0)";
        out.println(line);
        this.currentLine++;

        line  = this.currentLine + ": ST 0, " + offset + "(6)";
        out.println(line);
        this.currentLine++;

        return true;
    }


    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    private boolean assignVariable(int offsetX, int offsetY)
    {
        String line = "";

        line  = this.currentLine + ": LD 0, " + offsetY + "(6)";
        out.println(line);
        this.currentLine++;

        line  = this.currentLine + ": ST 0, " + offsetX + "(6)";
        out.println(line);
        this.currentLine++;

        return true;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    //x = y *+-/ z
    private boolean outputArithmeticExpr(int offsetX, int offsetY, int offsetZ, int operation, int constants, int conPos)
    {
        String line = "";

        //get operands
        if(constants == 0)
        {
            line  = this.currentLine + ": LD 1, " + offsetY + "(6)";
            out.println(line);
            this.currentLine++;

            line  = this.currentLine + ": LD 2, " + offsetZ + "(6)";
            out.println(line);
            this.currentLine++;
        }
        else if (constants == 1)
        {
            if(conPos == 0)
            {
                //y is constant
                line  = this.currentLine + ": LDC 1, " + offsetY + "(0)";
                out.println(line);
                this.currentLine++;

                line  = this.currentLine + ": LD 2, " + offsetZ + "(6)";
                out.println(line);
                this.currentLine++;
            }
            else if(conPos == 1)
            {
                //z is constant
                line  = this.currentLine + ": LD 1, " + offsetY + "(6)";
                out.println(line);
                this.currentLine++;

                line  = this.currentLine + ": LDC 2, " + offsetZ + "(0)";
                out.println(line);
                this.currentLine++;
            }
        }
        else if (constants == 2)
        {
            //both are constants
            line  = this.currentLine + ": LDC 1, " + offsetY + "(0)";
            out.println(line);
            this.currentLine++;

            line  = this.currentLine + ": LDC 2, " + offsetZ + "(0)";
            out.println(line);
            this.currentLine++;
        }

        //output operation code
        if(operation == OpExp2.PLUS)
        {
            line  = this.currentLine + ": ADD 0, 1, 2";
        }
        else if(operation == OpExp2.MINUS)
        {
            line  = this.currentLine + ": SUB 0, 1, 2";
        }
        else if(operation == OpExp2.STAR)
        {
            line  = this.currentLine + ": MUL 0, 1, 2";
        }
        else if(operation == OpExp2.SLASH)
        {
            line  = this.currentLine + ": DIV 0, 1, 2";
        }
        //may generate div zero
        out.println(line);
        this.currentLine++;
        
        //store result
        line  = this.currentLine + ": ST 0, " + offsetX + "(6)";
        out.println(line);
        this.currentLine++;

        return true;
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    //y OP z  -> jump x where x is the number of instuctions to skip
    private boolean outputLogicalExpr(int offsetX, int offsetY, int offsetZ, int operation, int constants, int conPos)
    {
        String line = "";

        //get operands
        if(constants == 0)
        {
            line  = this.currentLine + ": LD 1, " + offsetY + "(6)";
            out.println(line);
            this.currentLine++;

            line  = this.currentLine + ": LD 2, " + offsetZ + "(6)";
            out.println(line);
            this.currentLine++;
        }
        else if (constants == 1)
        {
            if(conPos == 0)
            {
                //y is constant
                line  = this.currentLine + ": LDC 1, " + offsetY + "(0)";
                out.println(line);
                this.currentLine++;

                line  = this.currentLine + ": LD 2, " + offsetZ + "(6)";
                out.println(line);
                this.currentLine++;
            }
            else if(conPos == 1)
            {
                //z is constant
                line  = this.currentLine + ": LD 1, " + offsetY + "(6)";
                out.println(line);
                this.currentLine++;

                line  = this.currentLine + ": LDC 2, " + offsetZ + "(0)";
                out.println(line);
                this.currentLine++;
            }
        }
        else if (constants == 2)
        {
            //both are constants
            line  = this.currentLine + ": LDC 1, " + offsetY + "(0)";
            out.println(line);
            this.currentLine++;

            line  = this.currentLine + ": LDC 2, " + offsetZ + "(0)";
            out.println(line);
            this.currentLine++;
        }

        //update register 0
        line  = this.currentLine + ": SUB 0, 1, 2";
        out.println(line);
        this.currentLine++;

        //output logical jump code
        if(operation == OpExp2.EQ)
        {
            line  = this.currentLine + ": JNE 0, " + (offsetX - 1) + "(7)";
        }
        else if(operation == OpExp2.LT)
        {
            line  = this.currentLine + ": JGE 0, " + (offsetX - 1) + "(7)";
        }
        else if(operation == OpExp2.GT)
        {
            line  = this.currentLine + ": JLE 0, " + (offsetX - 1) + "(7)";
        }
        else if(operation == OpExp2.LTE)
        {
            line  = this.currentLine + ": JGT 0, " + (offsetX - 1) + "(7)";
        }
        else if(operation == OpExp2.GTE)
        {
            line  = this.currentLine + ": JLT 0, " + (offsetX - 1) + "(7)";
        }
        else if(operation == OpExp2.NE)
        {
            line  = this.currentLine + ": JEQ 0, " + (offsetX - 1) + "(7)";
        }

        //may generate div zero
        out.println(line);
        this.currentLine++;
        

        return true;
    }



/*    private void returnSequence()
    {
        
    }
*/

    //TODO
    private boolean outputFunction(Function fun)
    {
        int i = 0;
        variable v = null;

        functionOutput = "";
        currentFrameOffset = 0;

        if(fun == null)
        {
            return false;
        }

        jumpAround(fun.iCnt, fun.name); //jump around function
        if(fun.name.equals("main"))
        {
            this.entry = this.currentLine; //set main entry point
        }
        fun.entry = this.currentLine;
        
        emitRM("ST", AC, retFO, FP, "* store return address"); // b

        //TODO - function instruction loop
        //1. Local Args - done
        //2. Instructions - chris still neeeds to implement
        //3. calculated instruction cnt - still needs to be implemented

        //reserve space for ofp and return address //TODO modify to add a third reserved slot
        currentFrameOffset = -2;
        for(i = 0; i < fun.symbolList.size(); i++)
        {
            v = fun.symbolList.get(i);
            v.offset = currentFrameOffset;
            if(v.arraySize > 0)
            {
                currentFrameOffset -= v.arraySize + 1; // we will store the size of the array at the end of the array
            }
            else
            {
                currentFrameOffset--;
            }
        }

        //testing
        if(fun.name.equals("fun"))
        {
            assignConstant2(9, fun.getOffset("c"), "assign", false);
        }

        //testing
        if(fun.name.equals("main"))
        {
            assignConstant2(9, fun.getOffset("a"), "assign", false);
            //this is where args would be calculated
            callSequence(testFunction2);

            //test io calls
            outputCall(fun.getOffset("a"), "output", false);
            inputCall(fun.getOffset("a"), "input", false);

            assignConstant2(11, fun.getOffset("aa"), "assign", false);
            outputArithmeticExpr2(fun.getOffset("aaa"), /*x*/
                                  9,                    /*y*/
                                  fun.getOffset("aa"),  /*z*/
                                  OpExp2.PLUS, 1, 0,    /*op, #Constants, ConPos*/
                                  "arith1",             /*comment*/
                                  false, false, false); /*global flags (x,y,z)*/
            assignVariable2(fun.getOffset("a"), fun.getOffset("aa"), "", false);
        }

        //this is where the instruction loop would go
        //also you need to precalculate the number of instructions
        //given the instruction type
        
        emitRM("LD", PC, retFO, FP, "* return to caller"); // c

        return true;
    }


    /*
    Desc: Outputs a jump around a function
    Args: 
    (int) instructionCnt: the number of instructions in the function to jump around
    (String) name: the name of the function to jump around, used for a comment
    Ret: Nothing
    */
    private void jumpAround(int instructionCnt, String name)
    {
        emitRM("LDA", PC, instructionCnt, PC, "* jump around " + name);
    }


    /*
    Desc: Outputs a function call sequence -TODO
    Args: 
    (Function) fun: the offset into the frame of a variable
    Ret: Nothing
    */
    private void callSequence(Function fun)
    {
        //emitRM("LDA", PC, instructionCnt, PC, "* jump around main");
        //TODO Calculate Args

        emitRM("ST", FP, currentFrameOffset + ofpFO, FP, "* store current fp");
        emitRM("LDA", FP, currentFrameOffset, FP, "* push new frame");
        emitRM("LDA", AC, 1, PC, "* save return in ac");
        //emitRM("LDA", PC, fun.entry, PC, "* relative jump to function entry");
        emitRM("LDC", PC, fun.entry, AC, "* jump to function entry");
        emitRM("LD", FP, ofpFO, FP, "* pop current frame");
    }

    /*
    Desc: Outputs an assignment expression of the form (x = c) where c is a constant using the supplied information
    Args: 
    (int) offset: the offset into the frame of a variable
    (String) comment: a comment to go at the end of the line
    (boolean) global: the global flag that determine if it is a global variable
    Ret: Nothing
    */
    private void assignConstant2(int constant, int offset, String comment, boolean global)
    {
        int reg = FP;
        if(global)
        {
            reg = GP;
        }
        emitRM("LDC", AC, constant, AC, comment);
        emitRM("ST", AC, offset, reg, comment);
    }


    /*
    Desc: Outputs an assignment expression of the form (x = y) using the supplied information
    Args: 
    (int) offset[X/Y]: the offset into the frame of a variable
    (String) comment: a comment to go at the end of the line
    (boolean) global: the global flag that determine if it is a global variable
    Ret: Nothing
    */
    private void assignVariable2(int offsetX, int offsetY, String comment, boolean global)
    {
        int reg = FP;
        if(global)
        {
            reg = GP;
        }

        emitRM("LD", AC, offsetY, reg, "assign");
        emitRM("ST", AC, offsetX, reg, "assign");
    }

    /*
    Desc: Outputs an arithmetic expression of the form (x = y op z) using the supplied information
    Args: 
    (int) offset[X/Y/Z]: the offset into the frame if a variable or the constant value
    (int) operation: the arithmetic operation (using the OpExp2 static values)
    (int) constants: the number of constants
    (int) conPos: the constant position if there is a single constant (0 - left (y), 1 - right (z))
    (String) comment: a comment to go at the end of the line
    (boolean) [x/y/z]glob: the global flags for x, y and z that determine if they are global variables
    Ret: Nothing
    */
    private void outputArithmeticExpr2(int offsetX, int offsetY, int offsetZ, int operation, int constants, int conPos, String comment, boolean xglob, boolean yglob, boolean zglob)
    {
        String op = "";
        int reg1 = FP;
        int reg2 = FP;
        int reg3 = FP;
        if(yglob)
        {
            reg1 = GP;
        }
        if(zglob)
        {
            reg2 = GP;
        }
        if(xglob)
        {
            reg3 = GP;
        }

        String line = "";

        //get operands
        if(constants == 0)
        {
            //fetch y
            emitRM("LD", AC, offsetY, reg1, comment + " load y");

            //fetch z
            emitRM("LD", AC1, offsetZ, reg2, comment + " load z");
        }
        else if (constants == 1)
        {
            if(conPos == 0)
            {
                //fetch y
                emitRM("LDC", AC, offsetY, AC, comment + " load const y");

                //fetch z
                emitRM("LD", AC1, offsetZ, reg2, comment + " load z");
            }
            else if(conPos == 1)
            {
                //fetch y
                emitRM("LD", AC, offsetY, reg1, comment + " load y");

                //fetch z
                emitRM("LDC", AC1, offsetZ, AC, comment + " load const z");
            }
        }
        else if (constants == 2)
        {
            //fetch y
            emitRM("LDC", AC, offsetY, AC, comment + " load const y");

            //fetch z
            emitRM("LDC", AC1, offsetZ, AC, comment + " load const z");
        }

        //output operation code
        if(operation == OpExp2.PLUS)
        {
            op = "ADD";
        }
        else if(operation == OpExp2.MINUS)
        {
            op = "SUB";
        }
        else if(operation == OpExp2.STAR)
        {
            op = "MUL";
        }
        else if(operation == OpExp2.SLASH)
        {
            op = "DIV";
        }

        //perform operation
        emitRO(op, AC, AC, AC1, comment + " " + op + " x = y + z");
        
        //store result
        emitRM("ST", AC, offsetX, reg3, comment + " store x");
    }

    /*
    Desc: TODO
    Args: 
    Ret: 
    */
    //y OP z  -> jump x where x is the number of instuctions to skip
    private void outputLogicalExpr2(int offsetX, int offsetY, int offsetZ, int operation, int constants, int conPos, String comment, boolean xglob, boolean yglob)
    {
        String op = "";
        int reg1 = FP;
        int reg2 = FP;
        if(xglob)
        {
            reg1 = GP;
        }
        else if(yglob)
        {
            reg2 = GP;
        }

        //get operands
        if(constants == 0)
        {
            //fetch y
            emitRM("LD", AC, offsetY, reg1, comment + " load y");

            //fetch z
            emitRM("LD", AC1, offsetZ, reg2, comment + " load z");
        }
        else if (constants == 1)
        {
            if(conPos == 0)
            {
                //fetch y
                emitRM("LDC", AC, offsetY, AC, comment + " load const y");

                //fetch z
                emitRM("LD", AC1, offsetZ, reg2, comment + " load z");
            }
            else if(conPos == 1)
            {
                //fetch y
                emitRM("LD", AC, offsetY, reg1, comment + " load y");

                //fetch z
                emitRM("LDC", AC1, offsetZ, AC, comment + " load const z");
            }
        }
        else if (constants == 2)
        {
            //fetch y
            emitRM("LDC", AC, offsetY, AC, comment + " load const y");

            //fetch z
            emitRM("LDC", AC1, offsetZ, AC, comment + " load const z");
        }

        //update register 0
        emitRO("SUB", AC, AC, AC1, comment + " " + "SUB" + " x = y - z");

        //output logical jump code
        if(operation == OpExp2.EQ)
        {
            op = "JNE";
        }
        else if(operation == OpExp2.LT)
        {
            op = "JGE";
        }
        else if(operation == OpExp2.GT)
        {
            op = "JLE";
        }
        else if(operation == OpExp2.LTE)
        {
            op = "JGT";
        }
        else if(operation == OpExp2.GTE)
        {
            op = "JLT";
        }
        else if(operation == OpExp2.NE)
        {
            op = "JEQ";
        }

        //output jump statement
        emitRM(op, AC, (offsetX - 1), PC, comment + op + " logical expr");
    }

    private void inputCall(int offsetX, String comment, boolean global)
    {
        int reg = FP;
        if(global)
        {
            reg = GP;
        }

        emitRM("ST", FP, currentFrameOffset + ofpFO, FP, "* store current fp");
        emitRM("LDA", FP, currentFrameOffset, FP, "* push new frame");
        emitRM("LDA", AC, 1, PC, "* save return in ac");
        //emitRM("LDA", PC, fun.entry, PC, "* relative jump to function entry");
        emitRM("LDC", PC, 4, AC, "* jump to function entry");
        emitRM("LD", FP, ofpFO, FP, "* pop current frame");

        //store result
        emitRM("ST", 0, offsetX, reg, "assign");
    }

    private void outputCall(int offsetX, String comment, boolean global)
    {
        int reg = FP;
        if(global)
        {
            reg = GP;
        }

        //load value to be output
        emitRM("LD", AC1, offsetX, reg, comment + " load x");

        //standard function jump setup
        emitRM("ST", FP, currentFrameOffset + ofpFO, FP, "* store current fp");
        emitRM("LDA", FP, currentFrameOffset, FP, "* push new frame");
        
        //store data to output (-2)
        emitRM("ST", AC1, -2, reg, "store output value");

        //store return value
        emitRM("LDA", AC, 1, PC, "* save return in ac");

        //standard function call finale
        emitRM("LDC", PC, 7, AC, "* jump to function entry");
        emitRM("LD", FP, ofpFO, FP, "* pop current frame");
    }


    ///////////emit functions//////////

    //emit operation instruction
    private void emitRO(String opCode, int r, int s, int t, String comment)
    {
        String line = "";

        line = currentLine + ": " + opCode + " " + r + ", " + s + ", " + t + "    " + comment;
        out.println(line);
        this.currentLine++;
    }

    //emit memory instruction
    private void emitRM(String opCode, int r, int d, int s, String comment)
    {
        String line = "";

        line = currentLine + ": " + opCode + " " + r + ", " + d + "(" + s+ ")    " + comment;
        out.println(line);
        this.currentLine++;
    }

    //emit memory instruction
    private void emitRM_Abs(String opCode, int r, int a, String comment)
    {
        String line = "";

        line = currentLine + ": " + opCode + " " + r + ", " + (a - (this.currentLine + 1)) + "(" + "7" + ")    " + comment;
        out.println(line);
        this.currentLine++;
    }

    //emit full line comment
    private void emitComment(String comment)
    {
        String line = "";

        line = "* " + comment;
        out.println(line);
        this.currentLine++;
    }

    ///////////emit functions//////////

}
