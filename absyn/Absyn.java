package absyn;
import java.io.*;
import java.util.*;


 public class Absyn {
  public int pos;
  public int depth = 0;
  public int currentDID = 0;
  public boolean returnValue = false; //Used to identify if there is a return value statement in a function
  public PrintWriter p;
  public ArrayList<Symbol> globalList = new ArrayList<Symbol>();
  public ArrayList<Symbol> table = new ArrayList<Symbol>();
  public ArrayList<Symbol> argList = new ArrayList<Symbol>();
  public ArrayList<Symbol> localArgs = new ArrayList<Symbol>();
  public Hashtable<Integer,ArrayList<Symbol>> hash = new Hashtable<Integer,ArrayList<Symbol>>();

  final  int SPACES = 4;

   private void indent( int spaces ) {
    for( int i = 0; i < spaces; i++ ) {
      System.out.print( " " );
      p.print(" ");
    } 

  }

   public void showTree( ExpList tree, int spaces ) {
    while( tree != null ) {
      showTree( tree.head, spaces );
      tree = tree.tail;
    } 
  }

   public void showTree( ArgList tree, int spaces ) {
    while( tree != null ) {
      showTree( tree.head, spaces );
      tree = tree.tail;
    } 
  }

 public void showTree( DecList tree, int spaces ) {//TODO
    while( tree != null ) {
      showTree( tree.head, spaces );
      tree = tree.tail;
    } 
  }


//main showTree
 public void showTree( DecList tree, int spaces, PrintWriter p, String fileName, int checkpoint) 
 {
    this.p = p;
    while( tree != null )
    {
      showTree( tree.head, spaces );
      tree = tree.tail;
    }
    if(checkpoint == 2)
    {
      Symbol.dumpOrganizedTable(hash,p); 
    }

    if(checkpoint == 3)
    {
      Assembler a = new Assembler(fileName);
      a.run();
    }
  }

  public void dumpArgs(DecList tree) {
    RegularDec argDec = null;
    Symbol arg = null;
    argList.clear();

    while( tree != null ) {
      if(tree.head instanceof RegularDec)
      {
        argDec = (RegularDec) tree.head;
        if(argDec.size > 0)
        {
          arg = new Symbol(depth, currentDID, argDec.id, argDec.type, false, 1);
        }
        else
        {
          arg = new Symbol(depth, currentDID, argDec.id, argDec.type, false);
        }
        argList.add(arg);
      }
      tree = tree.tail;
    }
  }

  public void dumpLocalArgs(DecList tree) {
    RegularDec argDec = null;
    RegularVar argVar = null;
    SimpleExpr s = null;
    Dec d = null;
    Symbol arg = null;
    Symbol typeCheck;
    TypeSpec t = null;
    
    argList.clear();
    while( tree != null ) {
      if(tree.head instanceof SimpleExpr)
      {
        s = (SimpleExpr) tree.head;
        d = s.sime;
        if(d instanceof RegularVar)
        {
          argVar = (RegularVar) d;
          for(int i = 0; i < globalList.size(); i++) {
            typeCheck = globalList.get(i);
            if(argVar.name.equals(typeCheck.sID)) {
              t = typeCheck.type;
            }
          }
          arg = new Symbol(depth, currentDID, argVar.name, t, false); 
          localArgs.add(arg);
        }
        else if(d instanceof OpExp2)
        {
          t = new TypeSpec(0, 0);
          arg = new Symbol(depth, currentDID, "OpExp", t, false); 
          localArgs.add(arg);
        }
      }
      tree = tree.tail;
    }
  }

   private void showTree( Exp tree, int spaces ) {
    if( tree instanceof AssignExp )
      showTree( (AssignExp)tree, spaces );
    else if( tree instanceof IfExp )
      showTree( (IfExp)tree, spaces );
    //else if( tree instanceof IntExp )
      //showTree( (IntExp)tree, spaces );
    else if( tree instanceof OpExp )
      showTree( (OpExp)tree, spaces );
    else if( tree instanceof ReadExp )
      showTree( (ReadExp)tree, spaces );
    else if( tree instanceof RepeatExp )
      showTree( (RepeatExp)tree, spaces );
    else if( tree instanceof VarExp )
      showTree( (VarExp)tree, spaces );
    else if( tree instanceof WriteExp ) 
      showTree( (WriteExp)tree, spaces );
    else {
      indent( spaces );
      System.out.println( "Illegal expression at line " + tree.pos  );
      p.println("Illegal expression at line " + tree.pos);
    }
  }

 public void showTree( Dec tree, int spaces ) { //TODO
    if( tree instanceof RegularDec)
    {
      showTree( (RegularDec)tree, spaces );
    }
    else if( tree instanceof ArrayDec)
    {
      showTree( (ArrayDec)tree, spaces );

    }
    else if( tree instanceof OpExp2) {
      showTree ((OpExp2)tree, spaces );
    }
    else if( tree instanceof IntExp) {
      showTree ((IntExp)tree, spaces);
    }
    else if ( tree instanceof Expr) { 
      showTree ((Expr)tree, spaces);
    }
    else if (tree instanceof SimpleExpr){
       showTree ((SimpleExpr)tree, spaces);
    }
    else if( tree instanceof VarDec) { 
      showTree( (VarDec)tree, spaces );
    }
    else if( tree instanceof ReturnStmt) { 
      showTree( (ReturnStmt)tree, spaces );
    }
    else if( tree instanceof ExprStmt) {
      showTree ((ExprStmt)tree, spaces);
    }
    else if( tree instanceof IterStmt) {
      showTree ((IterStmt)tree, spaces);
    }
    else if( tree instanceof SeleStmt) {
      showTree ((SeleStmt)tree, spaces);
    }
    else if( tree instanceof CompStmt) {
      showTree ((CompStmt)tree, spaces);
    }
    else if( tree instanceof FunDec) {
      showTree ((FunDec)tree, spaces);
    }
    else if( tree instanceof Args) {
      showTree ((Args)tree, spaces);
    }
    else if( tree instanceof Call) {
      showTree ((Call)tree, spaces);
    }
    /*else if(tree != null)
    {
      indent( spaces );
      System.out.println( "Illegal expression at line " + tree.pos  );
    }*/
    else if(tree instanceof Nil) {
      showTree ((Nil)tree, spaces);
    }
    else
    {
      indent( spaces );
      System.out.println( "Illegal expression, object is null");
      p.println("Illegal expression, object is null");
    }
  }

   public void showTree( VarDec tree, int spaces ) { //TODO
    
    if(tree instanceof RegularVar) {
      showTree((RegularVar)tree, spaces);
    } else if (tree instanceof ArrayVar) {
      showTree((ArrayVar)tree, spaces);
    }
  }


   private void showTree( RegularDec tree, int spaces ) {
    indent( spaces );
    System.out.println( "RegularDec:" );
    p.println("RegularDec:");
    spaces += SPACES;
    showTree(tree.type, spaces);
    showTree(tree.id, spaces);
    if(Symbol.isDeclared2(tree.id, depth, currentDID, hash, globalList)){
      indent( spaces );
      System.out.println("Error: redec of var");
    } else {
        globalList.add(new Symbol(depth, currentDID, tree.id,tree.type, false));
        table.add(new Symbol(depth, currentDID, tree.id,tree.type, false));
    }
  }

   private void showTree( ArrayDec tree, int spaces ) {
    IntExp intExp = null;
    indent( spaces );
    System.out.println( "ArrayDec:" );
    p.println("ArrayDec:");
    spaces += SPACES;
    showTree(tree.type, spaces);
    showTree(tree.id, spaces);
    showTree(tree.number, spaces);

    if(Symbol.isDeclared2(tree.id, depth, currentDID, hash, globalList)){
      indent( spaces );
      System.out.println("Error: redec of var");
    } else {
      intExp = tree.number;
      globalList.add(new Symbol(depth, currentDID, tree.id,tree.type, false, Integer.parseInt(intExp.value)));
      table.add(new Symbol(depth, currentDID, tree.id, tree.type, false, Integer.parseInt(intExp.value)));
    }

  }
   private void showTree( RegularVar tree, int spaces ) {
    Symbol s;
    int type;
    if(Symbol.isDeclared2(tree.name, depth, currentDID, hash, globalList) == false) {
      indent( spaces );
      System.out.println("Error: variable not declared");
    }
    indent( spaces );
    System.out.println( "RegularVar:" );
    p.println("RegularVar:");
    spaces += SPACES;
    indent( spaces );
    System.out.println(tree.name);
    p.println(tree.name);
  }
   private void showTree( ArrayVar tree, int spaces ) {
    
    if(Symbol.isDeclared2(tree.id, depth, currentDID, hash, globalList) == false) {
      indent( spaces );
      System.out.println("Error: variable not declared");
    }
    indent( spaces );
    System.out.println( "ArrayVar:" );
    p.println("ArrayVar:");
    spaces += SPACES;
    indent( spaces );
    System.out.println(tree.id);
    p.println(tree.id);
    showTree ((SimpleExpr)tree.number, spaces);
  }

   private void showTree( AssignExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "AssignExp:" );
    p.println("AssignExp:");
    spaces += SPACES;
    showTree( tree.lhs, spaces );
    showTree( tree.rhs, spaces );
  }

   private void showTree( TypeSpec tree, int spaces ) {
    indent( spaces );
    if(tree.type == TypeSpec.INT) {
        System.out.println("Int");
        p.println("Int");
    } else {
        System.out.println("Void");
        p.println("Void");
    } 
  }

   private void showTree( String tree, int spaces ) {
    indent( spaces );
    System.out.println( "ID: " + tree );
    p.println("ID: " + tree);
  }


   private void showTree( IntExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "IntExp: " + tree.value ); 
    p.println("IntExp: " + tree.value );
  }

   private void showTree( OpExp2 tree, int spaces ) {
    indent( spaces );
    System.out.print( "OpExp:" );
    p.print("OpExp: ");
    switch( tree.op ) {
      case OpExp2.PLUS:
        System.out.println( " + " );
        p.println("+");
        break;
      case OpExp2.MINUS:
        System.out.println( " - " );
        p.println("-");
        break;
      case OpExp2.STAR:
        System.out.println( " * " );
        p.println("*");
        break;
      case OpExp2.SLASH:
        System.out.println( " / " );
        p.println("/");
        break;
      case OpExp2.EQ:
        System.out.println( " == " );
        p.println("==");
        break;
      case OpExp2.LT:
        System.out.println( " < " );
        p.println("<");
        break;
      case OpExp2.GT:
        System.out.println( " > " );
        p.println(">");
        break;
      case OpExp2.GTE:
        System.out.println( " >= " );
        p.println(">=");
        break;
      case OpExp2.LTE:
        System.out.println( " <= " );
        p.println("<=");
        break;    
      default:
        System.out.println( "Unrecognized operator at line " + tree.pos);
        p.println("Unrecognized operator at line " + tree.pos);
    }
    spaces += SPACES;


    //type checking
    int typeL = -1;
    int typeR = -1;
    //OpExp2 left = null;
    //OpExp2 right = null;
    RegularVar var = null;
    ArrayVar av = null;
    if(tree.left instanceof RegularVar)
    {
      var = (RegularVar) tree.left;
      typeL = Symbol.getGlobalType(var.name, depth, currentDID, globalList);
      System.out.println("TypeL: "+ typeL);
    }
    else if(tree.left instanceof ArrayVar)
    {
      av = (ArrayVar) tree.left;
      typeL = Symbol.getGlobalType(av.id, depth, currentDID, globalList);
      System.out.println("TypeL: "+ typeL);
    }
    else if (tree.left instanceof OpExp2)
    {
      typeL = getOpType((OpExp2) tree.left);
    }
    else
    {
      if(tree.left instanceof IntExp)
      {
        typeL = Symbol.INT;
      }
      //System.out.println("TypeL: "+ typeL);
    }
    
    //showTree(tree.right, spaces);
    //System.out.println("asdsadasdas");
    if(tree.right instanceof RegularVar)
    {
      var = (RegularVar) tree.right;
      typeR = Symbol.getGlobalType(var.name, depth, currentDID, globalList);
      System.out.println("TypeR: "+ typeR);
    }
    else if(tree.right instanceof ArrayVar)
    {
      av = (ArrayVar) tree.right;
      typeR = Symbol.getGlobalType(av.id, depth, currentDID, globalList);
      System.out.println("TypeR: "+ typeR);
    }
    else
    {
      if(tree.right instanceof IntExp)
      {
        typeR = Symbol.INT;
      }
      //System.out.println("TypeR: "+ typeR);
    }

    //check if types match
    if(typeL > -1 && typeR > -1 && typeL == typeR)
    {
      indent( spaces );
      if(typeL == Symbol.INT)
        System.out.println("int");
      else
        System.out.println("void");
    }
    else
    {
      //report error - todo
      indent( spaces );
      System.out.println("Type mismatch error");
    }

    showTree( tree.left, spaces );
    showTree( tree.right, spaces ); 
  }

   private void showTree( Expr tree, int spaces ) {
    OpExp2 op = null;
    ArrayVar aVar = null;
    RegularVar rVar = null;
    SimpleExpr sime = null;
    IntExp intExp = null;
    IntExp arrayIndex = null;
    Call call = null;
    String varName = "";
    Symbol s = null;
    TypeSpec type = null;
    int t = -1;
    int indexT = -1;

    indent( spaces );
    System.out.println( "Expr:" );
    p.println("Expr:");
    spaces += SPACES;
    if(tree.var instanceof RegularVar) {
      rVar = (RegularVar) tree.var;
      varName = rVar.name;
      for (int i = 0; i < globalList.size(); i++) {
        s = globalList.get(i);
        if(varName.equals(s.sID)) {
          //If var is an array, then we cannot reference it a regular var
          //TODO: TEST
          if(s.arrSize != -1) {
            indent (spaces);
            System.out.println("Error: Not a regular varaible");
          }
        }
      } 
    } else if(tree.var instanceof ArrayVar) {
      aVar = (ArrayVar) tree.var;
      varName = aVar.id;
      for (int i = 0; i < globalList.size(); i++) {
        s = globalList.get(i);
        if(varName.equals(s.sID)) {
          //If var is not an array, then we cannot reference it as one
          //TODO: TEST
          if(s.arrSize == -1) {
            indent (spaces);
            System.out.println("Error: Not an array varaible");
          }
        }
      }
    } 

    for (int i = 0; i < globalList.size(); i++) {
      s = globalList.get(i);

      if(varName.equals(s.sID)) {
        type = s.type;
      }
    }

    showTree ((VarDec)tree.var, spaces );
    if(tree.var instanceof ArrayVar) {
      if(aVar.number instanceof SimpleExpr) {
        sime = (SimpleExpr)aVar.number;
        if(sime.sime instanceof RegularVar) {
          rVar = (RegularVar) sime.sime;
          t = Symbol.getGlobalType(aVar.id, depth, currentDID, globalList);
          indexT = Symbol.getGlobalType(rVar.name,depth,currentDID,globalList);
          if(t != indexT) {
            indent (spaces);
            System.out.println("Error: Index type mismatch");
          }
        }
      }
    }
    
    //checks intexp assigned to void
    if(tree.expression instanceof SimpleExpr) {
      sime = (SimpleExpr)tree.expression;
      if(sime.sime instanceof IntExp) {
        intExp = (IntExp)sime.sime;
        if(type != null && type.type == Symbol.VOID ) {
          indent(spaces);
          System.out.println("Assignment type mismatch error");
        } else if(tree.var instanceof RegularVar && type != null) {
          rVar = (RegularVar) tree.var;
          varName = rVar.name;
          insertValue(rVar.name, intExp.value, -1); 
        } else if(tree.var instanceof ArrayVar) {
          aVar = (ArrayVar) tree.var;
          varName = aVar.id;
          sime = (SimpleExpr)aVar.number;
          if(sime.sime instanceof IntExp){
            System.out.println("INSERT");
            System.out.println(intExp.value);
            arrayIndex = (IntExp)sime.sime;
            insertValue(aVar.id, intExp.value, Integer.parseInt(arrayIndex.value));
          }
          
        }
      }
      //check singular assignment
      else if(sime.sime instanceof RegularVar)
      {
        rVar = (RegularVar) sime.sime;
        t = Symbol.getGlobalType(rVar.name, depth, currentDID, globalList);
        if(t != type.type) {
          indent(spaces);
          System.out.println("Assignment type mismatch error");
        }

      }
      //check arrayvar
      else if(sime.sime instanceof ArrayVar)
      {
        aVar = (ArrayVar) sime.sime;

        t = Symbol.getGlobalType(aVar.id, depth, currentDID, globalList);
        if(t != type.type) {
          indent(spaces);
          System.out.println("Assignment type mismatch error");
        }


      }
      else if(sime.sime instanceof OpExp2)
      {
        op = (OpExp2) sime.sime;
        t = getOpTypeNR(op);
        if(t != type.type)
        { 
          indent(spaces);
          System.out.println("Assignment type mismatch error");
        }
      }
      else if(sime.sime instanceof Call) {
        call = (Call) sime.sime;
        //t = Symbol.getType(aVar.id, depth, currentDID, globalList);
        t = Symbol.getGlobalType(call.id,depth,currentDID,globalList);
        if(t != type.type) {
          indent (spaces);
          System.out.println("Error: Assignment type mismatch");
        }
      } 
    }

    showTree ((Dec)tree.expression, spaces ); 
  }

   private void showTree( SimpleExpr tree, int spaces ) {
    indent( spaces );
    System.out.println( "SimpleExpr:" );
    p.println("SimpleExpr:");
    spaces += SPACES;
    //System.out.println(tree.sime);
    if(tree.sime instanceof OpExp2) {
      showTree ((OpExp2)tree.sime, spaces);
    } 
    else if(tree.sime instanceof IntExp) {
      showTree ((IntExp)tree.sime, spaces);
    }
    else if(tree.sime instanceof VarDec) {
      showTree ((VarDec)tree.sime, spaces);
    }
    else if(tree.sime instanceof Call) {
      showTree ((Call)tree.sime, spaces);
    }
   
  }

   private void showTree( ReturnStmt tree, int spaces ) {
    Symbol s;
    returnValue = true;
    indent( spaces );
    System.out.println( "ReturnStmt:" );
    p.println("ReturnStmt:");
    spaces += SPACES;

    if(tree.expression instanceof Expr && tree.expression != null){
      showTree((Expr)tree.expression, spaces);
      for(int i = 0; i < table.size();i++) {
        s = table.get(i);
        if(s.isFunction == true && s.type.type == TypeSpec.VOID) {
          indent( spaces );
          System.out.println("Error: Void function cannot have return value");
        }
      }
    }
    else if(tree.expression instanceof SimpleExpr && tree.expression != null) {
      SimpleExpr sime;
      Call call;
      showTree((SimpleExpr)tree.expression, spaces);
      sime = (SimpleExpr)tree.expression;

      for(int i = 0; i < table.size();i++) {
        s = table.get(i);
        if(s.isFunction == true && s.type.type == TypeSpec.VOID) {
          indent( spaces );
          System.out.println("Error: Void function cannot have return value");
        }
      } 

      if(sime.sime instanceof Call) {
        call = (Call)sime.sime;
        for(int i = 0; i < globalList.size();i++) {
          s = globalList.get(i);
          if(s.isFunction == true && s.sID.equals(call.id)) {
            if(s.type.type == TypeSpec.VOID) {
              indent( spaces );
              System.out.println("Error: cannot return void function");
            }
          }
        }
      }
    }
    else {
      for(int i = 0; i < table.size();i++) {
        s = table.get(i);
        if(s.isFunction == true && s.type.type == TypeSpec.INT) {
          indent( spaces );
          System.out.println("Error: Int function expects return value");
        }
      }
    }
  }

   private void showTree( ExprStmt tree, int spaces ) {
    indent( spaces );
    System.out.println( "ExprStmt:" );
    p.println("ExprStmt:");
    spaces += SPACES;
    if(tree.expression instanceof Expr && tree.expression != null){
       showTree((Expr)tree.expression, spaces);
    }
    else if(tree.expression instanceof SimpleExpr && tree.expression != null) {
      showTree((SimpleExpr)tree.expression, spaces); 
    }
  }

 private void showTree( IterStmt tree, int spaces ) {
    SimpleExpr exp = null;
    int type = -1;
    depth++;
    currentDID++;
    indent( spaces );
    System.out.println( "IterStmt:" );
    //System.out.println(depth +  ":IterStmt:" );
    p.println("IterStmt:");
    spaces += SPACES;
    if(tree.expression instanceof SimpleExpr)
    {
      exp = (SimpleExpr) tree.expression;
      type = Symbol.exprType(exp, globalList);
      //System.out.println("Type: " + type);
      if(type != Symbol.INT)
      {
        indent(spaces);
        System.out.println("Error: Test condition must be int");
      }
    }
    showTree( tree.expression, spaces );
    showTree( tree.stmt, spaces );
    depth--;
  }

 private void showTree( SeleStmt tree, int spaces ) {
    SimpleExpr exp = null;
    int type = 0;
    depth++;
    currentDID++;
    indent( spaces );
    System.out.println( "SeleStmt:" );
    //System.out.println(depth + "SeleStmt:" );
    p.println("SeleStmt:");
    spaces += SPACES;
    if(tree.type == SeleStmt.IF)
    {
      if(tree.expression instanceof SimpleExpr)
      {
        exp = (SimpleExpr) tree.expression;
        type = Symbol.exprType(exp, globalList);
        //System.out.println("Type: " + type);
        if(type != Symbol.INT)
        {
          indent(spaces);
          System.out.println("Error: Test condition must be int");
        }
      }
      showTree( tree.expression, spaces );
      showTree( tree.stmt, spaces );
    }
    else if(tree.type == SeleStmt.ELSE)
    {
      showTree( tree.expression, spaces );
      showTree( tree.stmt, spaces );
      showTree( tree.estmt, spaces );
    }
    else
    {
      System.out.println("WAT!");
    }
    depth--;
  }

 private void showTree( CompStmt tree, int spaces ) {
    indent( spaces );
    System.out.println( "CompStmt:" );
    p.println("CompStmt:");
    spaces += SPACES;
    showTree( tree.decs, spaces );
    showTree( tree.stmt, spaces );
  }

 private void showTree( FunDec tree, int spaces ) {

    hash.put(currentDID,Symbol.getCopy(table));
    table.clear();
    depth++;
    currentDID++;
    indent( spaces );
    System.out.println( "FunDec:" );
    //System.out.println(depth + ":FunDec:" );
    p.println("FunDec:");
    spaces += SPACES;

    if(Symbol.isDeclared2(tree.id, depth, currentDID, hash, globalList)){
      indent( spaces );
      System.out.println("Error: redec of var");
    } else {
        dumpArgs(tree.plist);
        //globalList.add(new Symbol(depth, currentDID, tree.id,tree.type, true));
        //table.add(new Symbol(depth, currentDID, tree.id,tree.type, true));

        globalList.add(new Symbol(depth, currentDID, tree.id,tree.type, true, argList));
        table.add(new Symbol(depth, currentDID, tree.id,tree.type, true, argList));
    }
    showTree( tree.type, spaces );
    showTree( tree.id, spaces );
    showTree( tree.plist, spaces );
    showTree( tree.cstmt, spaces );
    hash.put(currentDID,Symbol.getCopy(table));
    table.clear();

    //If the function expects an int return type and there is not return statement
    if(tree.type.type == TypeSpec.INT && returnValue == false) {
      indent( spaces );
      System.out.println("Error: Int function expects return value");
    }
    returnValue = false; //Resets value for next function
    currentDID++;
    depth--;
  }

 private void showTree( Call tree, int spaces ) {
    int i = 0;
    int j = 0;
    Symbol s = null;
    Symbol s2 = null;
    if(Symbol.functionDeclared(tree.id, depth, currentDID, globalList) == false) {
      indent( spaces );
      System.out.println("Error: function not declared");
    }
    ArrayList <Symbol> funArgs = null;

    indent( spaces );
    System.out.println( "Call:" );
    p.println("Call:");
    spaces += SPACES;
    showTree (tree.id, spaces );
    if(tree.args == null) {
      indent( spaces );
      System.out.println( "Args empty" );
      p.println("Args empty");
    } else {
      Args a = (Args) tree.args;
      localArgs.clear();
      dumpLocalArgs(a.args); 
      for(i = 0; i < localArgs.size(); i++)
      {
        s = localArgs.get(i);
        //System.out.println("args: " + s.sID);
      }

      for(i = 0; i < globalList.size(); i++)
      {
        s = globalList.get(i);
        if(s.isFunction == true && tree.id.equals(s.sID))
        {
          //System.out.println(s.sID);
          funArgs = s.args;
          if(funArgs.size() == localArgs.size())
          {
            for(j = 0; j < funArgs.size(); j++)
            {
              s = funArgs.get(j);
              s2 = localArgs.get(j);
              if(s != null && s2 != null && s.type != null && s2.type != null
               && s.type.type != s2.type.type)
              {

                indent( spaces );
                if(s.type.type == TypeSpec.INT)
                {
                  System.out.println("Error: Type Mismatch>  (" + s2.sID + ") Received VOID, expected INT");
                }
                else if(s.type.type == TypeSpec.VOID)
                {
                  System.out.println("Error: Type Mismatch>  (" + s2.sID + ") Received INT, expected VOID");
                }
              }
              else if(s != null && s2 != null && s.type != null && s2.type != null)
              {
                //System.out.println("match: " + s.sID + ", " + s2.sID + " (" + s.type.type + "," + s2.type.type + ")");
                indent( spaces );
                if(s.arrSize > 0 && s2.arrSize <= 0)
                {
                  System.out.println("Error: Type Mismatch>  (" + s2.sID + ") Received INT, expected INT []");
                }
                else if(s.arrSize <= 0 && s2.arrSize > 0)
                {
                  System.out.println("Error: Type Mismatch>  (" + s2.sID + ") Received INT [], expected INT");
                }
              }
              else
              {
                System.out.println(s.type + " " + s2.type);
              }
            }
          }
          else
          {
            //todo report arg count error
          }
          break;
        }
      }

      //show tree
      showTree(tree.args, spaces);
    }
    
  }

 private void showTree( Args tree, int spaces ) {
    indent( spaces );
    System.out.println( "Args:" );
    p.println("Args:");
    spaces += SPACES;
    showTree (tree.args, spaces );
  }
   private void showTree(Nil tree, int spaces) {
    indent( spaces );
    System.out.println(" Nil: ");
    p.println("Nil:");
    spaces += SPACES;
    indent( spaces );
    System.out.println(tree.error);
    p.println(tree.error);
  }
  //fei's dirty code
   private void showTree( IfExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "IfExp:" );
    p.println("IfExp:");
    spaces += SPACES;
    showTree( tree.test, spaces );
    showTree( tree.thenpart, spaces );
    showTree( tree.elsepart, spaces );
  }

  

  

   private void showTree( ReadExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "ReadExp:" );
    p.println("ReadExp:");
    showTree( tree.input, spaces + SPACES );  
  }

   private void showTree( RepeatExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "RepeatExp:" );
    p.println("RepeatExp:");
    spaces += SPACES;
    showTree( tree.exps, spaces );
    showTree( tree.test, spaces ); 
  }

   private void showTree( VarExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "VarExp: " + tree.name );
    p.println("VarExp:");
  }

   private void showTree( WriteExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "WriteExp:" );
    p.println("WriteExp:");
    showTree( tree.output, spaces + SPACES ); 
  }




  public int getOpType(OpExp2 tree)
  {
    int left, right = 0;
    OpExp2 opExp;
    if(tree.left instanceof OpExp2)
    {   
        opExp = (OpExp2)tree.left;
        //left = getOpType(opExp);
        return getOpType(opExp);
    }
    else
    {
      //type checking
      int typeL = -1;
      int typeR = -1;

      RegularVar var = null;
      ArrayVar av = null;

      if(tree.left instanceof RegularVar)
      {
        var = (RegularVar) tree.left;
        typeL = Symbol.getGlobalType(var.name, depth, currentDID, globalList);
        //System.out.println("TypeL: "+ typeL);
      }
      else if(tree.left instanceof ArrayVar)
      {
        av = (ArrayVar) tree.left;
        typeL = Symbol.getGlobalType(av.id, depth, currentDID, globalList);
        //System.out.println("TypeL: "+ typeL);
      }
      else
      {
        if(tree.left instanceof IntExp)
        {
          typeL = Symbol.INT;
        }
        //System.out.println("TypeL: "+ typeL);
      }
      
      //showTree(tree.right, spaces);
      //System.out.println("asdsadasdas");
      if(tree.right instanceof RegularVar)
      {
        var = (RegularVar) tree.right;
        typeR = Symbol.getGlobalType(var.name, depth, currentDID, globalList);
        //System.out.println("TypeR: "+ typeR);
      }
      else if(tree.right instanceof ArrayVar)
      {
        av = (ArrayVar) tree.right;
        typeR = Symbol.getGlobalType(av.id, depth, currentDID, globalList);
        //System.out.println("TypeR: "+ typeR);
      }
      else
      {
        if(tree.right instanceof IntExp)
        {
          typeR = Symbol.INT;
          //System.out.println("TypeR: "+ typeR);
        }
      }

      //check if types match
      if(typeL > -1 && typeR > -1 && typeL == typeR)
      {
        if(typeL == Symbol.INT)
          return Symbol.INT;
        else
          return Symbol.VOID;

      }

      return -1;
    }
  }

  public int getOpTypeNR(OpExp2 tree)
  {
    int typeL = -1;
      int typeR = -1;

      RegularVar var = null;
      ArrayVar av = null;

      if(tree.left instanceof RegularVar)
      {
        var = (RegularVar) tree.left;
        typeL = Symbol.getGlobalType(var.name, depth, currentDID, globalList);
        //System.out.println("TypeL: "+ typeL);
      }
      else if(tree.left instanceof ArrayVar)
      {
        av = (ArrayVar) tree.left;
        typeL = Symbol.getGlobalType(av.id, depth, currentDID, globalList);
        //System.out.println("TypeL: "+ typeL);
      }
      else if (tree.left instanceof OpExp2)
      {
        typeL = getOpType((OpExp2) tree.left);
      }
      else
      {
        if(tree.left instanceof IntExp)
        {
          typeL = Symbol.INT;
        }
        //System.out.println("TypeL: "+ typeL);
      }
      
      //showTree(tree.right, spaces);
      //System.out.println("asdsadasdas");
      if(tree.right instanceof RegularVar)
      {
        var = (RegularVar) tree.right;
        typeR = Symbol.getGlobalType(var.name, depth, currentDID, globalList);
        //System.out.println("TypeR: "+ typeR);
      }
      else if(tree.right instanceof ArrayVar)
      {
        av = (ArrayVar) tree.right;
        typeR = Symbol.getGlobalType(av.id, depth, currentDID, globalList);
        //System.out.println("TypeR: "+ typeR);
      }
      else
      {
        if(tree.right instanceof IntExp)
        {
          typeR = Symbol.INT;
          //System.out.println("TypeR: "+ typeR);
        }
      }

      //check if types match
      if(typeL > -1 && typeR > -1 && typeL == typeR)
      {
        if(typeL == Symbol.INT)
          return Symbol.INT;
        else
          return Symbol.VOID;
      }

      return -1;
  }

  public void insertValue(String id, String value, int index) {
    Symbol s;
    int i;
    for(i=0;i<globalList.size();i++) {
      s = globalList.get(i);
      if(s.sID.equals(id)) {
        if(index == -1){
          System.out.println("Inserting value "+value+" into regularVar "+id);
          globalList.get(i).value = Integer.parseInt(value);
        } else {
          System.out.println("Inserting value "+value+" into arrayVar "+id);
          if(index < 0 || index >= globalList.get(i).arrSize) {
            System.out.println("Array out of bounds error!");
          } else {
            globalList.get(i).valueArray[index-1] = Integer.parseInt(value);
          }
        }
      }
    }
    for(i=0;i<globalList.size();i++) {
      s = globalList.get(i);
      System.out.println("ID: "+s.sID+" VAL: "+s.value + " "+s.arrSize);
    }
  }

}
