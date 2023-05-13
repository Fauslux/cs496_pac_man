package client;

import java.util.ArrayList;
import java.util.Random;

import common.Movement;
import common.TileValue;

/**
 * This class is an AI for a ghost that randomly moves around a TileMap.
 * @author Dillon Orr
 * @version March 2023
 */
public class BlinkyAI extends Ghost {
	
	/**
	 * Constructor for a BlinkyAI object
	 */
	public BlinkyAI(String imgName) {
		super(imgName);
	}
	
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
		System.out.println(possibleMoves.size());
		int randomIndex = random.nextInt(possibleMoves.size());
		return possibleMoves.get(randomIndex);
	}
}
