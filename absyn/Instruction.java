package absyn;
import java.io.*;
import java.util.*;

public class Instruction {
	public final static int ASSIGNCONST = 0;
	public final static int ARITHMETIC = 1;
	public final static int ASSIGNVAR = 2;

	public int type;
	public boolean leftRightFlag;
	public int numConstants;
	public String x = null;
	public String y = null;
	public String z = null;
	public int constY;
	public int constZ;
	public int arrayIndex = -1;

	public Instruction(int type, String x, String y, String z, int constY, int constZ, int numConstants, boolean leftRightFlag, int arrayIndex) {

		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.constY = constY;
		this.constZ = constZ;
		this.numConstants = numConstants;
		this.leftRightFlag = leftRightFlag;
		this.arrayIndex = arrayIndex;
	}
}