package absyn;

public class CompStmt extends Dec {
  public DecList decs;
  public ExpList stmt;
  //public DecList stmt;

  public CompStmt( int pos, DecList decs, ExpList stmt ) {
    this.pos = pos;
    this.decs = decs;
    this.stmt = stmt;
  }
}
