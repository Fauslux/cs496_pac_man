package client;

import java.util.ArrayList;
import java.util.Iterator;

import common.Movement;
import common.TileMap;
import common.TileValue;
import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This is the main class for the Pacman game. 
 * @author Kevin Richardson
 * @author Dillon Orr
 * @version March 2023
 */

public class SetupGame extends Application {
	
	// This constant represents the size of a square tile (width and height), translating the
	// tiles in the TileMap to a GUI.
	private static final int TILESIZE = 20;
	// This field holds the current score of Pac-Man
	private IntObject score;
	
	/**
	 * Main method for SetupGame, launches the GUI
	 * @param args String list of arguments
	 */
	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Pac-Man");
        Group root = new Group();
        Scene scene = new Scene(root);
        
        Canvas canvas = new Canvas(TILESIZE * TileMap.HORIZONTALTILES, TILESIZE * TileMap.VERTICALTILES);
        Canvas mapCanvas = new Canvas(TILESIZE * TileMap.HORIZONTALTILES, TILESIZE * TileMap.VERTICALTILES);
        theStage.setScene(scene);
        root.getChildren().add(mapCanvas);
        root.getChildren().add(canvas);
        
        ArrayList<String> input = new ArrayList<String>();     
        scene.setOnKeyPressed(
        	new EventHandler<KeyEvent>(){
        		@Override
        		public void handle(KeyEvent e) {
        			String code = e.getCode().toString();
        			if( !input.contains(code))
        				input.add(code);
        		}
        	});
        scene.setOnKeyReleased(
        	new EventHandler<KeyEvent>() {
        		@Override
        		public void handle(KeyEvent e) {
        			String code = e.getCode().toString();
        			input.remove(code);
        		}
        	}
		);
        
        GraphicsContext mapGC = mapCanvas.getGraphicsContext2D();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Text settings in the GraphicsContext
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(16));
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        
        // Initializing the TileMap
        TileMap map = new TileMap();
        
        // Initializing Pacman
        Pacman pac = new Pacman();
        pac.setImage("pac_left_25.png");
        // Set the position of Pacman using his initial location in the TileMap
        pac.setPosition(TileMap.INITIALPACLOCATION[map.XCOORDINATE] * TILESIZE,
        					TileMap.INITIALPACLOCATION[map.YCOORDINATE] * TILESIZE);
        
        //Initializing ghosts
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        // Initializing Blinky
        BlinkyAI blinky = new BlinkyAI("blinky");
        blinky.setImage("blinky_25.png");
        blinky.setPosition(map.INITIALBLINKYLOCATION[map.XCOORDINATE] * TILESIZE,
        					map.INITIALBLINKYLOCATION[map.YCOORDINATE] * TILESIZE);
        ghosts.add(blinky);
        
        BlinkyAI blinky2 = new BlinkyAI("blinky");
        blinky2.setImage("blinky_25.png");
        blinky2.setPosition(map.INITIALPINKYLOCATION[map.XCOORDINATE] * TILESIZE,
        					map.INITIALPINKYLOCATION[map.YCOORDINATE] * TILESIZE);
        ghosts.add(blinky2);
        
        
        BlinkyAI blinky3 = new BlinkyAI("blinky");
        blinky3.setImage("blinky_25.png");
        blinky3.setPosition(map.INITIALINKYLOCATION[map.XCOORDINATE] * TILESIZE,
        					map.INITIALINKYLOCATION[map.YCOORDINATE] * TILESIZE);
        ghosts.add(blinky3);
        
        BlinkyAI blinky4 = new BlinkyAI("blinky");
        blinky4.setImage("blinky_25.png");
        blinky4.setPosition(map.INITIALCLYDELOCATION[map.XCOORDINATE] * TILESIZE,
        					map.INITIALCLYDELOCATION[map.YCOORDINATE] * TILESIZE);
        ghosts.add(blinky4);
        
        
        
        //https://github.com/tutsplus/Introduction-to-JavaFX-for-Game-Development/blob/master/Example5.java
        // Initialize the score
        score = new IntObject(0);
        
        theStage.show();
        
        // Render the map tiles on a GraphicsContext separate from the pellets and characters
        renderTiles(map.getMap(), mapGC);
        // Start the game loop
        gameLoop(map, input, gc, pac, ghosts);
        
    }
    
    
    public boolean gameLoop(TileMap map, ArrayList<String> input, GraphicsContext gc, Pacman pac, ArrayList<Ghost> ghosts) {
    	//Generate pellets
    	ArrayList<Pellet> pelletList = generatePellets(map);
    	LongObject lastNanoTime = new LongObject(System.nanoTime());
    	boolean pacDied = false;
    	
    	new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                                
                // game logic
                pacmanLogic(input, map, pac);
                
                // Handle ghost AI movement logic
                Iterator<Ghost> ghostMoveIter = ghosts.iterator();
                while (ghostMoveIter.hasNext()) {
                	Ghost currentGhost = ghostMoveIter.next();
                	if (currentGhost.getClass() == BlinkyAI.class) {
                    	blinkyLogicAI(map, (BlinkyAI) currentGhost);
                	}
                }
                //blinkyLogicAI(map, blinky);
                
                // Update game characters
                pac.update(elapsedTime);
                Iterator<Ghost> ghostUpdIter = ghosts.iterator();
                while (ghostUpdIter.hasNext()) {
                	Ghost currentGhost = ghostUpdIter.next();
                    currentGhost.update(elapsedTime);
                }
                                
                // collision detection
                Iterator<Pellet> pelletListIter = pelletList.iterator();
                while ( pelletListIter.hasNext() )
                {
                    Pellet pellet = pelletListIter.next();
                    if ( pac.intersects(pellet) )
                    {
                    	//pac.setVelocity(0,0);
                    	// If pellet hasn't been eaten yet, increase score
                    	if (! pellet.getIsEaten()) {
                        	score.increment();
                        	pellet.setIsEaten(true);
                        	pellet.hide();
                    	}
                    }
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

                // Update the score text
                String pointsText = "Score: " + (100 * score.getValue());
                gc.fillText( pointsText, 360, 15);
                gc.strokeText( pointsText, 360, 15);
                
                
                // GAME END LOGIC
                // If Pacman touches a ghost, the game will end and the AnimationTimer stops
                // TODO: Check for intersections when there are more ghosts (array of ghosts?)
                Iterator<Ghost> ghostCollIter = ghosts.iterator();
                while (ghostCollIter.hasNext()) {
                	Ghost currentGhost = ghostCollIter.next();
                	if (pac.intersects(currentGhost)) {
                    	gc.fillText("GAME OVER", (TILESIZE * TileMap.HORIZONTALTILES)/2, 
                    							(TILESIZE * TileMap.VERTICALTILES)/2);
                    	gc.strokeText("GAME OVER", (TILESIZE * TileMap.HORIZONTALTILES)/2, 
    							(TILESIZE * TileMap.VERTICALTILES)/2);
                    	// TODO: Add lives system, check for game over
                    	this.stop();
                    }
                }
                
            }
        }.start();

        System.out.println("hi");
        return pacDied;
    }
    
    /**
     * Helper method used to generate pellets according to the TileMap object
     * @param map
     * @return
     */
    public ArrayList<Pellet> generatePellets(TileMap map) {
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
     * The movement logic for Pacman
     * @param input an ArrayList of Strings that holds the user's input
     * @param map the TileMap of the map
     * @param pac the Pacman object 
     */
    public void pacmanLogic(ArrayList<String> input, TileMap map, Pacman pac) {
    	// Get the current tile location of Pacman
        int[] currentPacLocation = getCharacterLocation(pac);
        
        // Get Pacman's direction and move him 
        Movement currentDirection = pac.getDirection();
        Movement newDirection = Movement.STILL;
        if (input.contains(Movement.LEFT.getString())) {
        	pac.moveToLeft("pac_left_25.png");
        	newDirection = Movement.LEFT;
        }
        else if (input.contains(Movement.RIGHT.getString())) {
        	pac.moveToRight("pac_right_25.png");
        	newDirection = Movement.RIGHT;
        }
        else if (input.contains(Movement.UP.getString())) {
        	pac.moveToUp("pac_up_25.png");
        	newDirection = Movement.UP;
        }
        else if (input.contains(Movement.DOWN.getString())) {
        	pac.moveToDown("pac_down_25.png");
        	newDirection = Movement.DOWN;
        }
        
        // Handle Pacman movement logic   
        // Pacman cannot move in a direction if the next tile in that direction is a wall
        int nextPacTileValue = map.nextTileValue(currentPacLocation, pac.getDirection());
        if (nextPacTileValue == TileValue.WALL.getValue()) {
        	// If the next tile is a wall, Pacman stops moving
        	pac.stopMove();
        	pac.setPosition(currentPacLocation[TileMap.XCOORDINATE] * TILESIZE, 
							currentPacLocation[TileMap.YCOORDINATE] * TILESIZE);
        } else {
        	// If the next tile is available, let Pacman move
            // If the current direction and the new direction do NOT match, set Pac on track
        	if (currentDirection != newDirection && newDirection != Movement.STILL) {
        		pac.setPosition(currentPacLocation[TileMap.XCOORDINATE] * TILESIZE, 
            					currentPacLocation[TileMap.YCOORDINATE] * TILESIZE);
            	pac.setDirection(newDirection);
        	}
        }
    }
    
    /**
     * Logic for handling Blinky's AI movement
     * @param map the TileMap of the map
     * @param blinky the AI of the Blinky ghost
     */
    public void blinkyLogicAI(TileMap map, BlinkyAI blinky) {
    	// Handle ghost AI movement logic
        int[] currentBlinkyLocation = getCharacterLocation(blinky);
        int nextBlinkyTileValue = map.nextTileValue(currentBlinkyLocation, blinky.getDirection());
        if (nextBlinkyTileValue == TileValue.WALL.getValue()) {
        	// If the next tile is a wall, Ghost stops moving
        	blinky.stopMove();
        	blinky.setPosition(currentBlinkyLocation[TileMap.XCOORDINATE] * TILESIZE, 
								currentBlinkyLocation[TileMap.YCOORDINATE] * TILESIZE);
        }
        
        // If the ghost is standing still, it should pick a direction to move
        if (blinky.shouldChange()) {
        	int leftTileValue = map.nextTileValue(currentBlinkyLocation, Movement.LEFT);
        	int rightTileValue = map.nextTileValue(currentBlinkyLocation, Movement.RIGHT);
        	int upTileValue = map.nextTileValue(currentBlinkyLocation, Movement.UP);
        	int downTileValue = map.nextTileValue(currentBlinkyLocation, Movement.DOWN);
        	
        	Movement newBlinkyDirection = blinky.decideMove(leftTileValue, rightTileValue, 
        													upTileValue, downTileValue);
        	if (newBlinkyDirection == Movement.LEFT) {
        		blinky.moveToLeft("blinky_25.png");
        	} else if (newBlinkyDirection == Movement.RIGHT) {
        		blinky.moveToRight("blinky_25.png");
        	} else if (newBlinkyDirection == Movement.UP) {
        		blinky.moveToUp("blinky_25.png");
        	} else if (newBlinkyDirection == Movement.DOWN) {
        		blinky.moveToDown("blinky_25.png");
        	}
        }
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
}
