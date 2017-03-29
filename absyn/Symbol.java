package absyn;

import java.util.*;
import java.io.*;

public class Symbol {

  public final static int INT  = TypeSpec.INT;
  public final static int VOID  = TypeSpec.VOID;

  public int depth = -1;
  public int dID = -1;
  public String sID = "";
  public TypeSpec type = null;
  public boolean isFunction = false;
  public int arrSize = -1;
  public ArrayList <Symbol> args = null;
  public int value = 0; //Assumption that default value is 0. Used for uninitialized cases
  public int valueArray[];
  public int offset = 0;
  public int address = 0;

  public Symbol(int depth, int dID, String sID, TypeSpec type, boolean isFunction) {
  	this.depth = depth;
    this.dID = dID;
  	this.sID = sID;
 	  this.type = type;
    this.isFunction = isFunction;
  }

  public Symbol(int depth, int dID, String sID, TypeSpec type, boolean isFunction, int arrSize) {
    this.depth = depth;
    this.dID = dID;
    this.sID = sID;
    this.type = type;
    this.isFunction = isFunction;
    this.arrSize = arrSize;
    this.valueArray = new int[arrSize];
  }

  public Symbol(int depth, int dID, String sID, TypeSpec type, boolean isFunction, ArrayList <Symbol> args) {
    this.depth = depth;
    this.dID = dID;
    this.sID = sID;
    this.type = type;
    this.isFunction = isFunction;
    if(this.isFunction == true)
    {
        this.args = getCopy(args);
    }
  }

//temp
public Symbol(int depth, int dID, String sID, boolean isFunction) {
    this.depth = depth;
    this.dID = dID;
    this.sID = sID;
    this.isFunction = isFunction;
    if(this.isFunction == true)
    {
        this.args = getCopy(args);
    }
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

  public static ArrayList <Symbol> getCopy(ArrayList <Symbol> symbolList)
  {
    int i = 0;
    Symbol s = null;
    ArrayList <Symbol> list = new ArrayList <Symbol>();

    for(i = 0; i < symbolList.size(); i++)
    {
        s = symbolList.get(i);
        list.add(s);
    }

    return list;
  }

  public static void dumpTable(Hashtable <Integer, ArrayList <Symbol>> symbolTable)
  {
    int i = 0;
    int key = 0;
    Symbol s = null;
    ArrayList <Symbol> symbolList = null;
    Enumeration <Integer> enumKey = symbolTable.keys();
    
    System.out.println("\n\n***********************************************");
    System.out.println("Hashtable: ");

    while(enumKey.hasMoreElements()) 
    {
        key = enumKey.nextElement();
        symbolList = symbolTable.get(key);
        
        System.out.println("==========\n dID = " + key + "\n==========");
        for(i = 0; i < symbolList.size(); i++)
        {
            s = symbolList.get(i);
            System.out.println(s.sID);
        }        
    }

    System.out.println("***********************************************");
  }

    public static void dumpOrganizedTable(Hashtable <Integer, ArrayList <Symbol>> symbolTable, PrintWriter p)
  {
    int i = 0;
    int key = 0;
    Symbol s = null;
    ArrayList <Symbol> symbolList = null;
    Enumeration <Integer> enumKey = symbolTable.keys();
    
    System.out.println("\n\n***********************************************");
    p.println("\n\n***********************************************");
    System.out.println("Hashtable: ");
    p.println("Hashtable: ");
    while(enumKey.hasMoreElements()) 
    {
        key = enumKey.nextElement();
        symbolList = symbolTable.get(key);
        
        for(i = 0; i < symbolList.size(); i++)
        {
            s = symbolList.get(i);
            if(i == 0)
            {
                System.out.println("====================\n dID = " + key + ", depth = " + s.depth + "\n====================");
                p.println("====================\n dID = " + key + ", depth = " + s.depth + "\n====================");
            }

            if(s.type.type == TypeSpec.INT) {
              System.out.println(s.sID +"  =  "+ "INT");
              p.println(s.sID +"  =  "+ "INT");
            } else {
              System.out.println(s.sID +"  =  "+ "VOID");
              p.println(s.sID +"  =  "+ "VOID");
            }
            
        }        
    }

    System.out.println("***********************************************");
  }

  public static boolean isDeclared2(String id, int depth, int dID, Hashtable <Integer, ArrayList <Symbol>> symbolTable, ArrayList <Symbol> globalSymbolList)
  {
    int i;
    Symbol s = null;
    ArrayList <Symbol> symbolList = null;

    //check current scope using the hash table O(1) + n lookup,
    //where n is the length of the list in the hash table
    symbolList = symbolTable.get(dID);

    if(symbolList != null)
    {
      for(i = 0; i < symbolList.size(); i++)
      {
        s = symbolList.get(i);
        //match identifier
        if(s.sID.equals(id))
        {
            return true;

        }
      }
    }

    //if not found in table fall back to the global list with O(n) lookup,
    //where n is the number of symbols
    symbolList = globalSymbolList;

    if(symbolList != null)
    {
      for(i = 0; i < symbolList.size(); i++)
      { 
        s = symbolList.get(i);
        //match identifier
        if(s.sID.equals(id))
        {
            //check depth
            if(depth > s.depth)
            {
                //all good
                return true;
            }
            else if(depth == s.depth)
            {
                //compare depth id
                if(dID == s.dID)
                {
                    return true;
                }
            }
        }
      }
    }
    
    return false;
  }

  public static boolean functionDeclared(String id, int depth, int dID, ArrayList <Symbol> globalSymbolList)
  {
      
      Symbol s = null;
      for(int i = 0; i < globalSymbolList.size(); i++) {
        s = globalSymbolList.get(i);

        if(s.sID.equals(id)) {
          return true;
        }
      }
      return false;
  }

  public static int exprType(SimpleExpr exp, ArrayList <Symbol> globalList)
  {
    Symbol typeCheck = null;
    //SimpleExpr sim = null;
    RegularVar var = null;
    String name = "";
    String cname = "";
    int i = 0;

    /*if(exp.expression instanceof SimpleExpr)
    {
        sim = (SimpleExpr) exp.expression;
        if(sim.sime instanceof RegularVar)
        {
            var = (RegularVar) sim.sime;
            name = var.name;

            for(i = 0; i < globalList.size(); i++) 
            {
                typeCheck = globalList.get(i);
                if(name.equals(typeCheck.sID)) 
                {
                  return typeCheck.type.getType();
                }
            }
        }
    }*/

    if(exp.sime instanceof RegularVar)
    {
        var = (RegularVar) exp.sime;
        name = var.name;

        for(i = 0; i < globalList.size(); i++) 
        {
            typeCheck = globalList.get(i);
            if(name.equals(typeCheck.sID)) 
            {
              return typeCheck.type.getType();
            }
        }

        return -1;
    }
    /*else if(exp.sim instanceof OpExp2)
    {
        return INT;
    }*/

    return INT;
  }

  public static int getGlobalType(String id, int depth, int dID, ArrayList <Symbol> globalList) {
    int i = 0;
    Symbol s;

    for(i=0;i<globalList.size();i++) {
      s = globalList.get(i);
      if(s.sID.equals(id)) {
        if(s.type.type == TypeSpec.INT) {
          return INT;
        } else if(s.type.type == TypeSpec.VOID) {
          return VOID;
        }
      }
    }
    //If the symbol is not found
    return -1;
  }
}
