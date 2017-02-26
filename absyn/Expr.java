package absyn;

public class Expr extends Dec {
  public Var var;
  public Dec expression;
  public Dec sime;
  public Expr( int pos, Var var, Dec expression) {
    this.pos = pos;
    this.var = var;
    this.expression = expression;
  }
  public Expr( int pos, Dec sime) {
    this.pos = pos;
    this.sime = sime;
  }
}
