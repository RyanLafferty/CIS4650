package absyn;

public class AssignExp extends Exp {
  public VarExp lhs;
  public Exp rhs;
  public TypeSpec type;

  public AssignExp( int pos, VarExp lhs, Exp rhs ) {
    this.pos = pos;
    this.lhs = lhs;
    this.rhs = rhs;
  }
  public AssignExp( int pos, VarExp lhs, Exp rhs, TypeSpec type ) {
    this.pos = pos;
    this.lhs = lhs;
    this.rhs = rhs;
    this.type = type;
  }
}
