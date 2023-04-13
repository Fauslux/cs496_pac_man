package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * This class represents a list of players that will be used in the MainLobbyScreen and
 * within a RoomScreen.
 * @author Dillon Orr
 * @version December 2022
 */
public class PlayerList extends VBox {
	/** Observable list of player strings */
    ObservableList<String> playerStrings;
    
    /**
     * Constructor for a PlayerList
     */
    public PlayerList() {
    	setup();
    }
    
    /**
     * Initial method used to set up GUI elements
     */
	public void setup() {
		Label playersLabel = new Label("Players:");
        ListView<String> playerList = new ListView<String>();
        playerStrings = FXCollections.observableArrayList();
        playerList.setItems(playerStrings);
        playerList.getStyleClass().add("playerList");
        
        this.getChildren().add(playerList);
	}
	
	/**
     * Add a player to the player list
     * @param playerListing the player to add
     */
    public void addPlayer(String playerListing) {
    	this.playerStrings.add(playerListing);
    }
    
    /**
     * Remove a player from the player list
     * @param playerListing the player to remove
     */
    public void removePlayer(String playerListing) {
    	this.playerStrings.remove(playerListing);
    }
    
}
