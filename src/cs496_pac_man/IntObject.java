package cs496_pac_man;

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
}