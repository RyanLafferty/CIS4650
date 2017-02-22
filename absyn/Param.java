package absyn;

public class Param extends Exp {

  public TypeSpec type;
  public String id;

  public Param(TypeSpec type, String id) {
  	this.type = type;
  	this.id = id;
  }
}