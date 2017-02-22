package absyn;

public class Array extends Exp {

  public String id; 
  public Exp number;	

  public Array(int pos, String id, Exp number) {
  	this.pos = pos;
  	this.id = id;
  	this.number = number;
  }
}