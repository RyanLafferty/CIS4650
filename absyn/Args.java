package absyn;

public class Args extends Dec {
  public DecList args;
  public Args( int pos, DecList args) {
    this.args = args;
  }
}
