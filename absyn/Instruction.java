package absyn;
import java.io.*;
import java.util.*;

public class Instruction {

	/*Constants*/

	//costs
	public final static int FUNC = 2;
    public final static int CONST = 2;
    public final static int VAR = 2;
    public final static int ARITH = 4;
    public final static int LOGIC = 4;
    public final static int CALL = 7;
    public final static int INPUT = 6;
    public final static int OUTPUT = 7;

    //types
	public final static int ASSIGNCONST = 0;
	public final static int ARITHMETIC = 1;
	public final static int ASSIGNVAR = 2;
	public final static int LOGIC_INS = 3;
	public final static int INPUT_INS = 4;
	public final static int OUTPUT_INS = 5;
	public final static int CALL_INS = 6;

	public int type;
	public boolean leftRightFlag; //True means left var
	public int numConstants;
	public String x = null;
	public String y = null;
	public String z = null;
	public int constY;
	public int constZ;
	public int arrayIndexX = -1;
	public int arrayIndexY = -1;
	public int arrayIndexZ = -1;
	public int arrayIndex = -1;
	public boolean globalX;
	public boolean globalY;
	public boolean globalZ;
	public int op;
	public boolean truth;
	public int numInstructions = 0;
	public boolean cut = false;

	//Used for assign Var and assign const
	public Instruction(int type, String x, String y, boolean globalX, boolean globalY, int constY, int numConstants, boolean leftRightFlag, int arrayIndexX, int arrayIndexY) {

		this.type = type;
		this.x = x;
		this.y = y;
		this.constY = constY;
		this.numConstants = numConstants;
		this.leftRightFlag = leftRightFlag;
		this.arrayIndex = arrayIndex;
		this.globalX = globalX;
		this.globalY = globalY;
		this.arrayIndexX = arrayIndexX;
		this.arrayIndexY = arrayIndexY;
	}
	
	//Used for arithmetic expressions
	public Instruction(int type, String x, String y, String z,boolean globalX, boolean globalY, boolean globalZ, int constY, int constZ, int numConstants, int arrayIndexX, int arrayIndexY, int arrayIndexZ, int op) {

		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.constY = constY;
		this.constZ = constZ;
		this.numConstants = numConstants;
		this.arrayIndex = arrayIndex;
		this.op = op;
		this.globalX = globalX;
		this.globalY = globalY;
		this.globalZ = globalZ;
		this.arrayIndexX = arrayIndexX;
		this.arrayIndexY = arrayIndexY;
		this.arrayIndexZ = arrayIndexZ;
	}

	public Instruction(int type, boolean truth) {
		this.type = type;
		this.truth = truth;
	}
	//MAKE GET COST
}