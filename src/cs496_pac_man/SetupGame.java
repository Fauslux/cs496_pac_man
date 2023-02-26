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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SetupGame extends Application {
	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Hello, World!");

        Group root = new Group();
        
        Scene scene = new Scene(root);
        
        Canvas canvas = new Canvas(512, 512);
        
        theStage.setScene(scene);
        
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
        
        GraphicsContext gc = canvas.getGraphicsContext2D();

        VisibleObject pac = new Pacman();
        pac.setImage("pac_left.png");
        pac.setPosition(200,0);
        
        
        ArrayList<Sprite> pelletList = new ArrayList<Sprite>();
        
        for (int i = 0; i < 15; i++)
        {
        	Pellet pellets = new Pellet();
            pellets.setImage("briefcase.png");
            double px = 350 * Math.random() + 50;
            double py = 350 * Math.random() + 50;          
            pellets.setPosition(px,py);
            pelletList.add( pellets );
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
                
                pac.setVelocity(0,0);
                if (input.contains("LEFT")) {
                	pac.moveToLeft();
                }
                else if (input.contains("RIGHT")) {
                	pac.moveToLeft();
                }
                else if (input.contains("UP")) {
                	pac.moveToUp();
                }
                else if (input.contains("DOWN")) {
                	pac.moveToDown();
                }
                    
                pac.update(elapsedTime);
                
                // collision detection
                
                Iterator<Sprite> pelletListIter = pelletList.iterator();
                while ( pelletListIter.hasNext() )
                {
                    Sprite pellet = pelletListIter.next();
                    if ( pac.intersects(pellet) )
                    {
                    	pac.setVelocity(0,0);
                        
                    }
                }
                
                // render
                
                gc.clearRect(0, 0, 512,512);
                pac.render( gc );
                
                for (Sprite pellet : pelletList )
                	pellet.render( gc );

                String pointsText = "Score: $" + (100 * score.value);
                gc.fillText( pointsText, 360, 36 );
                gc.strokeText( pointsText, 360, 36 );
            }
        }.start();

        
        theStage.show();

    }
}
