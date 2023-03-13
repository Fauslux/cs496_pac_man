package cs496_pac_man;

/**
 * This class represents the Pacman game character 
 * @author Kevin Richardson
 * @version 2023
 */
public class Pacman extends GameCharacter {
	// The default movement speed of Pacman
	private final int PACSPEED = 100;
	
	/**
	 * Constructor for a Pacman object
	 */
	public Pacman() {
		super();
		this.setDirection(Movement.STILL);
		this.setMoveSpeed(PACSPEED);
	}
	
	


}
