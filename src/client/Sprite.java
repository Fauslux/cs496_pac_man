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
	
	private Image image;
	public double positionX;
	public double positionY;
	private double velocityX;
	private double velocityY;
	private double width;
	private double height;
	
	public Sprite() {
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}
	
	public void setImage(Image i) {
		image = i;
		width = i.getWidth()/2;
		height = i.getHeight()/2;
	}
	
	
	public void setImage(String filename) {
		try {
			Image localImage = new Image(getClass().getResource(
					"/game_resources/" + filename).toURI().toString());
			setImage(localImage);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}
	
	public double getVelocityX() {
		return this.velocityX;
	}
	
	public double getVelocityY() {
		return this.velocityY;
	}
	
	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY);
	}
	
	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}
	
	public boolean intersects(Sprite s) {
		//TODO: check for different intersection capabilities
		return s.getBoundary().intersects(this.getBoundary());
	}
	
	public void hide() {
		this.image = null;
	}
	
	public String toString() {
		return ("Position: [" + positionX + "," + positionY + "]" +
				" Velocity: [" + velocityX + "," + velocityY + "]");
	}

}
