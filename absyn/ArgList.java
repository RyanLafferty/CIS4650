package absyn;

public class ArgList{
  public Dec head;
  public ArgList tail;
  public ArgList( Dec head, ArgList tail ) {
    this.head = head;
    this.tail = tail;
  }
}
