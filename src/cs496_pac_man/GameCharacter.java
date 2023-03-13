package cs496_pac_man;

/**
 * This class is used to define specific methods for Pacman game characters
 * @author Dillon Orr
 * @version March 2023
 */

public class GameCharacter extends Sprite {
	// The movement speed of Pacman
	private int moveSpeed;
	// This field will hold Pacman's current moving direction
	private Movement currentDirection;
	
	/**
	 * Method used to stop the character from moving
	 */
	public void stopMove() {
		this.setVelocity(0, 0);
		this.setDirection(Movement.STILL);
	}
	
	/**
	 * Method used to move the character left
	 * @param imgFileName the filename of the image to use for the character going left
	 */
	public void moveToLeft(String imgFileName) {
		this.setDirection(Movement.LEFT);
		this.setVelocity(-moveSpeed, 0);
    	this.setImage(imgFileName);
	}

	/**
	 * Method used to move the character right
	 * @param imgFileName the filename of the image to use for the character going right
	 */
	public void moveToRight(String imgFileName) {
		this.setDirection(Movement.RIGHT);
		this.setVelocity(moveSpeed, 0);
		this.setImage(imgFileName);
	}

	/**
	 * Method used to move the character up
	 * @param imgFileName the filename of the image to use for the character going up
	 */
	public void moveToUp(String imgFileName) {
		this.setDirection(Movement.UP);
		this.setVelocity(0, -moveSpeed);
		this.setImage(imgFileName);
	}

	/**
	 * Method used to move the character down
	 * @param imgFileName the filename of the image to use for the character going down
	 */
	public void moveToDown(String imgFileName) {
		this.setDirection(Movement.DOWN);
		this.setVelocity(0, moveSpeed);
    	this.setImage(imgFileName);
	}

	/**
	 * Returns the current Movement direction of the character
	 * @return the current Movement direction of the character
	 */
	public Movement getDirection() {
		return this.currentDirection;
	}
	
	/**
	 * Sets the direction of the character
	 * @param direction the new direction that the character will be moving
	 */
	public void setDirection(Movement direction) {
		this.currentDirection = direction;
	}
	
	/**
	 * Sets the movespeed
	 * @param movespeed the new movespeed of the character
	 */
	public void setMoveSpeed(int movespeed) {
		this.moveSpeed = movespeed;
	}
}
