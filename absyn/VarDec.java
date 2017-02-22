package absyn;

public class VarDec extends Exp {

  public TypeSpec type;
  public String id;

  public VarDec(int pos, TypeSpec type, String id) {
  	this.pos = pos;
  	this.type = type;
  	this.id = id;
  }
}