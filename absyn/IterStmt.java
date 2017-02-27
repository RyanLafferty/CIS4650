package absyn;

public class IterStmt extends Dec {
  public Dec expression;
  //public Dec stmt;
  public Dec stmt;
  public IterStmt( int pos, Dec expression, Dec stmt ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
  }
}
