package client;

import common.Movement;

/**
 * This class represents a ghost enemy character in the game of Pacman. 
 * @author Dillon Orr
 * @version March 2023
 */

public class Ghost extends GameCharacter {
	// The default movement speed of a ghost
	private final int GHOSTSPEED = 100;
	// The name used to access images of the Ghost
	private String imgName;
	
	/**
	 * Constructor for a Ghost object with no given image name
	 */
	public Ghost() {
		this("blinky");
	}
	
	/**
	 * Constructor for a ghost object
	 * @param imgName The image name used for the ghost's sprite
	 */
	public Ghost(String imgName) {
		super(imgName);
		this.setMoveSpeed(GHOSTSPEED);
		this.setDirection(Movement.STILL);
		this.imgName = imgName;
	}
}
