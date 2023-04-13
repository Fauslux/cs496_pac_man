package client;

import common.Movement;

/**
 * This class represents a ghost enemy in Pacman. When Pacman runs into a ghost, he dies.
 * @author Dillon Orr
 * @version March 2023
 */

public class Ghost extends GameCharacter {
	// The default movement speed of a ghost
	private final int GHOSTSPEED = 60;
	// The name used to access images of the Ghost
	private String imgName;
	
	/**
	 * Constructor for a Ghost object
	 */
	public Ghost() {
		super("");
		this.setMoveSpeed(GHOSTSPEED);
		this.setDirection(Movement.STILL);
	}
	
	/**
	 * Constructor for a Ghost object
	 */
	public Ghost(String imgName) {
		super(imgName);
		this.setMoveSpeed(GHOSTSPEED);
		this.setDirection(Movement.STILL);
		this.imgName = imgName;
	}

}
