package absyn;

public class Param extends Exp {

  public TypeSpec type;
  public String ID;

  public Param(TypeSpec type, String ID) {
  	this.type = type;
  	this.ID = ID;
  }
}