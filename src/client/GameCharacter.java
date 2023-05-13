package client;

import common.Movement;
import javafx.scene.image.Image;

/**
 * This class is used to define specific methods for Pacman game characters
 * @author Dillon Orr
 * @version May 2023
 */

public class GameCharacter extends Sprite {
	// The movement speed of Pacman
	private int moveSpeed;
	// This field will hold Pacman's current moving direction
	private Movement currentDirection;
	// This field holds the image name that will be used to change the Sprite
	private String imgName;
	
	/**
	 * Constructor for a game character, an extension of the Sprite
	 * @param imgName The name used to find the image for the character
	 */
	public GameCharacter(String imgName) {
		super();
		this.imgName = imgName;
	}
	
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
    	this.setImage(imgFileName, "left");	
	}

	/**
	 * Method used to move the character right
	 * @param imgFileName the filename of the image to use for the character going right
	 */
	public void moveToRight(String imgFileName) {
		this.setDirection(Movement.RIGHT);
		this.setVelocity(moveSpeed, 0);
		this.setImage(imgFileName, "right");
	}

	/**
	 * Method used to move the character up
	 * @param imgFileName the filename of the image to use for the character going up
	 */
	public void moveToUp(String imgFileName) {
		this.setDirection(Movement.UP);
		this.setVelocity(0, -moveSpeed);
		this.setImage(imgFileName, "up");
	}

	/**
	 * Method used to move the character down
	 * @param imgFileName the filename of the image to use for the character going down
	 */
	public void moveToDown(String imgFileName) {
		System.out.println(imgFileName + " moving down, speed: " + moveSpeed);
		this.setDirection(Movement.DOWN);
		this.setVelocity(0, moveSpeed);
    	this.setImage(imgFileName, "down");    	
	}
	
	/**
	 * Method used to move with a given direction
	 * @param direction The direction to move
	 * @param posY The y position 
	 * @param posX The x position
	 */
	public void movement(Movement direction, int posY, int posX) {
		this.setPosition(posX, posY);
		
		switch(direction) {
		case UP:
			this.moveToUp(imgName);
			break;
		case DOWN:
			this.moveToDown(imgName);
			break;
		case LEFT:
			this.moveToLeft(imgName);
			break;
		case RIGHT:
			this.moveToRight(imgName);
			break;
		case STILL:
			this.stopMove();
			break;
		}
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
	
	/**
	 * Returns the image name as a String
	 */
	public String toString() {
		return imgName;
	}
	
	/**
	 * Sets the image of the character sprite
	 * @param filename 
	 * @param direction
	 */
	public void setImage(String filename, String direction) {
		super.setImage(this.imgName + "_" + direction + "_25.png");
	}
	
	/**
	 * Returns the image name used by the character
	 * @return the image name used by the character
	 */
	public String getImageName() {
		return this.imgName;
	}
	
	
}
