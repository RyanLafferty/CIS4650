package absyn;
import java.io.*;
import java.util.*;
//comment
public class Assembler
{   
    /*Constants*/
    public final static int DSIZE = 1024;
    public final static int ISIZE = 1024;
    public final static int PC = 7;
    public final static int GP = 6;
    public final static int FP = 5;
    public final static int AC = 0;
    public final static int AC1 = 1;

    public final static int ofpFO = 0; //original frame pointer
    public final static int retFO = -1; //return address
    public final static int tempRET = -2; //temp / return value slot

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
    private ArrayList <Function> functionList = new ArrayList <Function>();
    private int iterInstructions = -1;
    private int iterJump = 0;
    private int tempFrameOffset;


    //Constructor
    public Assembler(String fileName, ArrayList <Symbol> symbolTable, ArrayList <Function> functionList)
    {
        this.fileName = fileName;
        this.symbolTable = symbolTable;
        this.functionList = functionList;
    }

    //create test main
    public void createTempMain()
    {
        testFunction = new Function("main");
        testFunction.iCnt = 19 + 13;
        testFunction.symbolList.add(new variable("a"));
        testFunction.symbolList.add(new variable("aa"));
        testFunction.symbolList.add(new variable("aaa"));
    }

    //create test function
    public void createTempFun()
    {
        testFunction2 = new Function("fun");
        testFunction2.iCnt = 4 + 2;
        testFunction2.symbolList.add(new variable("c"));
        testFunction2.symbolList.add(new variable("cc"));
        testFunction2.symbolList.add(new variable("ccc"));
    }


    /*
    Desc: The main loop responsible for calling the functions that will
          output the assembly code to the file
    Args: none
    Ret: Returns true on success and false on failure
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

        

        //createTempMain();
        //createTempFun();
        //outputFunction(this.testFunction2);
        //outputFunction(this.testFunction);
        for(int i=0;i<functionList.size();i++) {
            Function f = functionList.get(i);
            f.updateInstructionCnt();
            outputFunction(f);
        }

        

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
    Desc: Outputs the prelude code and io setup code
    Args: none
    Ret: Returns true on success and false on failure
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
    Desc: Outputs the finale code
    Args: none
    Ret: Returns true on success and false on failure
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
    Desc: STATIC - NOT USED
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

        globalPointer = currentDataOffset;
        globalOffset = globalPointer;

        return true;
    }

    /*
    Desc: Loads the global offsets into the variable objects for later use
          in code generation
    Args: 
    Ret: Returns true on success and false on failure
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

        globalPointer = currentDataOffset;
        globalOffset = globalPointer;

        return true;
    }

    /*
    Desc: STATIC - NOT USED
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
    Desc: STATIC - NOT USED
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
    Desc: STATIC - NOT USED
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
    Desc: STATIC - NOT USED
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
    Desc: STATIC - NOT USED
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
    Desc: STATIC - NOT USED
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

    //Cost: 2 Instructions
    /*
    Desc: Outputs a function by iterating through a list of declarations and instructions
    Args: 
    (Function) fun: The function variable containing local declarations and instructions that are to be output
                    using the assembler api
    Ret: true if successful, false otherwise
    */
    private boolean outputFunction(Function fun)
    {
        int i = 0;
        int xOff = 0;
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

        //reserve space for ofp and return address
        currentFrameOffset = -3;
        for(i = 0; i < fun.symbolList.size(); i++)
        {
            v = fun.symbolList.get(i);
            v.offset = currentFrameOffset;
            //System.out.println("local var offset " + v.name + ": " + currentFrameOffset);
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
        /*if(fun.name.equals("fun"))
        {
            assignConstant2(9, fun.getOffset("c"), "assign", false);
            //return 99
            assignConstant2(99, tempRET, "return 99", false);
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
            outputArithmeticExpr2(fun.getOffset("aaa"), /*x
                                  9,                    /*y
                                  fun.getOffset("aa"),  /*z
                                  OpExp2.PLUS, 1, 0,    /*op, #Constants, ConPos
                                  "arith1",             /*comment
                                  false, false, false); /*global flags (x,y,z)
            assignVariable2(fun.getOffset("a"), fun.getOffset("aa"), "", false);
        }*/

        for(i=0;i<fun.instructionList.size();i++){
            iterInstructions--;
            System.out.println(fun.instructionList.get(i));
            Instruction instruct = fun.instructionList.get(i);
            if(instruct.type == 0) {
                System.out.println("ASSIGN CONST");
                xOff = fun.getOffset(instruct.x);
                if(xOff > 0)
                {
                    //check global araarylist
                    xOff = this.getOffset(instruct.x);
                }
                assignConstant2(instruct.constY,xOff, "Assign const", instruct.globalX);
                System.out.println(instruct.constY + "," + fun.getOffset(instruct.x) + "," + instruct.globalX);
            } else if(instruct.type == 2) {
                int xIndexOffset = 0;
                int yIndexOffset = 0;
                System.out.println("ASSIGN VAR");

                xOff = fun.getOffset(instruct.x);
                if(xOff > 0)
                {
                    //check global araarylist
                    xOff = this.getOffset(instruct.x);
                }

                if(instruct.arrayIndexX != -1) {
                    xIndexOffset += instruct.arrayIndexX;
                }
                if(instruct.arrayIndexY != -1) {
                    yIndexOffset += instruct.arrayIndexY;
                }
                assignVariable2(xOff-xIndexOffset,fun.getOffset(instruct.y)-yIndexOffset,"Assign var",instruct.globalX);
                //assignVariable2(int offsetX, int offsetY, String comment, boolean global)
            } else if(instruct.type == 1) {
                int xIndexOffset = 0;
                int yIndexOffset = 0;
                int zIndexOffset = 0;
                int conPos = -1;
                System.out.println("ARITHMETIC");

                if(instruct.arrayIndexX != -1) {
                    xIndexOffset += instruct.arrayIndexX;
                }

                if(instruct.y == null) {
                    yIndexOffset = instruct.constY;
                } else if(instruct.arrayIndexY != -1) {
                    yIndexOffset = fun.getOffset(instruct.y) - instruct.arrayIndexY;
                } else {
                    yIndexOffset = fun.getOffset(instruct.y);
                }

                if(instruct.z == null) {
                    zIndexOffset = instruct.constZ;
                } else if(instruct.arrayIndexZ != -1) {
                    zIndexOffset = fun.getOffset(instruct.z) - instruct.arrayIndexZ;
                } else {
                    zIndexOffset = fun.getOffset(instruct.z);
                }

                if(instruct.numConstants == 1) {
                    if(instruct.y != null && instruct.z == null) {
                        conPos = 1;
                    } else if(instruct.y == null && instruct.z != null) {
                        conPos = 0;
                    }
                }

                outputArithmeticExpr2(fun.getOffset(instruct.x)-xIndexOffset,yIndexOffset,zIndexOffset,instruct.op,instruct.numConstants,conPos,"Arithmetic Expr",instruct.globalX,instruct.globalY,instruct.globalZ);
            } else if(instruct.type == 3){

                int xIndexOffset = 0;
                int yIndexOffset = 0;
                int conPos = -1;
                System.out.println("Logical");

                if(instruct.x == null) {
                    xIndexOffset = instruct.constX;
                } else if(instruct.arrayIndexX != -1) {
                    xIndexOffset = fun.getOffset(instruct.x) - instruct.arrayIndexX;
                } else {
                    xIndexOffset = fun.getOffset(instruct.x);
                }

                if(instruct.y == null) {
                    yIndexOffset = instruct.constY;
                } else if(instruct.arrayIndexY != -1) {
                    yIndexOffset = fun.getOffset(instruct.y) - instruct.arrayIndexY;
                } else {
                    yIndexOffset = fun.getOffset(instruct.y);
                }

                if(instruct.numConstants == 1) {
                    if(instruct.x != null && instruct.y == null) {
                        conPos = 1;
                    } else if(instruct.x == null && instruct.y != null) {
                        conPos = 0;
                    }
                }

                System.out.println("OFFSET X "+instruct.numInstructions);
                System.out.println("OFFSET Y "+xIndexOffset);
                System.out.println("OFFSET Z "+yIndexOffset);
                System.out.println("OP "+instruct.op);
                System.out.println("Num consts "+instruct.numConstants);

                outputLogicalExpr2(instruct.numInstructions,xIndexOffset,yIndexOffset,instruct.op,instruct.numConstants,conPos,"Sele stmt",instruct.globalX,instruct.globalY);
                //outputLogicalExpr2(int offsetX, int offsetY, int offsetZ, int operation, int constants, int conPos, String comment, boolean xglob, boolean yglob)
            } else if(instruct.type == 7) {
                System.out.println("Iter stmt");
                int xIndexOffset = 0;
                int yIndexOffset = 0;
                int conPos = -1;

                if(instruct.x == null) {
                    xIndexOffset = instruct.constX;
                } else if(instruct.arrayIndexX != -1) {
                    xIndexOffset = fun.getOffset(instruct.x) - instruct.arrayIndexX;
                } else {
                    xIndexOffset = fun.getOffset(instruct.x);
                }

                if(instruct.y == null) {
                    yIndexOffset = instruct.constY;
                } else if(instruct.arrayIndexY != -1) {
                    yIndexOffset = fun.getOffset(instruct.y) - instruct.arrayIndexY;
                } else {
                    yIndexOffset = fun.getOffset(instruct.y);
                }

                if(instruct.numConstants == 1) {
                    if(instruct.x != null && instruct.y == null) {
                        conPos = 1;
                    } else if(instruct.x == null && instruct.y != null) {
                        conPos = 0;
                    }
                }
                System.out.println("OFFSET X "+instruct.numInstructions);
                System.out.println("OFFSET Y "+xIndexOffset);
                System.out.println("OFFSET Z "+yIndexOffset);
                System.out.println("OP "+instruct.op);
                System.out.println("Num consts "+instruct.numConstants);
                iterInstructions = instruct.numInstructions;
                iterJump = instruct.numInstructions;
                System.out.println("THE INSTRUCTION NUMBER "+instruct.numInstructions);
                outputLogicalExpr2(instruct.numInstructions,xIndexOffset,yIndexOffset,instruct.op,instruct.numConstants,conPos,"Iter stmt",instruct.globalX,instruct.globalY);
            } else if(instruct.type == 4) {
                System.out.println("Input");
                inputCall(fun.getOffset(instruct.x),"Input",instruct.globalX);
                //inputCall(int offsetX, String comment, boolean global)

            } else if(instruct.type == 6) {
                System.out.println("Call");
                boolean global;
                int j;
                int k;
                Function f;



                tempFrameOffset = currentFrameOffset - 3;
                if(instruct.argList != null){
                    for(j=0;j<instruct.argList.size();j++) {
                        Arg a = instruct.argList.get(j);
                        if(a.var == null) {
                            assignConstant2(a.constX,tempFrameOffset,"Assign const",false);
                        } else {
                            if(fun.getOffset(a.var) == 1) {
                                global = true; 
                            } else {
                                global = false;
                            }
                            assignVariable2(tempFrameOffset,fun.getOffset(a.var), "Assign var", global);

                        }
                        tempFrameOffset --;
                    }
                }
                for(k=0;k<functionList.size();k++) {
                    f = functionList.get(k);
                    if(f.name.equals(instruct.x)){
                        callSequence(f);
                        break;
                    }
                }
            }

            if(iterInstructions == 2){
                iterJump = Math.abs(iterJump) * -1;
                iterJump -= Instruction.LOGIC;
                jumpAround(iterJump,"to top of iter");
                iterInstructions = -1;
            }  

        }
        
        //TODO store return value in tempRET

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


    //Cost: 7 Instructions
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
        //1. how to store args in function being called
        //this can be done just before the sequence
        /*
        starting point  = currentFrameOffset - 3
        for each arg:
            store arg in
            starting point --
        */

        emitRM("ST", FP, currentFrameOffset + ofpFO, FP, "* store current fp");
        emitRM("LDA", FP, currentFrameOffset, FP, "* push new frame");
        emitRM("LDA", AC, 1, PC, "* save return in ac");
        //emitRM("LDA", PC, fun.entry, PC, "* relative jump to function entry");
        emitRM("LDC", PC, fun.entry, AC, "* jump to function entry");

        //load return value
        emitRM("LD", AC, tempRET, FP, "* load return value");

        emitRM("LD", FP, ofpFO, FP, "* pop current frame");

        //store return value
        emitRM("ST", AC, tempRET, FP, "* store return value");
    }

    //Cost: 2 Instructions
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


    //Cost: 2 Instructions
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

    //Cost: 4 Instructions
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

    //Cost: 4 Instructions
    /*
    Desc: Outputs an arithmetic expression of the form (x = y op z) using the supplied information
    Args: 
    (int) offset[X/Y/Z]: the offset into the frame if a variable or the constant value
    (int) operation: the logical operation (using the OpExp2 static values)
    (int) constants: the number of constants
    (int) conPos: the constant position if there is a single constant (0 - left (y), 1 - right (z))
    (String) comment: a comment to go at the end of the line
    (boolean) [x/y]glob: the global flags for x and y that determine if they are global variables
    Ret: Nothing
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
        emitRM(op, AC, (offsetX), PC, comment + op + " logical expr");
    }

    //Cost: 6 Instructions
    /*
    Desc: Outputs the code required to take input from the user and store it in a specified variable
    Args: 
    (int) offset[X]: the offset into the frame of a variable
    (String) comment: a comment to go at the end of the line
    (boolean) global: the global flags for x that determine if they are global variables
    Ret: Nothing
    */
    private void inputCall(int offsetX, String comment, boolean global)
    {
        int reg = FP;
        if(global)
        {
            reg = GP;
        }

        emitRM("ST", FP, currentFrameOffset + ofpFO, FP, "*input store current fp");
        emitRM("LDA", FP, currentFrameOffset, FP, "* push new frame");
        emitRM("LDA", AC, 1, PC, "* save return in ac");
        //emitRM("LDA", PC, fun.entry, PC, "* relative jump to function entry");
        emitRM("LDC", PC, 4, AC, "* jump to function entry");
        emitRM("LD", FP, ofpFO, FP, "* pop current frame");

        //store result
        emitRM("ST", 0, offsetX, reg, "assign");
    }

    //Cost: 7 Instructions
    /*
    Desc: Outputs the code required to print output to the user and store it in a specified variable
    Args: 
    (int) offset[X]: the offset into the frame of a variable
    (String) comment: a comment to go at the end of the line
    (boolean) global: the global flags for x that determine if they are global variables
    Ret: Nothing
    */
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


    //Cost: 1 Instruction
    //Probably not used
    private void outputIter(int cnt)
    {
        emitRM("LDC", 3, cnt, AC, "* init register");
    }


    //Cost: 2 Instructions
    //Probably not used
    private void outputDecrement()
    {
        emitRM("LDC", 4, 1, AC, "load decrement");
        emitRO("SUB", 3, 3, 4, "decrement counter");
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

    public int getOffset(String vName)
    {
        int i = 0;
        for(i = 0; i < globalVars.size(); i++)
        {   
            if(vName.equals(globalVars.get(i).name))
            {
                return globalVars.get(i).offset;
            }
        }

        return 1;
    }

}
