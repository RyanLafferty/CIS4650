package absyn;

public class ReturnStmt extends Dec {
  public Dec expression;
  public ReturnStmt( int pos, Dec expression ) {
    this.pos = pos;
    this.expression = expression;
  }
}
