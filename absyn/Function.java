package absyn;
import java.io.*;
import java.util.*;

public class Function {

	public ArrayList <variable> symbolList = new ArrayList<variable>();
	public String name;
	public int iCnt = 0; // instruction cnt
	public int entry = 0; //function entry point
	public int offset = 0; //offset in dMem to address location

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
			System.out.println("TEST");
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

		for(i = 0; i < symbolList.size(); i++)
		{
			if(vName.equals(symbolList.get(i).name))
			{
				return symbolList.get(i).offset;
			}
		}

		return -1;
	}



	private int updateInstructionCnt()
	{

		return 0;
	}


}