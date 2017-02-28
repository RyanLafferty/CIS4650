package absyn;

public class ParamList{
  public Dec head;
  public ParamList tail;
  public ParamList( Dec head, ParamList tail ) {
    this.head = head;
    this.tail = tail;
  }
}
