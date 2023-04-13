package common;

import javafx.scene.input.KeyCode;

/**
 * This enumeration represents the movements that will be used in the TileMap. This will be used
 * to translate inputs into movements in the map.
 * @author Dillon Orr
 * @version March 2023
 */

public enum Movement {
	UP (KeyCode.UP.getCode(), "UP"), 
	DOWN (KeyCode.DOWN.getCode(), "DOWN"),
	LEFT (KeyCode.LEFT.getCode(), "LEFT"),
	RIGHT (KeyCode.RIGHT.getCode(), "RIGHT"),
	STILL (0, "STILL");
	
	// The integer value that is tied to a movement direction.
	private int moveValue;
	// The string  that is tied to a movement direction.
	private String moveString;
	// The KeyCode that is tied to a direction
	private KeyCode moveKey;
	
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
	
	/**
	 * Returns the KeyCode key
	 * @return the KeyCode key
	 */
	/*
	public KeyCode getKey() {
		return this.moveKey;
	}
	*/
	
	/**
	 * Helper method used to get a movement direction using a KeyCode ID
	 * @param id The ID of the KeyCode
	 * @return The Movement direction
	 */
	/*
	public static Movement getMovementUsingID(int id) {
		Movement movement = null;
		
		if (id == Movement.UP.getKey().getCode()) {
			System.out.println("Movement UP");
			movement = Movement.UP;
		} else if (id == Movement.DOWN.getKey().getCode()) {
			movement = Movement.DOWN;
		} else if (id == Movement.LEFT.getKey().getCode()) {
			movement = Movement.LEFT;
		} else if (id == Movement.RIGHT.getKey().getCode()) {
			movement = Movement.RIGHT;
		} else if (id == Movement.STILL.getKey().getCode()) {
			movement = Movement.STILL;
		} 
		
		System.out.println("Movement is: " + movement);
				
		return movement;
	}
	*/
}
