package absyn;

import java.util.*;


public class Symbol {

  public final static int INT  = TypeSpec.INT;
  public final static int VOID  = TypeSpec.VOID;

  public int depth;
  public int dID;
  public String sID;
  public TypeSpec type;

  public Symbol(int depth, int dID, String sID, TypeSpec type) {
  	this.depth = depth;
    this.dID = dID;
  	this.sID = sID;
 	  this.type = type;
  }

  public static boolean isDeclared(String id, int depth, int dID, ArrayList <Symbol> symbolList)
  {
    int i;
    Symbol s;
    //System.out.println("looking for: " + id + " at depth " + depth);
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
                //compare depth id
                if(dID == s.dID)
                {
                    //System.out.println("depth equal, but matched dID, therefore declared");
                    return true;
                }
                else
                {
                    //not found within context
                    //System.out.println("ERROR: dID did not match");
                }
            }
            else
            {
                //not a match
                //System.out.println("ERROR: Not Declared");
            }

        }
    }

    return false;
  }

  public static int getType(String id, int depth, int dID, ArrayList <Symbol> symbolList)
  {
    int i = 0;
    Symbol s;

    for(i = 0; i < symbolList.size(); i++)
    {
      s = symbolList.get(i);
      //match identifier
        if(s.sID.equals(id))
        {
            //check depth
            if(depth > s.depth)
            {
                //return type
                if(s.type.type == TypeSpec.INT)
                {
                  //System.out.println("int");
                  return INT;
                }
                else if(s.type.type == TypeSpec.VOID)
                {
                  return VOID;
                }
            }
            else if(depth == s.depth)
            {
                //compare depth id
                if(dID == s.dID)
                {
                  //return type
                  if(s.type.type == TypeSpec.INT)
                  {
                    //System.out.println("int");
                    return INT;
                  }
                  else if(s.type.type == TypeSpec.VOID)
                  {
                    return VOID;
                  }
                }
            }
        }
    }

    return -1;
  }

  public static ArrayList <Symbol> returnScopeTable(int depth, int dID, ArrayList <Symbol> symbolList)
  {
    int i = 0;
    ArrayList <Symbol> scopeTable = new ArrayList<Symbol>();
    Symbol s = null;


    for(i = 0; i < symbolList.size(); i++)
    {
        s = symbolList.get(i);

        //check depth
        if(depth > s.depth)
        {
            //all good
            scopeTable.add(s);
        }
        else if(depth == s.depth)
        {
            //compare depth id
            if(dID == s.dID)
            {
                scopeTable.add(s);
            }
        }
      }

    return scopeTable;
  }
}
