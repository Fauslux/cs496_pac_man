package common;

/**
 * This enumeration represents the movements that will be used in the TileMap. This will be used
 * to translate inputs into movements in the map.
 * @author Dillon Orr
 * @version March 2023
 */

public enum Movement {
	UP (0, "UP"), 
	DOWN (1, "DOWN"),
	LEFT (2, "LEFT"),
	RIGHT (3, "RIGHT"),
	STILL (4, "STILL");
	
	// The integer value that is tied to a movement direction.
	private int moveValue;
	// The string  that is tied to a movement direction.
	private String moveString;
	
	/**
	 * Constructor for a Movement enumeration value
	 * @param moveValue An ID integer used to represent the movement 
	 * @param moveString A String used to represent the movement
	 */
	Movement(int moveValue, String moveString) {
		this.moveValue = moveValue;
		this.moveString = moveString;
	}
	
	/**
	 * Returns the integer value of movement
	 * @return the integer value of movement
	 */
	public int getValue() {
		return this.moveValue;
	}
	
	/**
	 * Returns the string value of movement
	 * @return the string value of movement
	 */
	public String getString() {
		return this.moveString;
	}
	
}
