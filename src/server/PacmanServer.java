package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import common.Characters;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import common.Movement;
import common.TileMap;
import common.TileValue;

public class PacmanServer implements MessageListener {
	// Constant that determines the maximum amount of players
	private static final int MAXPLAYERS = 5;
	// Constant that determines the minimum amount of players
	private static final int MINPLAYERS = 1;
	// Constant that determings the starting amount of Pacman's lives
	private final int STARTINGLIVES = 2;
    // The server socket
    private ServerSocket serverSocket;
    // HashMap that holds a client's username and a corresponding ConnectionAgent
    private HashMap<ConnectionAgent, String> agents; 
    // ArrayList that holds a client's ConnectionAgent (used when order matters)
    private ArrayList<ConnectionAgent> agentsArrayList;
    // StringBuilder that builds a player list when players join
    private StringBuilder stringPlayerList;
    // HashMap that maps a client to a specific game character
    private HashMap<ConnectionAgent, Integer> clientCharacterID;
    // TileMap that the game will use
    private TileMap tileMap;
    // HashMap that maps a client to their score
    private HashMap<ConnectionAgent, Integer> clientScores;
    // HashMap that maps a client to their ready status
    private HashMap<ConnectionAgent, Boolean> clientReady;
    // Pacman's current score
    private int score;
    // Keeps track of how many pellets have been eaten
    private int pelletsEaten;
    // How many total pellets there are in the tile map 
    private int totalPellets;
    // How many lives the current Pacman has
    private int currentPacLives;
    // Boolean determinant for if the game is started
    private boolean gameStarted;
    // Boolean determinant for if the game is over
    private boolean gameOver;
    
	
    /**
     * Constructor for a Pacman game server
     * @param port the port number
     * @throws IOException 
     */
    public PacmanServer(int port) throws IOException {
    	this.serverSocket = new ServerSocket(port);
    	this.agents = new HashMap<>();
    	this.clientCharacterID = new HashMap<>();
    	this.clientScores = new HashMap<>();
    	this.clientReady = new HashMap<>();
    	this.stringPlayerList = new StringBuilder();
    	this.gameStarted = false;
    	this.agentsArrayList = new ArrayList<>();

    	setupGame();

    }
    
    /**
     * The method that will initialize the game
     */
    public void setupGame() {
    	this.tileMap = new TileMap();
    	this.score = 0;
    	this.pelletsEaten = 0;
    	this.totalPellets = this.countPelletsInMap();
    }
    
    /**
     * This method will listen for client requests. 
     * @throws IOException 
     */
    public void listen() throws IOException {
        while(!serverSocket.isClosed()){
            try{
            	// Currently, the game will only hold 5 players
            	
            	if (agents.size() < MAXPLAYERS) {
            		if (!this.gameStarted) {
	            		System.out.println("Game Started: " + this.gameStarted);
	            		Socket socket = serverSocket.accept();
	                    ConnectionAgent agent = new ConnectionAgent(socket);
	                    
	                    // !!!!! TODO: get player names from API !!!!!
	                    String playerName = "player" + agents.size();
	                    if (agents.size() == 0) {
	                    	// If the first player is being added, do not add a space before
	                        this.stringPlayerList.append(playerName);
	                    } else {
	                    	// If any player past the first is being added, add a space before
	                    	this.stringPlayerList.append(" " + playerName);
	                    }
	                    // Tell all players to update their player list
	                    System.out.println("Broadcasting new player");
	                    broadcast("newplayer:" + playerName);
	
	                    this.agents.put(agent, playerName);
	                    this.agentsArrayList.add(agent);
	                    this.clientReady.put(agent, false);
	                    // Set client's character based on when they joined (first is Pacman, etc.)
	                    int charID = clientCharacterID.size();
	                	this.clientCharacterID.put(agent, charID);
	                	
	
	                    agent.addMessageListener(this);
	                    Thread serverThread = new Thread(agent);
	                    serverThread.start();
	            	} 
            	}
            } catch(IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
    
    /**
     * Broadcasts the message to all ConnectionAgents
     * @param message the message to broadcast
     */
    public void broadcast(String message) {
        for(Map.Entry<ConnectionAgent, String> entry : this.agents.entrySet())     {
        	ConnectionAgent agent = entry.getKey();
        	System.out.println("Broadcasting to " + agent);
        	// Removed \n from sent message
        	agent.sendMessage(message + "");
        }
    }
    
	@Override
	public void messageReceived(String message, MessageSource source) {
		System.out.println("Message received: " + message);
		
		// Split the message after the command character, :
		String[] messageArray = message.split(":");
		// The first part of the split message is the command that the server sent
		String command = messageArray[0];
		// The second part is the parameters/options that were sent with the command
		String options = "";
		if (messageArray.length > 1) {
			options = messageArray[1];
		}
		// Get the ConnectionAgent of the client who sent the message
        ConnectionAgent clientAgent = (ConnectionAgent) source;
        
        // Get the game character of the client who sent the message
        int charID = this.clientCharacterID.get(clientAgent);
        Characters clientCharacter = Characters.getCharacterUsingID(charID);
		
        if (command.contains("getplayerlist")) {
        	clientAgent.sendMessage("newplayerlist:" + stringPlayerList);
        } else if (command.contains("move")) {
        	// Validate move first and broadcast a valid move to all players
        	// TODO: Validate move
        	String[] moveArray = options.split(" ");
        	// moveArray[0] is the KeyCode ID
        	int keyID = Integer.parseInt(moveArray[0]);
        	Movement direction = getMovementUsingID(keyID);
        	// moveArray[1] is the client character's x tile position
        	int posY = Integer.parseInt(moveArray[1]);
        	// moveArray[2] is the client character's y tile position
        	int posX = Integer.parseInt(moveArray[2]);
        	
        	if (validateMove(direction, posX, posY)) {
        		broadcast("move:" + clientCharacter.getID() + " " + options + " ");
        	} else {
        		direction = Movement.STILL;
        		broadcast("move:" + clientCharacter.getID() + " " + direction + " " + posY + " " + posX + " ");
        	}

        	
        } else if (command.contains("pelletcollected") && 
        			clientCharacter.equals(Characters.PACMAN)) {
        	// If Pacman collects a pellet, update everyone's score
        	this.score += 100;
        	broadcast("pelletcollected:" + options + " ");
        	this.pelletsEaten++;
        	if (this.pelletsEaten == this.totalPellets) {
        		broadcast("refreshpellets:");
        		this.pelletsEaten = 0;
        	}
        	
        } else if (command.contains("ready")) {
        	// If the player has indicated they are ready, set their ready status
        	this.clientReady.replace(clientAgent, true);
        	
        	// Check if every player is ready
        	int readyCount = 0;
	    	for(Map.Entry<ConnectionAgent, Boolean> entry : this.clientReady.entrySet())     {
	    		if (entry.getValue()) {
	    			readyCount++;
	    		}
	    	}
	    	// !!!!! TODO: Should we allow singleplayer games that just play against AI?
	    	// TODO: Also, should we allow spectators?
	    	if (readyCount == agents.size() && agents.size() >= MINPLAYERS) {
	    		// If every player is ready (and at least 2 players), tell them to start
	    		for(Map.Entry<ConnectionAgent, Integer> entry : this.clientCharacterID.entrySet())     {
	            	ConnectionAgent agent = entry.getKey();
	            	agent.sendMessage("character:" + entry.getValue() + " ");
	            }
	    		this.gameStarted = true;
	    		this.currentPacLives = this.STARTINGLIVES;
	    		broadcast("startgame:" + agents.size() + " " + this.currentPacLives + " ");
	    	}
        } else if (command.contains("hitghost") && clientCharacter == Characters.PACMAN) {
        	// !!!!! TODO: If the player is the last player, then the whole game is over
        	// Broadcast that the 
        	broadcast("livelost:" + agents.get(clientAgent) + " ");
        	this.currentPacLives--;
        	this.pelletsEaten = 0;
        	
        	// If the current Pacman is out of lives, switch to the next player
        	
    		if (this.currentPacLives == 0) {
    			// Broadcast the highscore of the Pacman player
    			broadcast("highscore:" + this.agentsArrayList.indexOf(clientAgent) + 
																" " + this.score + " ");
        		this.clientScores.put(clientAgent, this.score);
        		this.score = 0;

    			if (isLastPlayer(clientAgent)) {
            		// If the player is the last Pacman, the game is over
    				broadcast("gameover:");
    				this.gameOver = true;
    				
    			} else {
            		
            		// Hold for 5 seconds to give a break between rounds
            		try {
        				Thread.sleep(5000);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}
            		changeTurn();
            		this.currentPacLives = this.STARTINGLIVES;
            		
    				// Tell players to restart their game rendering with a new turn
                	broadcast("newturn:" + this.STARTINGLIVES + " ");
    			}
        	} else if (!this.gameOver) {
        		// Hold for 5 seconds to give a break between rounds
        		try {
    				Thread.sleep(5000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        		
        		// Tell players to restart their game rendering
            	broadcast("continuegame:");
        	}
        }
	}
	
	/**
	 * Method used to determine if the client is the last player 
	 * @param clientAgent The client that is being checked
	 * @return true if the client is the last player
	 */
	private boolean isLastPlayer(ConnectionAgent clientAgent) {
		boolean result = false;
		ConnectionAgent lastAgent = this.agentsArrayList.get(this.agentsArrayList.size() - 1);
		
		if (clientAgent.equals(lastAgent)) {
			System.out.println(this.agents.get(lastAgent) + " is the last player");
			result = true;
		}
		
		return result;
	}

	/**
	 * Mtehod used when the current Pacman loses all of their lives, switching to the next player
	 */
	private void changeTurn() {
		for(Map.Entry<ConnectionAgent, Integer> entry : this.clientCharacterID.entrySet())     {
        	ConnectionAgent agent = entry.getKey();
        	// Get the new character's ID by doing [(current ID + 1) mod (number of players)]
        	//int newCharacterID = (entry.getValue() - 1) % this.clientCharacterID.size();
        	int newCharacterID = Math.floorMod(entry.getValue() - 1, this.clientCharacterID.size());
        	entry.setValue(newCharacterID);
        	// Tell the client's their new character
        	agent.sendMessage("character:" + entry.getValue() + " ");
        }
	}

	/**
	 * Used to validate a move of a client's character based on their input direction and position
	 * @param direction The direction that the client wants their character to move
	 * @param posX The X value of the client character's position in the map
	 * @param posY The Y value of the client character's position in the map
	 * @return true if the move is valid
	 */
	private boolean validateMove(Movement direction, int posX, int posY) {
		boolean result = true;
		// If the direction to be moved is not staying still, check if it's a valid move
		if (direction != Movement.STILL) {
			// Get the value of the next tile using a current tile
			int[] currentTile = {posY, posX};
			int nextTileValue = tileMap.nextTileValue(currentTile, direction);
			// If the next tile is a wall, then the move is not valid
			if (nextTileValue == TileValue.WALL.getValue()) {
				result = false;
			}
		}	
		
		return result;
	}

	@Override
	public void sourceClosed(MessageSource source) {
		System.out.println("Source closed: " + source);
		// TODO: Remove clients when they leave, tell other clients
		
		// TODO: Change their movement to AI, or remove their character from the game?
	}

	/**
	 * Helper method used to get a movement direction using a KeyCode ID
	 * @param id The ID of the KeyCode
	 * @return The Movement direction
	 */
	public Movement getMovementUsingID(int id) {
		Movement movement = null;
		
		if (id == Movement.UP.getValue()) {
			movement = Movement.UP;
		} else if (id == Movement.DOWN.getValue()) {
			movement = Movement.DOWN;
		} else if (id == Movement.LEFT.getValue()) {
			movement = Movement.LEFT;
		} else if (id == Movement.RIGHT.getValue()) {
			movement = Movement.RIGHT;
		} else if (id == Movement.STILL.getValue()) {
			movement = Movement.STILL;
		} 
		
		return movement;
	}
	
	/**
	 * Counts the number of pellets in the tile map
	 * @return the number of pellets in the tile map
	 */
	private int countPelletsInMap() {
		int[][] map = tileMap.getMap();
		int pellets = 0;
		
		for (int i = 1; i < map.length; i++) {
			for (int j = 1; j < map[i].length; j++) {
				if (map[i][j] == TileValue.PELLET.getValue()) {
					pellets++;
				}
			}
		}
		
		return pellets;
	}
}
