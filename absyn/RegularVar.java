package absyn;

public class RegularVar extends VarDec {
  public String name;
  public int pos;
  public RegularVar( int pos, String name ) {
    this.pos = pos;
    this.name = name;
  }
}
