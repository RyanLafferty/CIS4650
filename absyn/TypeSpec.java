package absyn;

public class TypeSpec extends Exp {
  public final static int INT  = 0;
  public final static int VOID  = 1;
  public int type;
  
  public TypeSpec(int pos, int type) {
  	this.pos = pos;
    this.type = type;
  }
}