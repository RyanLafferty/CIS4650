package absyn;
import java.io.*;
import java.util.*;

public class Function {

	public ArrayList<variable> symbolList = new ArrayList<variable>();
	public String name;
	public int iCnt = 0;

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


	private int updateInstructionCnt()
	{
		//TODO
		return 0;
	}


}