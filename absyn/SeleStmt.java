package absyn;

public class SeleStmt extends Dec {
  public Dec expression;
  //public Dec stmt;
  public Exp stmt;
  //public Dec estmt;
  public Exp estmt;
  public SeleStmt( int pos, Dec expression, Exp stmt ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
  }
  public SeleStmt( int pos, Dec expression, Exp stmt, Exp estmt ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
    this.estmt = estmt;
  }
}
