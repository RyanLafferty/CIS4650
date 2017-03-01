package absyn;

public class SeleStmt extends Dec {
  public static int IF = 0;
  public static int ELSE = 1;

  public Dec expression;
  public Dec stmt;
  public Dec estmt;
  public int type;
  public SeleStmt( int pos, Dec expression, Dec stmt, int type ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
    this.type = type;
  }
  public SeleStmt( int pos, Dec expression, Dec stmt, Dec estmt, int type ) {
    this.pos = pos;
    this.expression = expression;
    this.stmt = stmt;
    this.estmt = estmt;
    this.type = type;
  }
}
