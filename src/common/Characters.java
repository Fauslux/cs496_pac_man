package common;

/**
 * This enumeration is used to determine which specific character is assigned to what player.
 * @author Dillon Orr
 * @version March 2023
 */

public enum Characters {
	PACMAN(0, "Pacman"),
	BLINKY(1, "Blinky"),
	PINKY(2, "Pinky"),
	INKY(3, "Inky"),
	CLYDE(4, "Clyde");
	
	// An integer ID used to identify the character
	private int characterID;
	// The String name of the character
	private String name;
	
	/**
	 * Constructor for a Character enum 
	 * @param characterID the ID of the character
	 */
	Characters(int characterID, String name) {
		this.characterID = characterID;
		this.name = name;
	}
	
	/**
	 * Returns the character's ID number
	 * @return the character's ID number
	 */
	public int getID() {
		return this.characterID;
	}
	
	/**
	 * Returns the character's name as a String
	 * @return the character's name as a String
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Helper method used to get a game character using a client's id
	 * @param id The ID of the client
	 * @return The game character
	 */
	public static Characters getCharacterUsingID(int id) {
		Characters clientCharacter = null;
		
		if (id == Characters.PACMAN.getID()) {
			clientCharacter = Characters.PACMAN;
		} else if (id == Characters.BLINKY.getID()) {
			clientCharacter = Characters.BLINKY;
		} else if (id == Characters.PINKY.getID()) {
			clientCharacter = Characters.PINKY;
		} else if (id == Characters.INKY.getID()) {
			clientCharacter = Characters.INKY;
		} else if (id == Characters.CLYDE.getID()) {
			clientCharacter = Characters.CLYDE;
		}
				
		return clientCharacter;
	}
	
}
