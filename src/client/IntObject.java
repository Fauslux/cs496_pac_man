package client;

/**
 * This class is used to hold an integer object for the game
 * @author Kevin Richardson, Dillon Orr
 * @version May 2023
 */

public class IntObject {
    private int value;
    
    public IntObject(int i)
    {
        value = i;
    }
    
    public void increment() {
    	this.value++;
    }
    
    public void decrement() {
    	this.value--;
    }
    
    public void increment(int value) {
    	this.value += value;
    }
    
    public int getValue() {
    	return this.value;
    }
    
    public void setValue(int value) {
    	this.value = value;
    }
}