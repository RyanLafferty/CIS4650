package absyn;

public class Call extends Dec {
  public String id;
  public Dec args;

  public Call( int pos, String id, Dec args) {
    this.pos = pos;
    this.id = id;
    this.args = args;
  }
}
