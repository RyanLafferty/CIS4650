package absyn;

public class RegularDec extends VarDec {

  public TypeSpec type;
  public String id;
  public int size;

  public RegularDec(int pos, TypeSpec type, String id) {
  	this.pos = pos;
  	this.type = type;
  	this.id = id;
  	this.size = 0;
  }
  public RegularDec(int pos, TypeSpec type, String id, int size) {
  	this.pos = pos;
  	this.type = type;
  	this.id = id;
  	this.size = size;
  }
}