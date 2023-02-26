package cs496_pac_man;

public class Pacman extends VisibleObject {
	private final int moveSpeed = 50;
	private boolean preventMove = false;
	
	public void stopMove() {
		
	}
	
	@Override
	public void moveToLeft() {
		this.addVelocity(-50,0);
    	this.setImage("pac_left.png");
	}

	@Override
	public void moveToRight() {
		this.addVelocity(50,0);
    	this.setImage("pac_right.png");
	}

	@Override
	public void moveToUp() {
		this.addVelocity(0,-50);
		this.setImage("pac_up.png");
	}

	@Override
	public void moveToDown() {
		this.addVelocity(0,50);
    	this.setImage("pac_down.png");
	}

}
