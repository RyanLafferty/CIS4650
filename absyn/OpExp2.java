package absyn;

public class OpExp2 extends Dec {
  public final static int PLUS  = 0;
  public final static int MINUS = 1;
  public final static int STAR = 2;
  public final static int SLASH  = 3;
  public final static int EQ    = 4;
  public final static int LT    = 5;
  public final static int GT    = 6;
  public final static int LTE    = 7;
  public final static int GTE    = 8;
  public final static int NE    = 9;

  public Dec left;
  public int op;
  public Dec right;
  public OpExp2( int pos, Dec left, int op, Dec right ) {
    this.pos = pos;
    this.left = left;
    this.op = op;
    this.right = right;
  }
}
