package absyn;

public class Var extends Exp {

  public String id; 	

  public Var(int pos, String id) {
  	this.pos = pos;
  	this.id = id;
  }
}