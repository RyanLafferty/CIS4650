package absyn;

public class Expr extends Dec {
  public VarDec var;
  public Dec expression;
  public Dec sime;
  public Expr( int pos, VarDec var, Dec expression) {
    this.pos = pos;
    this.var = var;
    this.expression = expression;
  }
}
