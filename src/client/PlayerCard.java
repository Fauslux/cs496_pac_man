package client;

import java.net.URISyntaxException;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * This class is the GUI element for box to show player information, such as name, character,
 * and high score.
 * @author Dillon Orr
 * @version May 2023
 */

public class PlayerCard extends VBox {
	/** The image that represents the game character that the player is playing as */
	private ImageView characterImageView;
	/** The name of the player */
	private String playerName;
	/** The label to show the high-score of the player */
	private Label highScoreLabel;
	
	/**
	 * Constructor for a PlayerCard 
	 * @param characterImgName The name of the character to set the image with
	 * @param playerName The name of the player
	 */
	public PlayerCard(String characterImgName, String playerName) {
		super(5);
		this.setAlignment(Pos.BASELINE_CENTER);
		this.setImage(characterImgName);
		this.playerName = playerName;
		// Set default score to 0
		this.setScore(0);
		setup();
	}
	
	/**
	 * Constructor for a PlayerCard using a player's name
	 * @param playerName The name of the player
	 */
	public PlayerCard(String playerName) {
		super(5);
		this.setAlignment(Pos.BASELINE_CENTER);
		this.playerName = playerName;
		setup();
	}
	
	/**
	 * Initial setup for a PlayerCard
	 */
	public void setup() {
		// Create a label with the player's name
		Label nameLabel = new Label(this.playerName);
		nameLabel.setTextFill(Color.WHITE);
		nameLabel.setTextAlignment(TextAlignment.CENTER);
		this.getChildren().add(nameLabel);
		
		// Add the player's character image view to the player card
		this.characterImageView = new ImageView();
		this.getChildren().add(characterImageView);
		
		// Add the player's high score label to the player card
		this.highScoreLabel = new Label("High Score: " + 0);
		this.highScoreLabel.setTextFill(Color.WHITE);
		this.highScoreLabel.setTextAlignment(TextAlignment.CENTER);
		this.getChildren().add(highScoreLabel);
	}
	
	/**
	 * Set the character image of the playe rcard
	 * @param characterImgName The character's image name (used to find file)
	 */
	public void setImage(String characterImgName) {
		try {
			Image characterImage = new Image(getClass().getResource(
										"/game_resources/" + characterImgName + "_left_25.png")
										.toURI().toString());
			this.characterImageView.setImage(characterImage);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the high score of the player card
	 * @param score The high score to set with 
	 */
	public void setScore(int score) {		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				highScoreLabel.setText("High Score: " + String.valueOf(score));					
			}
		});
	}
	
	/**
	 * Returns the player's name
	 * @return the player's name
	 */
	public String getPlayerName() {
		return this.playerName;
	}
	
	public String getScore() {
		return this.highScoreLabel.getText();
	}
	
}
