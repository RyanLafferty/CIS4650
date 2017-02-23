package absyn;

public class RegularVar extends Var {
  public String name;
  public int pos;
  public RegularVar( int pos, String name ) {
    this.pos = pos;
    this.name = name;
  }
}
