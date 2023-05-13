package client;

/**
 * This class is the GUI object of a pellet in the Pacman game
 * @author Kevin Richardson, Dillon Orr
 * @version April 2023
 */

public class Pellet extends Sprite {
	// Boolean that determines if the pellet has been eaten by Pac-Man
	private boolean isEaten;
	
	/**
	 * Construcor for the pellet object
	 * @param isEaten If the pellet has been eated
	 */
	public Pellet(boolean isEaten) {
		this.isEaten = isEaten;
	}
	
	/**
	 * Returns the boolean value of if the pellet has been eaten
	 * @return the boolean value of the pellet being eated
	 */
	public boolean getIsEaten() {
		return this.isEaten;
	}
	
	/**
	 * Set the value of the pellet being eaten
	 * @param isEaten The new value of being eaten
	 */
	public void setIsEaten(boolean isEaten) {
		this.isEaten = isEaten;
	}
}
