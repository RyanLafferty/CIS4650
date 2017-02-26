package absyn;

public class FunDec extends Dec {

  public TypeSpec type;
  public String id;
  public ParamList plist;
  public CompStmt cstmt;
  public FunDec(int pos, TypeSpec type, String id, ParamList plist, CompStmt stmt) {
    this.pos = pos;
    this.type = type;
    this.id = id;
    this.plist = plist;
    this.cstmt = cstmt;
  }
}
