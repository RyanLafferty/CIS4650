package absyn;

public class Param extends Exp {

  public TypeSpec type;
  public String id;

  public Param(int pos, TypeSpec type, String id) {
  	this.pos = pos;
  	this.type = type;
  	this.id = id;
  }
}