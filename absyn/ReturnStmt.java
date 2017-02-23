package absyn;

public class ReturnStmt extends Exp {
  public Exp expression;
  public ReturnStmt( int pos, Exp expression ) {
    this.pos = pos;
    this.expression = expression;
  }
}
