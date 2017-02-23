package absyn;

public class ArrayVar extends Var {

  public String id; 
  public Exp number;
  public int pos;	

  public ArrayVar(int pos, String id, Exp number) {
  	this.pos = pos;
  	this.id = id;
  	this.number = number;
  }
}