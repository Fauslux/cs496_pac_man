package common;

/**
 * This enumeration represents a tile's value in the map. As of this version, there are 
 * 10 values. These are: wall, dot, empty-space, power-pellet, fruit, Pac-Man, Blinky,
 * Pinky, Inky, and Clyde.
 * @author Dillon Orr
 * @version March 2023
 */

public enum TileValue {
	WALL (0),
	PELLET (1),
	EMPTY (2),
	POWER (3),
	FRUIT (4),
	PACMAN (5),
	BLINKY (6),
	PINKY (7),
	INKY (8),
	CLYDE (9);
	
	// The content of the tile is tied to a single integer value.
	private int value;
	
	/**
	 * Constructor for a TileValue enumeration entry
	 * @param value The integer value that represents the tile
	 */
	TileValue(int value) {
		this.value = value;
	}
	
	/**
	 * Returns the integer representation of the tile
	 * @return the integer representation of the tile
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Get an enumeration entry using a given integer value 
	 * @param value The integer value being given
	 * @return the corresponding TileValue entry
	 */
	public TileValue getWithValue(int value) {
		TileValue result = WALL;
		
		return result;
	}
	
	
}
