package absyn;

public class Symbol {

  public int depth;
  public String sID;
  public TypeSpec type;

  public Symbol(int depth, String sID, TypeSpec type) {
  	this.depth = depth;
  	this.sID = sID;
 	this.type = type;


  }
}
