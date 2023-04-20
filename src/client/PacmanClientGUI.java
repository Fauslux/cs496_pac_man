package client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import common.Characters;
import common.Movement;
import common.TileMap;
import common.TileValue;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PacmanClientGUI extends Application implements ConsoleListener {
	// Constant that determines the maximum amount of players
	private static final int MAXPLAYERS = 5;
	// Constant that determines the minimum amount of players
	private static final int MINPLAYERS = 2;
	/** This constant represents the size of a square tile (width and height) */
	private static final int TILESIZE = 20;
	/** This holds the current score of Pac-Man */
	private IntObject score;
	/** The Pacman game object */
	private Pacman pac;
	/** The Blinky game object */
	private Ghost blinky;
	/** The Blinky game object */
	private Ghost pinky;
	/** The Blinky game object */
	private Ghost inky;
	/** The Blinky game object */
	private Ghost clyde;
	/** An ArrayList of Ghosts in the game */
    ArrayList<Ghost> ghosts;
	/** An ArrayList of Pellets in the game */
	ArrayList<Pellet> pelletList;
	/** The TileMap of the game */
	TileMap map;
    /** A player list ListView/VBox element */
    PlayerList playerList;
    /** A Button to indicate the player is ready */
    Button readyButton;
	/** The stage of the screen */
	Stage stage;
	/** Boolean determinant for if the game is over */
	boolean gameOver;
	/** Tracks what the client's current character is */
	public Characters currentChar;
	
	/**
	 * Tells the Pacman game screen to start.
	 */
    public static void go(String[] args) {
        launch(args);
    }
    
	@Override
	public void start(Stage theStage) throws Exception {		
		// !!! TODO: Add a screen/indicator for if the player is not connected to the server
		this.stage = theStage;
				
        // Tells the Pacman client to listen to this screen
        PacmanClientDriver.pacmanClient.setStreamListener(this);
        
		// Setting up Stage settings
		stage.setTitle("Pac-Man");
		int windowHeight = TILESIZE * TileMap.VERTICALTILES;
		int windowWidth = TILESIZE * TileMap.HORIZONTALTILES;
		
		// !!! TODO: Change to scale resolution to monitor size?
        stage.setMaxHeight(windowHeight);
        stage.setMinHeight(windowHeight);
        stage.setMaxWidth(windowWidth);
        stage.setMinWidth(windowWidth);

        // Set up the pre-game screen
        BorderPane root = new BorderPane();
        // Set up the player ListView
        this.playerList = new PlayerList();
        root.setCenter(playerList);

        // Set up the ready Button
        this.readyButton = new Button("Ready");
        this.readyButton.setOnAction(buttonHandler);
        root.setBottom(readyButton);
        
        
        // Ask the server to send a list of current players
        PacmanClientDriver.doCommand("getplayerlist:");
        
        // !!! TODO: Move stuff to pregameSetup method
        //pregameSetup(theStage, root);
        
        // Set up the scene of the stage along with the root
        Scene scene = new Scene(root, windowHeight, windowWidth);
		scene.getStylesheets().add(getClass().getResource("application.css")
				.toExternalForm());
        stage.setScene(scene);
        
        ArrayList<Integer> input = new ArrayList<Integer>();   
        // Handler for when a key is pressed 
        scene.setOnKeyPressed(
        	new EventHandler<KeyEvent>(){
        		@Override
        		public void handle(KeyEvent e) {
              			int code = e.getCode().getCode();

        			if(validKey(e.getCode()) && !input.contains(code)) {
        				// If the current input is valid and not registered, add to input list
        				// and send that input to the server
        				input.add(code);      
        				Movement direction = getMovementUsingCode(code);
        				
        				GameCharacter gameChar = null;

        				switch(currentChar) {
        				case PACMAN:
        					gameChar = pac;
        					break;
        				case BLINKY:
        					gameChar = blinky;
        					break;
        				case PINKY:
        					gameChar = pinky;
        					break;
        				case INKY:
        					gameChar = inky;
        					break;
        				case CLYDE:
        					gameChar = clyde;
        					break;
        				}
        				
        				System.out.println("character in input: " + gameChar);
        				// If the character isn't already moving in a certain direction, 
        				// tell server new direction (and current coordinates)
        				if (gameChar.getDirection() != direction) {
        					sendMovement(gameChar, direction.getValue());
        				}
        			}	
        		}
        	}
    	);
        
        // Handler for when a key is released
        scene.setOnKeyReleased(
        	new EventHandler<KeyEvent>() {
        		@Override
        		public void handle(KeyEvent e) {
        			if (validKey(e.getCode())) {
            			// Remove the input from the input list 
            			input.remove((Integer) e.getCode().getCode());
        			}
        		}
        	}
		);
        
        stage.show();
	}
	
	/**
	 * Method used to send the client character's location to the server
	 * @param character The GameCharacter to send movement of (need tile position)
	 * @param directionCode The ID of the Movement direction
	 */
	private void sendMovement(GameCharacter character, int directionCode) {        
    	int[] tilePosition = getCharacterLocation(character);
        
        PacmanClientDriver.pacmanClient.send("move:" + directionCode + " " + 
				tilePosition[TileMap.YCOORDINATE] + " " + 
        		tilePosition[TileMap.XCOORDINATE] + " ");
	}
	
	/**
	 * Helper method used to get the client's current GameCharacter
	 * @return the client's current GameCharacter
	 */
	private GameCharacter getCurrentGameCharacter() {
		GameCharacter gameChar = null;
		
		System.out.println("Getting current game character, currentChar is: " + this.currentChar);
		switch(this.currentChar) {
		case PACMAN:
			gameChar = this.pac;
			break;
		case BLINKY:
			gameChar = this.blinky;
			break;
		case PINKY:
			gameChar = this.pinky;
			break;
		case INKY:
			gameChar = this.inky;
			break;
		case CLYDE:
			gameChar = this.clyde;
			break;
		}
		
		return gameChar;
	}
	
	 /**
     * The button event handler for the GUI
     */
    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Object btn = event.getSource();            
            
            if (btn == readyButton) {
            	// Send the server a message saying that you are ready
            	PacmanClientDriver.doCommand("ready:");
            	// !! TODO: Disabling the button after readying for now
            	// Should we allow un-readying?
            	readyButton.setDisable(true);
            }
        }
    };

	/**
	 * Helper method used to display the screen that appears before the game starts
	 * @param theStage the main stage of the screen
	 */
	private void pregameSetup(Stage theStage, Parent root) {

        
        
	}

	@Override
	public void statusNotify(String message) {
		// This method is used  when a message is received 
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
		
		// Interpret commands sent from the server
		if (command.contains("newplayerlist")) {
			// Split the player names by spaces
			String[] playersArray = options.split(" ");
			for (String playerName : playersArray) {
				this.playerList.addPlayer(playerName);
			}
		} else if (command.contains("newplayer")) {
			System.out.println("Adding player: " + options);
			
			// !!! TODO: Sometimes causes an error because of the thread
			// Suggested fixes: Platform.runLater() method or javafx.concurrent API
			// https://stackoverflow.com/questions/32498307/in-javafx-is-an-observablearraylist-thread-safe
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					playerList.addPlayer(messageArray[1]);					
				}
			});
		} else if (command.contains("removeplayer")) {
			this.playerList.removePlayer(options);
			
		} else if (command.contains("move")) {
			String[] moveArray = options.split(" ");
			// moveArray[0] is the ID of the character that is being moved
			int charID = Integer.parseInt(moveArray[0]);
			Characters moveCharacter = Characters.getCharacterUsingID(charID);
			// moveArray[1] is the ID of the movement direction
			Movement direction = getMovementUsingID(Integer.parseInt(moveArray[1]));
			// moveArray[2] is the Y coordinate
			int posY = Integer.parseInt(moveArray[2]) * TILESIZE;
			// moveArray[3] is the X coordinate
			int posX = Integer.parseInt(moveArray[3]) * TILESIZE;
			
			// Change character movement based on received information
			switch (moveCharacter) {
			case PACMAN:
				// Change Pacman's movement
				this.pac.movement(direction, posY, posX);
				break;
			case BLINKY:
				// Change Blinky's movement
				this.blinky.movement(direction, posY, posX);
				break;
			case PINKY:
				// Change Pinky's movement
				this.pinky.movement(direction, posY, posX);
				break;
			case INKY:
				// Change Inky's movement
				this.inky.movement(direction, posY, posX);
				break;
			case CLYDE:
				// Change Clyde's movement
				this.clyde.movement(direction, posY, posX);
				break;
			
			}
			
			// Update Pacman's movement when 
			//pacMoveLogic();
			// !!!!! TODO: Get direction (and coordinates?) from server.
			//pac.moveToLeft("pac_left_25.png");
			
		} else if (command.contains("pelletcollected")) {
			// Update the current score
			score.increment();
			// Update using the given index of the pellet 
			Pellet pelletCollected = pelletList.get(Integer.parseInt(options.split(" ")[0]));
			pelletCollected.setIsEaten(true);
			pelletCollected.hide();
			
		} else if (command.contains("gameover")) {
			// Stop the animation
			
		} else if (command.contains("startgame")) {
			// options is a String representing the number of players
			int numPlayers = Integer.parseInt(options.split(" ")[0]);
			// Initialize ghosts based on number of players
	        this.ghosts = new ArrayList<Ghost>();	
	        // Number of ghosts is number of players - Pacman
			initializeGhosts(numPlayers);
			
			// Begin the game animation loop
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gameOver = false;
					gameLoop();	
				}
			});
		} else if (command.contains("character")) {
			// options is a String representing the Characters ID of the client's character
			int charID = Integer.parseInt(options.split(" ")[0]);
			this.currentChar = Characters.getCharacterUsingID(charID);
			System.out.println("Current character is: " + currentChar);
		}
	}
	
	/**
	 * Helper method used to initialize ghosts based on number of players
	 * @param numPlayers The number of players in the game
	 */
	private void initializeGhosts(int numPlayers) {
		for (int i = 0; i < numPlayers; i++) {
			Characters character = Characters.getCharacterUsingID(i);
			// Initialize ghost based on ID and add them to the Ghosts list
			System.out.println("Initializing Ghost");
			switch(character) {
			case BLINKY:
				this.blinky = new Ghost("blinky");
				this.ghosts.add(blinky);
				break;
			case PINKY:
				this.pinky = new Ghost("pinky");
				this.ghosts.add(pinky);
				break;
			case INKY:
				this.inky = new Ghost("inky");
				this.ghosts.add(inky);
				break;
			case CLYDE:
				this.clyde = new Ghost("clyde");
				this.ghosts.add(clyde);
				break;
			}
		}
	}
	
	
	
	/**
	 * Method used to handle Pacmans movement
	 */
	private void pacMoveLogic() {
		
	}

	/**
	 * Method used to handle the general game loop of Pacman
	 */
	private void gameLoop() {
		System.out.println("Starting game");
		
        Group root = new Group();
		int windowHeight = TILESIZE * TileMap.VERTICALTILES;
		int windowWidth = TILESIZE * TileMap.HORIZONTALTILES;
        
        // Settings up game objects
        this.pac = new Pacman();
        this.score = new IntObject(0);
        // !!!!! TODO: Retrieve how many ghosts there are from the server
        
        this.map = new TileMap();
        pac.setImage("pac_left_25.png");
        // Set the position of Pacman using his initial location in the TileMap
        pac.setPosition(TileMap.INITIALPACLOCATION[map.XCOORDINATE] * TILESIZE,
        					TileMap.INITIALPACLOCATION[map.YCOORDINATE] * TILESIZE);

        // Setting up the game screen
        Canvas canvas = new Canvas(TILESIZE * TileMap.HORIZONTALTILES, TILESIZE * TileMap.VERTICALTILES);
        Canvas mapCanvas = new Canvas(TILESIZE * TileMap.HORIZONTALTILES, TILESIZE * TileMap.VERTICALTILES);
        root.getChildren().add(mapCanvas);
        root.getChildren().add(canvas);
        
        GraphicsContext mapGC = mapCanvas.getGraphicsContext2D();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Text settings in the GraphicsContext
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(16));
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);

        // Render the map tiles on a GraphicsContext separate from the pellets and characters
        renderTiles(map.getMap(), mapGC);
        
        // Set the new root of the Stage
        this.stage.getScene().setRoot(root);
        
        // Start the game render loop
        gameRenderLoop(gc);
	}
	
	/**
	 * Method used when the game is started and being rendered
	 */
	private void gameRenderLoop(GraphicsContext gc) {
    	LongObject lastNanoTime = new LongObject(System.nanoTime());
    	pelletList = generatePellets(map);
    	GameCharacter gameChar = getCurrentGameCharacter();
    	
    	// Start the animation rendering timer
    	new AnimationTimer() {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                // If client is about to hit a wall (and not staying still), tell server to stop movement
                if (gameChar.getDirection() != Movement.STILL) {
                	int[] charPosition = getCharacterLocation(gameChar);
                	if (map.nextTileValue(charPosition, gameChar.getDirection()) == TileValue.WALL.getValue()) {
                		sendMovement(gameChar, Movement.STILL.getValue());
                	}
                }
                
                // Update game characters
                pac.update(elapsedTime);
                Iterator<Ghost> ghostUpdIter = ghosts.iterator();
                while (ghostUpdIter.hasNext()) {
                	Ghost currentGhost = ghostUpdIter.next();
                    currentGhost.update(elapsedTime);
                }
                
                // Render the game objects
                gc.clearRect(0, 0, TILESIZE * TileMap.HORIZONTALTILES, TILESIZE * TileMap.VERTICALTILES);
                for (Sprite pellet : pelletList )
                	pellet.render(gc);
                pac.render(gc);
                Iterator<Ghost> ghostRenderIter = ghosts.iterator();
                while (ghostRenderIter.hasNext()) {
                	Ghost currentGhost = ghostRenderIter.next();
                	currentGhost.render(gc);
                }
                
                // Client-side Pacman collision detection                
                if (currentChar == Characters.PACMAN) {
                	ListIterator<Pellet> pelletListIter = pelletList.listIterator();
                    while ( pelletListIter.hasNext() )
                    {
                    	int pelletIndex = pelletListIter.nextIndex();
                        Pellet pellet = pelletListIter.next();
                        // If Pacman collides with a pellet that hasn't been eaten yet...
                        if ( pac.intersects(pellet) && !pellet.getIsEaten()) {
                    		// (client-side) Set the pellet to be eaten and hidden
                        	pellet.setIsEaten(true);
                        	pellet.hide();
                        	// Send a message to the server with the pellet index
                            PacmanClientDriver.doCommand("pelletcollected:" + pelletIndex);
                        }
                    }
                }
                

                // Update the score text
                String pointsText = "Score: " + (100 * score.getValue());
                gc.fillText( pointsText, 360, 15);
                gc.strokeText( pointsText, 360, 15);
            }
        }.start();
	}

	/**
	 * Helper method used to generate a list pellets based on a TileMap
	 * @param map a map used to generate a stage/level
	 * @return an ArrayList of Pellet objects
	 */
	private ArrayList<Pellet> generatePellets(TileMap map) {
    	// Initializing score pellets
        ArrayList<Pellet> pelletList = new ArrayList<Pellet>();
        // Go through the tile map and if there is 1, put a pellet there
        for (int y = 0; y < TileMap.VERTICALTILES; y++) {
        	for (int x = 0; x < TileMap.HORIZONTALTILES; x++) {
        		if (map.getMap()[y][x] == TileValue.PELLET.getValue()) {
        			Pellet pellets = new Pellet(false);
                    pellets.setImage("pellet_20.png");     
                    pellets.setPosition(x * TILESIZE, y * TILESIZE);
                    pelletList.add( pellets );
        		}
        	}
        }
        return pelletList;
    }
	
	/**
     * Helper method used to get Pacman's current location in accordance to the tile map
     * @return a 2-integer array of the y and x values
     */
    public int[] getCharacterLocation(GameCharacter character) {
    	int[] result = new int[2];
    	double actualX = character.positionX;
        double actualY = character.positionY;
        long tileX = Math.round(actualX/TILESIZE);
        long tileY = Math.round(actualY/TILESIZE);
        
        result[TileMap.YCOORDINATE] = (int) tileY;
        result[TileMap.XCOORDINATE] = (int) tileX;
        
        return result;
    }
    
    /**
     * Helper method used to render tiles in the matrix
     * @param map A matrix of integers that represents the map
     * @param gc the GraphicsContext to render the tiles on
     */
    private void renderTiles(int[][] map, GraphicsContext gc) {
    	// Create a new tile to render for every point in the matrix
    	for (int y = 0; y < TileMap.VERTICALTILES; y++) {
    		for (int x = 0; x < TileMap.HORIZONTALTILES; x++) {
    			int tileValue = map[y][x];
    			
    			// Create a new Tile and render it
    			Tile tile = new Tile();
    			tile.setPosition(x*TILESIZE, y*TILESIZE);
    			// If the tile is a wall, give it the wall space image
    			// If the tile is empty or a pellet, give it the empty space image
    			if (tileValue == TileValue.WALL.getValue()) {
    				tile.setImage("wall_20.png");
    			} else {
    				tile.setImage("empty_20.png");
    			}
    			tile.render(gc);
    		}
    	}
    }
    
    /**
     * Helper method to determine if the key pressed is valid
     * @param key the KeyCode of the key
     * @return true if the key is valid
     */
    private boolean validKey(KeyCode key) {
    	boolean result = false;
    	switch (key) {
    		case LEFT:
    		case RIGHT:
    		case UP:
    		case DOWN:
    			result = true;
    			break;
    	}
    	
    	return result;
    }
    
    /**
	 * Helper method used to get a movement direction using a KeyCode ID
	 * @param code The ID of the KeyCode
	 * @return The Movement direction
	 */
	public Movement getMovementUsingCode(int code) {
		Movement movement = null;
		
		if (code == KeyCode.UP.getCode()) {
			movement = Movement.UP;
		} else if (code == KeyCode.DOWN.getCode()) {
			movement = Movement.DOWN;
		} else if (code == KeyCode.LEFT.getCode()) {
			movement = Movement.LEFT;
		} else if (code == KeyCode.RIGHT.getCode()) {
			movement = Movement.RIGHT;
		} else  {
			movement = Movement.STILL;
		}
		
		System.out.println("Movement is: " + movement);
				
		return movement;
	}
	
	/**
	 * Helper method used to get a movement direction using a Movement ID
	 * @param code The ID of the Movement
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
		} else  {
			movement = Movement.STILL;
		}
		
		System.out.println("Movement is: " + movement);
				
		return movement;
	}
}
