package cs496_pac_man;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents an AI for the ghost to manuever around the map
 * @author Dillon Orr
 *
 */
public class BlinkyAI extends Ghost {
	
	
	// Used to determine if the ghost AI can move left
	private boolean leftAvailable;
	// Used to determine if the ghost AI can move right
	private boolean rightAvailable;
	// Used to determine if the ghost AI can move up
	private boolean upAvailable;
	// Used to determine if the ghost AI can move down
	private boolean downAvailable;
	
	/**
	 * Method used to determine if the Ghost should move (when the ghost has stopped moving
	 */
	public boolean shouldChange() {
		boolean result = false;
		if (this.getDirection() == Movement.STILL) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Method used to decide what move the ghost should make
	 * @param position
	 */
	public Movement decideMove(int leftValue, int rightValue, int upValue, int downValue) {
		ArrayList<Movement> possibleMoves = new ArrayList<>();
		if (leftValue != TileValue.WALL.getValue()) {
			possibleMoves.add(Movement.LEFT);
		}
		if (rightValue != TileValue.WALL.getValue()) {
			possibleMoves.add(Movement.RIGHT);
		}
		if (upValue != TileValue.WALL.getValue()) {
			possibleMoves.add(Movement.UP);
		}
		if (downValue != TileValue.WALL.getValue()) {
			possibleMoves.add(Movement.DOWN);
		}
		// Get a random index based on the number of possible moves in the array
		Random random = new Random();
		int randomIndex = random.nextInt(possibleMoves.size());
		return possibleMoves.get(randomIndex);
	}
	
	/**
	 * Method used to check moves available to the ghost
	 * @param position
	 */
	public void checkForMove(int[] position) {
		
	}

}
