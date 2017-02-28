package absyn;

abstract public class Absyn {
  public int pos;

  final static int SPACES = 4;

  static private void indent( int spaces ) {
    for( int i = 0; i < spaces; i++ ) System.out.print( " " );
  }

  static public void showTree( ExpList tree, int spaces ) {
    while( tree != null ) {
      showTree( tree.head, spaces );
      tree = tree.tail;
    } 
  }

static public void showTree( DecList tree, int spaces ) {//TODO
    while( tree != null ) {
      showTree( tree.head, spaces );
      tree = tree.tail;
    } 
  }

  static private void showTree( Exp tree, int spaces ) {
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
    }
  }

static public void showTree( Dec tree, int spaces ) { //TODO
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
    else if( tree instanceof VarDec) { 
      showTree( (VarDec)tree, spaces );
    }
 
    else 
    {
      indent( spaces );
      System.out.println( "Illegal expression at line " + tree.pos  );
    }
  }

  static public void showTree( VarDec tree, int spaces ) { //TODO
    if( tree instanceof RegularDec)
    {
      System.out.println("ASD");

    }
    else 
    {
      indent( spaces );
      System.out.println( "Illegal expression at line " + tree.pos  );
    }
  }


  static private void showTree( RegularDec tree, int spaces ) {
    indent( spaces );
    System.out.println( "RegularDec:" );
    spaces += SPACES;
    showTree(tree.type, spaces);
    showTree(tree.id, spaces);
  }

static private void showTree( ArrayDec tree, int spaces ) {
    indent( spaces );
    System.out.println( "ArrayDec:" );
    spaces += SPACES;
    showTree(tree.type, spaces);
    showTree(tree.id, spaces);
    showTree(tree.number, spaces);
  }

  static private void showTree( AssignExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "AssignExp:" );
    spaces += SPACES;
    showTree( tree.lhs, spaces );
    showTree( tree.rhs, spaces );
  }

  static private void showTree( TypeSpec tree, int spaces ) {
    indent( spaces );
    if(tree.type == TypeSpec.INT) {
        System.out.println("Int");
    } else {
        System.out.println("Void");
    } 
  }

  static private void showTree( String tree, int spaces ) {
    indent( spaces );
    System.out.println( "ID: " + tree );
  }


  static private void showTree( IntExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "IntExp: " + tree.value ); 
  }

  static private void showTree( OpExp2 tree, int spaces ) {
    indent( spaces );
    System.out.print( "OpExp:" ); 
    switch( tree.op ) {
      case OpExp2.PLUS:
        System.out.println( " + " );
        break;
      case OpExp2.MINUS:
        System.out.println( " - " );
        break;
      case OpExp2.STAR:
        System.out.println( " * " );
        break;
      case OpExp2.SLASH:
        System.out.println( " / " );
        break;
      case OpExp2.EQ:
        System.out.println( " = " );
        break;
      case OpExp2.LT:
        System.out.println( " < " );
        break;
      case OpExp2.GT:
        System.out.println( " > " );
        break;
      default:
        System.out.println( "Unrecognized operator at line " + tree.pos);
    }
    spaces += SPACES;
    showTree( tree.left, spaces );
    showTree( tree.right, spaces ); 
  }

  static private void showTree( Expr tree, int spaces ) {
    indent( spaces );
    System.out.println( "Expr:" );
    spaces += SPACES;
    //TODO: Fix Expr strucutre so var is no longer associated with VarDec
    //showTree ((VarDec)tree.var, spaces );
  }


  //fei's dirty code
  static private void showTree( IfExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "IfExp:" );
    spaces += SPACES;
    showTree( tree.test, spaces );
    showTree( tree.thenpart, spaces );
    showTree( tree.elsepart, spaces );
  }

  

  

  static private void showTree( ReadExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "ReadExp:" );
    showTree( tree.input, spaces + SPACES );  
  }

  static private void showTree( RepeatExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "RepeatExp:" );
    spaces += SPACES;
    showTree( tree.exps, spaces );
    showTree( tree.test, spaces ); 
  }

  static private void showTree( VarExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "VarExp: " + tree.name );
  }

  static private void showTree( WriteExp tree, int spaces ) {
    indent( spaces );
    System.out.println( "WriteExp:" );
    showTree( tree.output, spaces + SPACES ); 
  }

  //TODO - create show tree functions

}
