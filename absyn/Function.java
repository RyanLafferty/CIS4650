package absyn;
import java.io.*;
import java.util.*;

public class Function 
{
	public ArrayList <variable> symbolList = new ArrayList<variable>();
	public ArrayList <Instruction> instructionList = new ArrayList<Instruction>();
	public String name;
	public int iCnt = 0; // instruction cnt
	public int entry = 0; //function entry point
	public int offset = 0; //offset in dMem to address location
	public int instructionCnt = 0;

	public Function(String funName) {
		this.name = funName;
	}

	public static boolean alreadyDeclared(String funName, ArrayList<Function> funList) {

		int i;
		Function f;

		for(i=0;i<funList.size();i++) {
			f = funList.get(i);

			if(f.name.equals(funName)) {
				return true;
			}
		}
		return false;
	}

	public static int functionIndex(String funName, ArrayList<Function> funList) {
		int i;
		Function f;

		for(i=0;i<funList.size();i++) {
			f = funList.get(i);

			if(f.name.equals(funName)) {
				return i;
			}
		}
		return -1;
	}


	public static void updateValue(String funName, ArrayList<Function> funList, String varName, int value, int index) {
 
		int i;
		int j;
		Function f;

		for(i=0;i<funList.size();i++) {
			f = funList.get(i);
			if(f.name.equals(funName)) {
				for(j=0;j<f.symbolList.size();j++) {
					
					if(f.symbolList.get(j).name.equals(varName)) {
						if(index == -1) {
							System.out.println("Updating regularVar value");
							f.symbolList.get(j).value = value;
						} else {
							System.out.println("Updating arrayVar "+varName+" at index "+index+" with value "+value);
							f.symbolList.get(j).valueArray[index] = value;
						}
						
					}
				}
			}
		}
	}

	public int getOffset(String vName)
	{
		int i = 0;
		System.out.println("VNAME:"+vName);
		for(i = 0; i < symbolList.size(); i++)
		{	
			if(vName.equals(symbolList.get(i).name))
			{
				return symbolList.get(i).offset;
			}
		}

		return -1;
	}



	public int updateInstructionCnt()
	{
		int i = 0;
		Instruction in = null;

		this.instructionCnt = 0;
		this.instructionCnt += Instruction.FUNC;
		for(i = 0; i < instructionList.size(); i++)
		{
			in = instructionList.get(i);
			//System.out.println("instruction " + i + ": " + in.type);
			if(in.type == Instruction.ASSIGNCONST)
			{
				this.instructionCnt += Instruction.CONST;
			}
			else if(in.type == Instruction.ASSIGNVAR)
			{
				this.instructionCnt += Instruction.VAR;
			}
			else if(in.type == Instruction.ARITHMETIC)
			{
				this.instructionCnt += Instruction.ARITH;
			}
			else if(in.type == Instruction.LOGIC_INS)
			{
				this.instructionCnt += Instruction.LOGIC;
				//TODO calculate/add body cost
			}
			else if(in.type == Instruction.INPUT_INS)
			{
				this.instructionCnt += Instruction.INPUT;
			}
			else if(in.type == Instruction.OUTPUT_INS)
			{
				this.instructionCnt += Instruction.OUTPUT;
			}
			else if(in.type == Instruction.CALL_INS)
			{
				this.instructionCnt += Instruction.CALL;
			}
		}

		return 0;
	}


}