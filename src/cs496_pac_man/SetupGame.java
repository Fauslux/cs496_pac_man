package cs496_pac_man;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * This is the main class for the Pacman game. 
 * @author Kevin Richardson
 * @author Dillon Orr
 * @version March 2023
 */

public class SetupGame extends Application {
	
	// This constant represent the size of a square tile (width and height), translating the
	// tiles in the TileMap to a GUI.
	private final int TILESIZE = 20;

	
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
            	});
        
        GraphicsContext mapGC = mapCanvas.getGraphicsContext2D();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Initializing the TileMap
        TileMap map = new TileMap();
        
        // Initializing Pacman
        Pacman pac = new Pacman();
        pac.setImage("pac_left_25.png");
        // Set the position of Pacman using his initial location in the TileMap
        pac.setPosition(map.INITIALPACLOCATION[map.XCOORDINATE] * TILESIZE,
        					map.INITIALPACLOCATION[map.YCOORDINATE] * TILESIZE);
        
        // Initializing Blinky
        BlinkyAI blinky = new BlinkyAI();
        blinky.setImage("blinky_25.png");
        blinky.setPosition(map.INITIALBLINKYLOCATION[map.XCOORDINATE] * TILESIZE,
        					map.INITIALBLINKYLOCATION[map.YCOORDINATE] * TILESIZE);
        
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
        
        //https://github.com/tutsplus/Introduction-to-JavaFX-for-Game-Development/blob/master/Example5.java
        LongObject lastNanoTime = new LongObject(System.nanoTime());
        IntObject score = new IntObject(0);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                // game logic
                // Get the current tile location of Pacman
                int[] currentPacLocation = getCharacterLocation(pac);
                //pac.setVelocity(0,0);
                
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
                // Pacman cannot move in a direction if the next tile is a wall
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
                
                // Update game characters
                pac.update(elapsedTime);
                blinky.update(elapsedTime);
                                
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
                pac.render(gc);
                blinky.render(gc);
                
                for (Sprite pellet : pelletList )
                	pellet.render( gc );

                String pointsText = "Score: $" + (100 * score.getValue());
                gc.fillText( pointsText, 360, 15);
                gc.strokeText( pointsText, 360, 15);
            }
            
        }.start();
        
        // Render the map tiles on a GraphicsContext separate from the pellets and characters
        renderTiles(map.getMap(), mapGC);

        theStage.show();
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
     */
    private void renderTiles(int[][] map, GraphicsContext gc) {
    	for (int y = 0; y < TileMap.VERTICALTILES; y++) {
    		for (int x = 0; x < TileMap.HORIZONTALTILES; x++) {
    			int tileValue = map[y][x];
    			// Create a new Tile and render it
    			Tile tile = new Tile();
    			tile.setPosition(x*TILESIZE, y*TILESIZE);
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
