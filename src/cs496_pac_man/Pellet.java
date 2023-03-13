package cs496_pac_man;

public class Pellet extends Sprite {
	// Boolean that determines if the pellet has been eaten by Pac-Man
	private boolean isEaten;
	
	public Pellet(boolean isEaten) {
		this.isEaten = isEaten;
	}
	
	public boolean getIsEaten() {
		return this.isEaten;
	}
	
	public void setIsEaten(boolean isEaten) {
		this.isEaten = isEaten;
	}
}
