package client;

import common.Movement;

/**
 * This class represents a ghost enemy in Pacman. When Pacman runs into a ghost, he dies.
 * @author Dillon Orr
 * @version March 2023
 */

public class Ghost extends GameCharacter {
	// The default movement speed of a ghost
	// !TODO: Change to 60 when testing with 4/21/2023 version
	private final int GHOSTSPEED = 60;
	// The name used to access images of the Ghost
	private String imgName;
	
	/**
	 * Constructor for a Ghost object
	 */
	public Ghost() {
		this("blinky");
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
