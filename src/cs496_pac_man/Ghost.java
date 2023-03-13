package cs496_pac_man;

/**
 * This class represents a ghost enemy in Pacman. When Pacman runs into a ghost, he dies.
 * @author Dillon Orr
 * @version March 2023
 */

public class Ghost extends GameCharacter {
	// The default movement speed of a ghost
	private final int GHOSTSPEED = 60;
	
	/**
	 * Constructor for a Ghost object
	 */
	public Ghost() {
		super();
		this.setMoveSpeed(GHOSTSPEED);
		this.setDirection(Movement.STILL);
	}

}
