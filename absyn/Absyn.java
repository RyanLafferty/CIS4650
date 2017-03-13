package absyn;
import java.io.*;

 public class Absyn {
  public int pos;
  public int depth = 0;
  public int currentDID = 0;
  public PrintWriter p;

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

 public void showTree( DecList tree, int spaces, PrintWriter p ) {//TODO
    this.p = p;
    while( tree != null ) {
      showTree( tree.head, spaces );
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
  }

   private void showTree( ArrayDec tree, int spaces ) {
    indent( spaces );
    System.out.println( "ArrayDec:" );
    p.println("ArrayDec:");
    spaces += SPACES;
    showTree(tree.type, spaces);
    showTree(tree.id, spaces);
    showTree(tree.number, spaces);
  }
   private void showTree( RegularVar tree, int spaces ) {
    indent( spaces );
    System.out.println( "RegularVar:" );
    p.println("RegularVar:");
    spaces += SPACES;
    indent( spaces );
    System.out.println(tree.name);
    p.println(tree.name);
  }
   private void showTree( ArrayVar tree, int spaces ) {
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
    showTree( tree.left, spaces );
    showTree( tree.right, spaces ); 
  }

   private void showTree( Expr tree, int spaces ) {
    indent( spaces );
    System.out.println( "Expr:" );
    p.println("Expr:");
    spaces += SPACES;
    showTree ((VarDec)tree.var, spaces );
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
    indent( spaces );
    System.out.println( "ReturnStmt:" );
    p.println("ReturnStmt:");
    spaces += SPACES;
    if(tree.expression instanceof Expr && tree.expression != null){
       showTree((Expr)tree.expression, spaces);
    }
    else if(tree.expression instanceof SimpleExpr && tree.expression != null) {
      showTree((SimpleExpr)tree.expression, spaces); 
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
    depth++;
    indent( spaces );
    //System.out.println( "IterStmt:" );
    System.out.println(depth +  ":IterStmt:" );
    p.println("IterStmt:");
    spaces += SPACES;
    showTree( tree.expression, spaces );
    showTree( tree.stmt, spaces );
    depth--;
  }

 private void showTree( SeleStmt tree, int spaces ) {
    depth++;
    indent( spaces );
    //System.out.println( "SeleStmt:" );
    System.out.println(depth + "SeleStmt:" );
    p.println("SeleStmt:");
    spaces += SPACES;
    if(tree.type == SeleStmt.IF)
    {
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
    depth++;
    indent( spaces );
    //System.out.println( "FunDec:" );
    System.out.println(depth + ":FunDec:" );
    p.println("FunDec:");
    spaces += SPACES;
    showTree( tree.type, spaces );
    showTree( tree.id, spaces );
    showTree( tree.plist, spaces );
    showTree( tree.cstmt, spaces );
    depth--;
  }

 private void showTree( Call tree, int spaces ) {
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
      showTree (tree.args, spaces ); 
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

  //TODO - create show tree functions

}
