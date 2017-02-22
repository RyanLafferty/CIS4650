package absyn;

public class ArrayDec extends Exp {

  public TypeSpec type;
  public String id;
  public String number;

  public ArrayDec(int pos, TypeSpec type, String id, String number) {
  	this.pos = pos;
  	this.type = type;
  	this.id = id;
  	this.number = number;
  }
}