package absyn;
import java.io.*;
import java.util.*;

public class variable 
{
	public String name;
	public int value = 0;
	public int valueArray[];
	public int arraySize = -1;
    public int offset;
    public boolean isGlobal = false;

	public variable(String name) {
		this.name = name;
	}

    public variable(String name, int offset, boolean isGlobal) {
        this.name = name;
        this.offset = offset;
        this.isGlobal = isGlobal;
    }

	public variable(String name, int arraySize) {
		this.name = name;
		this.arraySize = arraySize;
		this.valueArray = new int[arraySize];
	}
}