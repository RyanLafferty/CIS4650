package absyn;

public class FunDec extends Dec {
  public TypeSpec type;
  public String id;
  public DecList plist;
  public Dec cstmt;
  public FunDec(int pos, TypeSpec type, String id, DecList plist, Dec stmt) {
    this.pos = pos;
    this.type = type;
    this.id = id;
    this.plist = plist;
    this.cstmt = cstmt;
  }
}
