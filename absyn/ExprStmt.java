package absyn;

public class ExprStmt extends Dec {
  public Dec expression;
  public ExprStmt( int pos, Dec expression ) {
    this.pos = pos;
    this.expression = expression;
  }
}
