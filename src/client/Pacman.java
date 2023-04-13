package client;

import common.Movement;

/**
 * This class represents the Pacman game character 
 * @author Kevin Richardson
 * @version 2023
 */
public class Pacman extends GameCharacter {
	// The default movement speed of Pacman
	private final int PACSPEED = 100;
	// The name of Pacman images
	private static final String pacImg = "pac";
	// Field that holds whether Pacman is dead or not
	private boolean isDead;
	
	/**
	 * Constructor for a Pacman object
	 */
	public Pacman() {
		super(pacImg);
		this.setDirection(Movement.STILL);
		this.setMoveSpeed(PACSPEED);
		this.isDead = false;
	}
	
	/**
	 * Returns the value that determines if Pacman is dead
	 * @return the value that determines if Pacman is dead
	 */
	public boolean getIsDead() {
		return this.isDead;
	}
	
	/**
	 * Set the the vlaue of the boolean that determines if Pacman is dead
	 * @param isDead the new value of death
	 */
	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}
	


}
