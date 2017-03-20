package absyn;

public class RegularDec extends VarDec {

  public TypeSpec type = null;
  public String id = "";
  public int size = -1;

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