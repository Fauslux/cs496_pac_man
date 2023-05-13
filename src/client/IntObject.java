package client;

/**
 * This class is used to hold an integer object for the game
 * @author Kevin Richardson, Dillon Orr
 * @version May 2023
 */

public class IntObject {
	// The integer value of the object
    private int value;
    
    /**
     * Constructor for an IntObject with a given int
     * @param i The starting integer
     */
    public IntObject(int i)
    {
        value = i;
    }
    
    /**
     * Increments the int by 1
     */
    public void increment() {
    	this.value++;
    }
    
    /**
     * Decrements the int by 1
     */
    public void decrement() {
    	this.value--;
    }
    
    /**
     * Increments the int by a given value 
     * @param value The given value
     */
    public void increment(int value) {
    	this.value += value;
    }
    
    /**
     * Returns the integer value
     * @return the integer value
     */
    public int getValue() {
    	return this.value;
    }
    
    /**
     * Sets the integer value
     * @param value The value to set with
     */
    public void setValue(int value) {
    	this.value = value;
    }
}