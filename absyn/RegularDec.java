package absyn;

public class RegularDec extends VarDec {

  public TypeSpec type;
  public String id;

  public RegularDec(int pos, TypeSpec type, String id) {
  	this.pos = pos;
  	this.type = type;
  	this.id = id;
  }
}