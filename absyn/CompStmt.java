package absyn;

public class CompStmt extends Dec {
  public DecList decs;
  public DecList stmt;
  //public DecList stmt;

  public CompStmt( int pos, DecList decs, DecList stmt ) {
    this.pos = pos;
    this.decs = decs;
    this.stmt = stmt;
  }
}
