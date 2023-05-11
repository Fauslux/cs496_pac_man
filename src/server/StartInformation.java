/**
 * 
 */
package server;

/**
 * @author Kevin
 *
 */
public class StartInformation {
	
	private String hostOfGame;
	
	private int numberOfPlayers;
	
	private String[] playerList;
	
	private String code;
	
	public StartInformation(String hostOfGame,
			int numberOfPlayers, String[] playerList, String code) {
		this.hostOfGame = hostOfGame;
		this.numberOfPlayers = numberOfPlayers;
		this.playerList = playerList;
		this.code = code;
	}


    public String getPlayer(int index) {
		String playerName = null;
		if(this.playerList.length > 0) {
			if(this.playerList[index] != null) {
				playerName = this.playerList[index];
			}
		}
		
		return playerName;
	}
	
	/**
     * Gets a player and removes it if successful.
     * @param index - An integer that represents the position in the list of
     * player names.
     * 
     * @return Name of a player if found or null if it doesn't exist. 
     */
	public boolean removePlayer(int index) {
		boolean couldRemovePlayer = false;
		
		if(getPlayer(index) != null) {
			this.playerList[index] = null;
			couldRemovePlayer = true;
		}
		
		return couldRemovePlayer;
	}
	
	   /**
     * @return the code
     */
    public String getCode() {
        return code;
    }


    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
	
	/**
	 * @return the hostOfGame
	 */
	public String getHostOfGame() {
		return hostOfGame;
	}

	/**
	 * @param hostOfGame the hostOfGame to set
	 */
	public void setHostOfGame(String hostOfGame) {
		this.hostOfGame = hostOfGame;
	}

	/**
	 * @return the numberOfPlayers
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * @param numberOfPlayers the numberOfPlayers to set
	 */
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	/**
	 * @return the playerList
	 */
	public String[] getPlayerList() {
		return playerList;
	}

	/**
	 * @param playerList the playerList to set
	 */
	public void setPlayerList(String[] playerList) {
		this.playerList = playerList;
	}
	
	

}
