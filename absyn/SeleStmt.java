package absyn;

public class SeleStmt extends Dec {
  public Dec expression;
  //public Dec stmt;
  public Dec stmt;
  //public Dec estmt;
  public Dec estmt;
  public SeleStmt( int pos, Dec expression, Dec stmt ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
  }
  public SeleStmt( int pos, Dec expression, Dec stmt, Dec estmt ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
    this.estmt = estmt;
  }
}
