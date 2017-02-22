package absyn;

public class TypeSpec {
  public final static int INT  = 0;
  public final static int VOID  = 1;
  public int type;
  
  public TypeSpec(int type) {
    this.type = type;
  }
}