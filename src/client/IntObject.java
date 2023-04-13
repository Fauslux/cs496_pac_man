package client;

public class IntObject
{
    private int value;
    
    public IntObject(int i)
    {
        value = i;
    }
    
    public void increment() {
    	this.value++;
    }
    
    public int getValue() {
    	return this.value;
    }
    
    public void setValue(int value) {
    	this.value = value;
    }
}