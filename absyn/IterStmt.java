package absyn;

public class IterStmt extends Dec {
  public Dec expression;
  //public Dec stmt;
  public Exp stmt;
  public IterStmt( int pos, Dec expression, Exp stmt ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
  }
}
