package absyn;
import java.io.*;
import java.util.*;

public class variable {

	public String name;
	public int value;
    public int offset;

    public variable(String name) {
        this.name = name;
    }

	public variable(String name, int value) {
		this.name = name;
		this.value = value;
	}
}