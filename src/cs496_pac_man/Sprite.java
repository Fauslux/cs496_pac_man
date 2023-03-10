package cs496_pac_man;


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
		width = i.getWidth();
		height = i.getHeight();
	}
	
	
	public void setImage(String filename) {
		Image localImage = new Image(filename);
		setImage(localImage);
	}
	
	
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
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
