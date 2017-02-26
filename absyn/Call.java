package absyn;

public class Call extends Dec {
  public String id;
  public DecList args;

  public Call( int pos, String id, DecList args) {
    this.pos = pos;
    this.id = id;
    this.args = args;
  }
}
