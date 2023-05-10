package common;

/**
 * This class represents the map that will be used in Pac-Man. The map is 28 by 31 tiles, 
 * and is based on a real Pac-Man map. Each tile utilizes a certain value based on the tile 
 * content, with the values being mapped in the TileValue enumeration.
 * @author Dillon Orr
 * @version March 2023
 */

public class TileMap {
	// The number of horizontal tiles in the TileMap.
	public final static int HORIZONTALTILES = 28;
	// The number of vertical tiles in the TileMap.
	public final static int VERTICALTILES = 31;
	// The index of the y-coordinate in location arrays.
	public final static int YCOORDINATE = 0;
	// The index of the x-coordinate in location arrays.
	public final static int XCOORDINATE = 1;
	// The initial/starting location of Pacman as an array of 2 integers.
	public final static int[] INITIALPACLOCATION = {23, 13};
	// The initial/starting location of Blinky as an array of 2 integers.
	public final static int[] INITIALBLINKYLOCATION = {11, 13};
	// The initial/starting location of Blinky as an array of 2 integers.
	public final static int[] INITIALPINKYLOCATION = {14, 9};
	// The initial/starting location of Blinky as an array of 2 integers.
	public final static int[] INITIALINKYLOCATION = {17, 13};
	// The initial/starting location of Blinky as an array of 2 integers.
	public final static int[] INITIALCLYDELOCATION = {14, 17};	
	
	// This is the main map that will be used in the Pac-Man game, utilizing values to represent
	// certain tile content.
	// The first index will be the y-coordinate, the second index will be the x-coordinate.
	// !TODO: Unblock tunnels and add tunnel teleportation
	private int[][] map = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 2, 2, 2, 6, 2, 2, 2, 2, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 2, 2, 2, 2, 2, 2, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 2, 2, 7, 0, 2, 2, 2, 2, 2, 2, 0, 9, 2, 2, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 2, 2, 2, 2, 2, 2, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 2, 2, 2, 8, 2, 2, 2, 2, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 5, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0},
			{0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0},
			{0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
			{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
	
	/**
	 * Constructor for a TileMap.
	 */
	public TileMap() {
		// !!!TODO: Add settings variables (# tiles, locations) for different maps?
	}
	
	/**
	 * Method used to get the integer value of the next tile, based on direction
	 * @param currentTile An array of 2 integers indicating the current tile
	 * @param direction The direction from the current tile
	 * @return the integer tile value of the next tile
	 */
	public int nextTileValue(int[] currentTile, Movement direction) {
		int[] nextTile = currentTile.clone();
		int nextTileValue = TileValue.WALL.getValue();
		
		if (direction == Movement.UP) {
			// The tile above Pacman will be Pacman's location with y-1.
			nextTile[YCOORDINATE] = currentTile[YCOORDINATE]-1;
		} else if (direction == Movement.DOWN) {
			// The tile below Pacman will be Pacman's location with y+1.
			nextTile[YCOORDINATE] = currentTile[YCOORDINATE]+1;
		} else if (direction == Movement.LEFT) {
			// The tile above Pacman will be Pacman's location with x-1.
			int nextX = currentTile[XCOORDINATE]-1;
			nextTile[XCOORDINATE] = currentTile[XCOORDINATE]-1;
		} else if (direction == Movement.RIGHT) {
			// The tile above Pacman will be Pacman's location with x+1.
			nextTile[XCOORDINATE] = currentTile[XCOORDINATE]+1;
		} 
		
		// Check if the next tile is within the bounds of the map
		if (nextTile[YCOORDINATE] < VERTICALTILES && nextTile[YCOORDINATE] >= 0 &&
			nextTile[XCOORDINATE] < HORIZONTALTILES && nextTile[XCOORDINATE] >= 0) {
			
			// If the next tile is within the map, get it's value
			nextTileValue = map[nextTile[YCOORDINATE]][nextTile[XCOORDINATE]];
		}
		
		return nextTileValue;
	}
	
	/**
	 * Returns the matrix of the tile map
	 * @return the matrix of the tile map
	 */
	public int[][] getMap() {
		return this.map;
	}
	
	
}
