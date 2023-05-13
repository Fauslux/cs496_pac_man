package client;


import java.net.URISyntaxException;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents a sprite (visual part) used in the game
 * @author Kevin Richardson
 * @version March 2023
 */
public abstract class Sprite {
	// The JavaFX image used by the sprite
	private Image image;
	// The x-value of the position of the sprite
	public double positionX;
	// The y-value of the position of the sprite
	public double positionY;
	// The x-value of the velocity of the sprite
	private double velocityX;
	// The y-value of the velocity of the sprite
	private double velocityY;
	// The width of the sprite
	private double width;
	// The height of the sprite
	private double height;
	
	/**
	 * Constructor for a Sprite object
	 */
	public Sprite() {
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}
	
	/**
	 * Set the image of the Sprite using a given JavaFX Image object
	 * @param i The given image
	 */
	public void setImage(Image i) {
		image = i;
		width = i.getWidth()/2;
		height = i.getHeight()/2;
	}
	
	/**
	 * Set the image of the Sprite using a given filename
	 * @param filename The filename to set the image with
	 */
	public void setImage(String filename) {
		try {
			Image localImage = new Image(getClass().getResource(
					"/game_resources/" + filename).toURI().toString());
			setImage(localImage);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the position of the sprite
	 * @param x The x-value to set 
	 * @param y The y-value to set
	 */
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	/**
	 * Set the velocity of the sprite
	 * @param x The x-value to set
	 * @param y THe y-value to set
	 */
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}
	
	/**
	 * Returns the x-value of the velocity 
	 * @return the x-value of the velocity
	 */
	public double getVelocityX() {
		return this.velocityX;
	}
	
	/**
	 * Returns the y-value of the velocity 
	 * @return the y-value of the velocity
	 */
	public double getVelocityY() {
		return this.velocityY;
	}
	
	/**
	 * Add to the velocity of the sprite
	 * @param x The x-value to add with
	 * @param y The y-value to add with
	 */
	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	/**
	 * Update the sprite's position based on a given amount of time
	 * @param time The time to update with
	 */
	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}
	
	/**
	 * Render the sprite on a given GraphicsContext
	 * @param gc The given GraphicsContext
	 */
	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY);
	}
	
	/**
	 * Returns the boundary of the sprite as a 2D rectangle
	 * @return the boundary of the sprite
	 */
	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}
	
	/**
	 * Check if the sprite intersects with another
	 * @param s The other sprite to check if there is an intersection
	 * @return true if this sprite intersects with the other
	 */
	public boolean intersects(Sprite s) {
		//TODO: check for different intersection capabilities
		return s.getBoundary().intersects(this.getBoundary());
	}
	
	/**
	 * Hide the sprite by removing the image
	 */
	public void hide() {
		this.image = null;
	}
	
	/**
	 * Returns the position and velocity of the sprite
	 */
	public String toString() {
		return ("Position: [" + positionX + "," + positionY + "]" +
				" Velocity: [" + velocityX + "," + velocityY + "]");
	}

}
