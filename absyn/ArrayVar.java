package absyn;

public class ArrayVar extends Var {

  public String id; 
  public Dec number;
  public int pos;	

  public ArrayVar(int pos, String id, Dec number) {
  	this.pos = pos;
  	this.id = id;
  	this.number = number;
  }
}