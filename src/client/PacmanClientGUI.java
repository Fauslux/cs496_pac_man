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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This class is the GUI that is used to display the Pacman game graphically. It is told
 * messages that the client receives from the server, and updates the client-side game accordingly.
 * @author Dillon Orr
 * @version May 2023
 */

public class PacmanClientGUI extends Application implements ConsoleListener {
	/** This constant represents the size of a square tile (width and height) */
	private static final int TILESIZE = 20;
	/** This constant is used to increase the height of the game window */
	private final int HEIGHT_ADDITION = 150;
	/** This holds the current score of Pacman */
	private IntObject score;
	/** This holds the current amount of lives of Pacman */
	private IntObject lives;
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
    private ArrayList<Ghost> ghosts;
	/** An ArrayList of Pellets in the game */
	private ArrayList<Pellet> pelletList;
	/** The TileMap of the game */
	private TileMap map;
    /** A player list ListView/VBox element */
    private PlayerList playerList;
    /** A Button to indicate the player is ready */
    private Button readyButton;
	/** The stage of the screen */
	private Stage stage;
	/** Boolean determinant for if the game is over */
	boolean gameOver;
	/** Boolean determinant for if the turn is over (one life = one turn) */
	boolean turnOver;
	/** Tracks what the client's current character is */
	public Characters currentChar;
	/** Keeps track of the desired direction that the player wants to move */
	Movement desiredDirection;
	/** The current turn (also the number of the player who is Pacman) */
	private int turnNumber;
	/** An ArrayList of PlayerCards that is used to display player information in-game */
	private ArrayList<PlayerCard> playerCards;
	/** The maximum window height of the game */
	private int maxWindowHeight;
	/** The maximum window width of the game */
	private int maxWindowWidth;
	/** The GraphicsContext that the game will be rendered on (characters, pellets, score, etc.) */
	private GraphicsContext gameGraphicsContext;
	/** The String of the player who won the game (retrieved when the server says game over) */
	private String winnerName;
	
	/**
	 * Tells the Pacman game screen to start.
	 */
    public static void go(String[] args) {
        launch(args);
    }
    
    /**
     * The initial method called when the screen starts
     */
	@Override
	public void start(Stage theStage) throws Exception {		
		// !!! TODO: Add a screen/indicator for if the player is not connected to the server
		this.stage = theStage;
        // Tells the Pacman client to listen to this screen
        PacmanClientDriver.pacmanClient.setStreamListener(this);
		// Setting up Stage settings
		stage.setTitle("Pac-Man");
		// Added 150 to height due to the bottom being cut off
		this.maxWindowHeight = TILESIZE * TileMap.VERTICALTILES + HEIGHT_ADDITION;
		this.maxWindowWidth = TILESIZE * TileMap.HORIZONTALTILES;
		// !!! TODO: Change to scale resolution to monitor size?
        stage.setMaxHeight(maxWindowHeight);
        stage.setMinHeight(maxWindowHeight);
        stage.setMaxWidth(maxWindowWidth);
        stage.setMinWidth(maxWindowWidth);

        // Set up the pre-game screen
        BorderPane root = new BorderPane();
        // Set up the player ListView
        this.playerList = new PlayerList();
        root.setCenter(playerList);
        // Set up the ready Button
        this.readyButton = new Button("Ready");
        this.readyButton.setOnAction(buttonHandler);
        root.setBottom(readyButton);
        
        // Sends a message to the server to get starting information
        PacmanClientDriver.doCommand("startInfo:" + 
                PacmanClientDriver.getPlayerName() + ":" + 
                PacmanClientDriver.getCode());
        // Ask the server to send a list of current players
        PacmanClientDriver.doCommand("getplayerlist:");
        
        // Set up the scene of the stage along with the root
        Scene scene = new Scene(root, maxWindowHeight, maxWindowWidth);
		scene.getStylesheets().add(getClass().getResource("application.css")
				.toExternalForm());
        stage.setScene(scene);
        // Input handling
        setupInputHandlers(scene);
        
        stage.show();
	}
	
	/** 
	 * Setup the input handlers for when buttons are pressed or released
	 * @param scene The scene where a button is pressed/released
	 */
	private void setupInputHandlers(Scene scene) {
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
        				desiredDirection = getMovementUsingCode(code);
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
	}

	/**
	 * Method used to send the client character's location to the server
	 * @param character The GameCharacter to send movement of (need tile position)
	 * @param directionCode The ID of the Movement direction
	 */
	private void sendMovement(GameCharacter character, int directionCode) {   
		// Reset the desired direction
		desiredDirection = Movement.STILL;
    	int[] tilePosition = getCharacterLocation(character);
    	// Send a command with the direction code and y-x position
        PacmanClientDriver.doCommand("move:" + directionCode + " " + 
				tilePosition[TileMap.YCOORDINATE] + " " + 
        		tilePosition[TileMap.XCOORDINATE] + " ");
	}
	
	/**
	 * Helper method used to get the client's current GameCharacter
	 * @return the client's current GameCharacter
	 */
	private GameCharacter getCurrentGameCharacter() {
		GameCharacter gameChar = null;
		
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
            	// !! TODO: Disabling the button after readying - should we allow un-readying?
            	readyButton.setDisable(true);
            }
        }
    };

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
			// Command received when a new player joins and gets a player list
			// Split the player names by spaces
			String[] playersArray = options.split(" ");
			for (String playerName : playersArray) {
				this.playerList.addPlayer(playerName);
			}
		} else if (command.contains("newplayer")) {
			// Command received when a new player joins
			System.out.println("Adding player: " + options);
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					playerList.addPlayer(messageArray[1]);					
				}
			});
		} else if (command.contains("removeplayer")) {
			// Command received when a player is removed
			this.playerList.removePlayer(options);
			
		} else if (command.contains("move")) {
			// Command received when a character moves
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
			
		} else if (command.contains("pelletcollected")) {
			// Command received when a pellet is collected
			// Update the current score
			score.increment();
			// Update using the given index of the pellet 
			Pellet pelletCollected = pelletList.get(Integer.parseInt(options.split(" ")[0]));
			pelletCollected.setIsEaten(true);
			pelletCollected.hide();
			
		} else if (command.contains("gameover")) {
			// Stop the animation
			// The server sends the name of a winner (or %draw) when the game ends
			this.winnerName = options.split(" ")[0];
			this.gameOver = true;
			PacmanClientDriver.doCommand("close");
		} else if (command.contains("startgame")) {
			// Initial message received when the game will first begin
			String[] messageArr = options.split(" ");
			// options[0] is the number of players
			int numPlayers = Integer.parseInt(messageArr[0]);
			// options[1] is the amount of lives Pacman has
			this.lives = new IntObject(Integer.parseInt(messageArr[1]));
	        // Number of ghosts is number of players - Pacman
			initializeGhosts(numPlayers);
			this.turnNumber = 0;
			// Begin the game animation loop
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					turnOver = false;
					gameOver = false;
					startGame();	
				}
			});
		} else if (command.contains("character")) {
			// options is a String representing the Characters ID of the client's character
			int charID = Integer.parseInt(options.split(" ")[0]);
			this.currentChar = Characters.getCharacterUsingID(charID);
			System.out.println("Current character is: " + currentChar);
		} else if (command.contains("refreshpellets")) {
			regeneratePellets();
		} else if (command.contains("livelost")) {
			String pacPlayerName = options.split(" ")[0];
			this.lives.decrement();
			this.turnOver = true;
		} else if (command.contains("continuegame")) {
			// Restart the render loop
			this.turnOver = false;
			// !!! TODO: Using the player list to determine number of players (?)
			initializeGhosts(this.playerList.playerStrings.size());
			gameRenderLoop(this.gameGraphicsContext);
			
		} else if (command.contains("newturn")) {
			// Change the player cards
			String[] messageArr = options.split(" ");
			// messageArr[0] is the number of lives
			this.lives.setValue(Integer.parseInt(messageArr[0]));
			this.score.setValue(0);
			this.turnNumber++;
			setPlayerCardImages();
			
			// !!! TODO: Repeated code from continuegame above (lines 350-355)
			// Restart the render loop
			this.turnOver = false;
			// !!! TODO: Using the player list to determine number of players (?)
			initializeGhosts(this.playerList.playerStrings.size());
			gameRenderLoop(this.gameGraphicsContext);
		} else if (command.contains("highscore")) {
			// Change the high score of a player card
			String[] messageArr = options.split(" ");
			// messageArr[0] is the previous Pacman player's index
			// messageArr[1] is the high score of that previous player
			updatePlayerCardScore(Integer.parseInt(messageArr[0]),
					Integer.parseInt(messageArr[1]));
		}
	}
	
	/**
	 * Updated a player's card with their high score
	 * @param playerName The name of the player to update
	 * @param highScore The player's high score to update with
	 */
	private void updatePlayerCardScore(int playerIndex, int highScore) {
		this.playerCards.get(playerIndex).setScore(highScore);
	}

	/**
	 * Helper method used to initialize ghosts based on number of players
	 * @param numPlayers The number of players in the game
	 */
	private void initializeGhosts(int numPlayers) {
        this.ghosts = new ArrayList<Ghost>();	
		for (int i = 0; i < numPlayers; i++) {
			Characters character = Characters.getCharacterUsingID(i);
			// Initialize ghost based on ID and add them to the Ghosts list
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
	 * Method used to start the general game loop of Pacman
	 */
	private void startGame() {	
		int windowHeight = TILESIZE * TileMap.VERTICALTILES;
		int windowWidth = TILESIZE * TileMap.HORIZONTALTILES;

		// !!! TODO: Change to scale resolution to monitor size?
		BorderPane root = new BorderPane();
		root.setMaxHeight(this.maxWindowHeight);
		root.setMaxWidth(this.maxWindowWidth);
		root.getStyleClass().add("bgColor");		

		this.playerCards = new ArrayList<PlayerCard>();
		generatePlayerCards(root);
        Group canvasGroup = new Group();
        // Settings up game objects
        this.score = new IntObject(0);
        this.map = new TileMap();

        // Setting up the game screen
        Canvas canvas = new Canvas(windowWidth, windowHeight);
        Canvas mapCanvas = new Canvas(windowWidth, windowHeight);
        canvas.minHeight(windowHeight);
        mapCanvas.minHeight(windowHeight);
        canvas.minWidth(windowWidth);
        mapCanvas.minWidth(windowWidth);
        
        canvasGroup.getChildren().add(mapCanvas);
        canvasGroup.getChildren().add(canvas);
        
        GraphicsContext mapGC = mapCanvas.getGraphicsContext2D();
        this.gameGraphicsContext = canvas.getGraphicsContext2D();
        // Text settings in the GraphicsContext
        this.gameGraphicsContext.setTextAlign(TextAlignment.CENTER);
        this.gameGraphicsContext.setFont(new Font(16));
        this.gameGraphicsContext.setStroke(Color.WHITE);
        this.gameGraphicsContext.setFill(Color.WHITE);
        // Render the map tiles on a GraphicsContext separate from the pellets and characters
        renderTiles(map.getMap(), mapGC);
        
        root.setCenter(canvasGroup);
        // Set the new root of the Stage
        this.stage.getScene().setRoot(root);
        // Start the game render loop
        gameRenderLoop(this.gameGraphicsContext);
	}
	
	/**
	 * Generates player cards for the players in the game
	 * @param root The root to add player cards to
	 */
	private void generatePlayerCards(BorderPane root) {
		HBox playerCardsBox = new HBox();
		// Add a left space to the box of PlayerCards
		Region leftSpacer = new Region();
        leftSpacer.setId("horizontalSpacer");
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        playerCardsBox.getChildren().add(leftSpacer);
        
		// Add a PlayerCard for all current players 
        int counter = 1;
		for (String playerName : this.playerList.playerStrings) {
			// Get the character image name by using mod operator on current ID
			PlayerCard playerCard = new PlayerCard(playerName);
			this.playerCards.add(playerCard);
			playerCardsBox.getChildren().add(playerCard);
			
			if (counter != this.playerList.playerStrings.size()) {
				// Add a space between players (but not the last player
				Region betweenSpacer = new Region();
				betweenSpacer.setId("sepSpacer");
				HBox.setHgrow(betweenSpacer, Priority.ALWAYS);
				playerCardsBox.getChildren().add(betweenSpacer);
			}
			counter++;
		}
		setPlayerCardImages();
		
		// Add a right space to the box of PlayerCards
		Region rightSpacer = new Region();
        rightSpacer.setId("horizontalSpacer");
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        playerCardsBox.getChildren().add(rightSpacer);
		// Add the PlayerCards to the top of the BorderPane
		root.setTop(playerCardsBox);
	}

	/**
	 * Method used to setup player card images based on the turn number
	 */
	private void setPlayerCardImages() {
        // The turn number is the index of the current Pacman player
		// On turn number 0: player0:pacman(0), 	player1:blinky(1), 	player2:pinky(2)
		// On turn number 1: player0:pinky(2), 		player1:pacman(0), 	player2:blinky(1)
		// On turn number 2: player0:blinky(1), 	player1:pinky(2), 	player2:pacman(0)
        int playerNumber = 0;
        for (PlayerCard playerCard : this.playerCards) {
			// Get the new character ID by using the mod operator on current ID
            int playerCardCharID = Math.floorMod(playerNumber - this.turnNumber, this.playerCards.size());
			String characterImgName = Characters
					.getCharacterUsingID(playerCardCharID).getName();
			playerCard.setImage(characterImgName);
			playerNumber++;
		}
	}

	/**
	 * Method used to place characters in their starting positions
	 */
	private void initializePositions() {
		pac.setPosition(map.INITIALPACLOCATION[map.XCOORDINATE] * TILESIZE,
							map.INITIALPACLOCATION[map.YCOORDINATE] * TILESIZE);
        pac.setImage("pac_left_25.png");

		if (blinky != null) {
			blinky.setPosition(map.INITIALBLINKYLOCATION[map.XCOORDINATE] * TILESIZE,
								map.INITIALBLINKYLOCATION[map.YCOORDINATE] * TILESIZE);
	        blinky.setImage("blinky_left_25.png");
		}
		if (pinky != null) {
			pinky.setPosition(map.INITIALPINKYLOCATION[map.XCOORDINATE] * TILESIZE,
								map.INITIALPINKYLOCATION[map.YCOORDINATE] * TILESIZE);
	        pinky.setImage("pinky_left_25.png");
		}
		if (inky != null) {
			inky.setPosition(map.INITIALINKYLOCATION[map.XCOORDINATE] * TILESIZE,
								map.INITIALINKYLOCATION[map.YCOORDINATE] * TILESIZE);
	        inky.setImage("inky_left_25.png");
		}
		if (clyde != null) {
			clyde.setPosition(map.INITIALCLYDELOCATION[map.XCOORDINATE] * TILESIZE,
								map.INITIALCLYDELOCATION[map.YCOORDINATE] * TILESIZE);
	        clyde.setImage("clyde_left_25.png");
		}
	}
	
	/**
	 * Method used when the game is started and being rendered
	 */
	private void gameRenderLoop(GraphicsContext gc) {
		this.pac = new Pacman();
		// Clear the GraphicsContext
        gc.clearRect(0, 0, TILESIZE * TileMap.HORIZONTALTILES, TILESIZE * TileMap.VERTICALTILES);
    	this.initializePositions();
    	pelletList = generatePellets();	
    	GameCharacter gameChar = getCurrentGameCharacter();
    	desiredDirection = Movement.STILL;
    	LongObject lastNanoTime = new LongObject(0);
        
    	// Start the animation rendering timer
    	AnimationTimer animTimer = new AnimationTimer() {
    		// The number of frames to pass before changing Pacman's image to show animation
    		private final int FRAMES_BETWEEN_ANIMATION = 3;
    		// Used to count frames and change the images of sprites
        	private int frameCounter = 0;
        	// If Pacman has hit a ghost (ensures no double hits)
            private boolean hitGhost = false;
            
            public void handle(long currentNanoTime) {
                // Calculate time since last update
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;   
                // Handle Pacman animation frames
                if (pac.getDirection() != Movement.STILL) {
                	// If Pacman is not standing still, update frame counter
                    frameCounter++;
                    if (frameCounter/FRAMES_BETWEEN_ANIMATION == 4) {
                    	// Frame 4 is the same as frame 2 for Pacman
                    	pac.setImageFrame(2);
                    	// Reset frame counter when all frames are reached (4 total)
                    	this.frameCounter = 0;
                    } else {
                    	if (frameCounter % FRAMES_BETWEEN_ANIMATION == 0 && frameCounter != 0) {
                        	pac.setImageFrame(frameCounter/FRAMES_BETWEEN_ANIMATION);
                    	}
                    }
                }
                // The last update time is now set to the current time
                lastNanoTime.value = currentNanoTime;
                
                // Handle player movement in a separate method
                movementHandler();
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
                
                // Client-side Pacman collision detection (only if the player is Pacman)
                if (currentChar == Characters.PACMAN && !hitGhost) {
                	// Checking if Pacman hit a ghost
                	Iterator<Ghost> ghostsIter = ghosts.iterator();
                	while (ghostsIter.hasNext()) {
                		if (pac.intersects(ghostsIter.next())) {
                			hitGhost = true;
                			PacmanClientDriver.doCommand("hitghost:");
                			
                		}
                	}
                	// Checking if Pacman hit a pellet
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
                            break;
                        }
                    }
                }
                // Update the score text
                String pointsText = "Score: " + (100 * score.getValue());
                gc.fillText( pointsText, 360, 15);
                gc.strokeText( pointsText, 360, 15);
                // Render the lives
                String livesText = "Lives: " + lives.getValue();
                gc.fillText( livesText, 160, 15);
                gc.strokeText( livesText, 160, 15);
                                
                if (turnOver) {
                	String turnPacName = getTurnPacmanName();
                	String displayString = turnPacName + " LOST A LIFE";
                	if (lives.getValue() == 0) {
                		displayString = turnPacName + " TURN OVER";
                	}
                	if (gameOver) {
                		// !!! TODO: If the game is over
                		// Tell the players the game is over, the winner, and to return to the lobby
                		if (!winnerName.equals("%draw")) {
                    		displayString = "GAME OVER - " + winnerName + " wins - RETURNING TO LOBBY";
                		} else {
                    		displayString = "GAME OVER - DRAW - RETURNING TO LOBBY";
                		}
                		
                	}
                	gc.fillText(displayString, (TILESIZE * TileMap.HORIZONTALTILES)/2, 
													(TILESIZE * TileMap.VERTICALTILES)/2);
                	gc.strokeText(displayString, (TILESIZE * TileMap.HORIZONTALTILES)/2, 
													(TILESIZE * TileMap.VERTICALTILES)/2);
                	// Stop the rendering loop
                	this.stop();
                }
            }			
        };
        animTimer.start();
	}

	/**
	 * Returns the name of the player who is currently playing as Pacman
	 * @return the name of the player who is currently playing as Pacman
	 */
	private String getTurnPacmanName() {
		return this.playerCards.get(this.turnNumber).getPlayerName();
	}
    	
	/**
	 * Method used to handle movement of the client's game character
	 */
	private void movementHandler() {
		GameCharacter gameChar = null;
		switch (this.currentChar) {
		case PACMAN:
			// Change Pacman's movement
			gameChar = this.pac;
			break;
		case BLINKY:
			// Change Blinky's movement
			gameChar = this.blinky;
			break;
		case PINKY:
			// Change Pinky's movement
			gameChar = this.pinky;
			break;
		case INKY:
			// Change Inky's movement
			gameChar = this.inky;
			break;
		case CLYDE:
			// Change Clyde's movement
			gameChar = this.clyde;
			break;
		}        
		
		int[] charPosition = getCharacterLocation(gameChar);   
        Movement currentDirection = gameChar.getDirection();
        if (map.nextTileValue(charPosition, desiredDirection) != TileValue.WALL.getValue()
				&& desiredDirection != Movement.STILL
				&& desiredDirection != gameChar.getDirection()) {
        	// If character can use the desired direction, then do so
            sendMovement(gameChar, desiredDirection.getValue());
		} else if (map.nextTileValue(charPosition, currentDirection) == TileValue.WALL.getValue()
				&& currentDirection != Movement.STILL) {
            // If character is about to hit a wall and not staying still, stay still
    		sendMovement(gameChar, Movement.STILL.getValue());
    	}
	}

	/**
	 * Method used to generate a list of pellets based on a TileMap
	 * @param map a map used to generate a stage/level
	 * @return an ArrayList of Pellet objects
	 */
	private ArrayList<Pellet> generatePellets() {
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
	 * Method used to regenerate the list of pellets when all pellets are collected
	 */
	private void regeneratePellets() {
		Thread regenThread = new Thread() {
			public void run() {
				for (Pellet pellet : pelletList) {
					pellet.setImage("pellet_20.png");
					pellet.setIsEaten(false);
				}
			}	
		};
		regenThread.start();
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
						
		return movement;
	}
	
	/**
	 * Method used to stop/end the GUI application
	 */
	@Override
	public void stop() {
		if(PacmanClientDriver.pacmanClient.getAgent() != null) {
			PacmanClientDriver.doCommand("close");
		} 
		System.exit(0);
	}
}
