package absyn;

import java.util.*;


public class Symbol {

  public int depth;
  public String sID;
  public String dID;
  public TypeSpec type;

  public Symbol(int depth, String sID, TypeSpec type) {
  	this.depth = depth;
  	this.sID = sID;
 	this.type = type;
  }

  public static boolean isDeclared(String id, int depth, ArrayList <Symbol> symbolList)
  {
    int i;
    Symbol s;
    System.out.println("looking for: " + id + " at depth " + depth);
    for(i = 0; i < symbolList.size(); i++)
    {
        //System.out.println(symbolList.get(i).sID);
        s = symbolList.get(i);
        //match identifier
        if(s.sID.equals(id))
        {
            //check depth
            if(depth > s.depth)
            {
                //all good
                //System.out.println("depth greater, therefore declared");
                return true;
            }
            else if(depth == s.depth)
            {
                //compare depth id -> might need to look into
            }
            else
            {
                //not good
                System.out.println("ERROR: Not Declared");
            }

        }
    }

    return false;
  }
}
