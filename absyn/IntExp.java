package absyn;

public class IntExp extends Dec {
  public String value;
  public IntExp( int pos, String value ) {
    this.pos = pos;
    this.value = value;
  }
}
