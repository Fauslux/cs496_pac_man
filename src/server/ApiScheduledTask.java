package server;

import java.util.TimerTask;

import org.json.simple.JSONArray;

public class ApiScheduledTask extends TimerTask {
	
	private String nameOfRoom;

	private String responseMessage;
	
	private String[] active_players;
	
	private String[] winners;
	
	private String[] losers;
	
	private String[] tieers;
	
	private String hostName;
	
	private int numberOfPlayers;
	
	private TalkToLobby linkToGame;
	
	boolean gameRunning;
	
	public ApiScheduledTask(String nameOfRoom, String hostName) {
		this.nameOfRoom = nameOfRoom;
	      linkToGame = new TalkToLobby();
	    gameRunning = true;
	}
	
	
	public void setGameRunningStatus(boolean value) {
	    this.gameRunning = value;
	}
	
	public void setActivePlayersList(String[] playerList) {
		this.active_players = playerList;
	}
	
	public void setWinnersList(String[] playerList) {
		this.winners = playerList;
	}
	
	public void setLosersList(String[] playerList) {
		this.losers = playerList;
	}
	
	public void setTieersList(String[] playerList) {
		this.tieers = playerList;
	}
	
	public void addPlayerToWinners(String playerName) {
		addStringToList(playerName, this.winners);
	}
	
	public void addPlayerToLosers(String playerName) {
		addStringToList(playerName, this.winners);
	}
	
	public void addPlayerToTieers(String playerName) {
		addStringToList(playerName, this.winners);
	}
	
	public void removePlayerToWinners(String playerName) {
		removeStringToList(playerName, this.winners);
	}
	
	public void removePlayerToLosers(String playerName) {
		removeStringToList(playerName, this.winners);
	}
	
	public void removePlayerToTieers(String playerName) {
		removeStringToList(playerName, this.winners);
	}
	
	public int getPlayerPosition(String entry, String[] list) {
		int indexOfEntry = -1;
		for(int i = 0; i < list.length; i++) {
			if(list[i] != null && list[i] == entry) {
				indexOfEntry = i;
			}
		}
		
		return indexOfEntry;
	}
	
	public void removeStringToList(String entry, String[] list) {
		int indexOfPlayer = getPlayerPosition(entry, list);
		if(indexOfPlayer >= 0) {
			list[indexOfPlayer] = null;
		}
		
		for(int i = indexOfPlayer; i < list.length-1; i++) {
			for(int j = i; j < list.length; j++) {
				list[i] = list[j];
			}
		}
		
	}
	
	public void addStringToList(String entry, String[] list) {
		boolean foundEmpty = false;
		for(int i = 0; i < list.length; i++) {
			if(!foundEmpty && list[i] != null) {
				list[i] = entry;
				foundEmpty = true;
			}
		}
	}
	
	/**
	 * @return the nameOfRoom
	 */
	public String getNameOfRoom() {
		return nameOfRoom;
	}

	private String[] jsonArrayToArray(JSONArray jsonArray) {
		String[] array = new String
				[jsonArray.size()];
		for(int i = 0; i < jsonArray.size(); i++) {
			array[i] = (String)
					jsonArray.get(i);
		}
		
		return array;
	}

	/**
	 * @param nameOfRoom the nameOfRoom to set
	 */
	public void setNameOfRoom(String nameOfRoom) {
		this.nameOfRoom = nameOfRoom;
	}



	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}



	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String stringArrayToJSONArray(String[] playerList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		
		if(playerList != null) {
        	for(int i = 0; i < playerList.length; i++) {
        		if(playerList[i] != null) {
        			stringBuilder.append("\"" + playerList[i] + 
    	    				"\"");
    	    		if(i+1 < playerList.length && playerList[i+1] != null) {
    	    			stringBuilder.append(",");
    	    		}
        		}
        	}
		}
		stringBuilder.append("]");
    	
    	return stringBuilder.toString();
	}

	@Override
	public void run() {
		StringBuilder apiMessage = 
    			new StringBuilder();
		
    	apiMessage.append("curl -X POST http://localhost:"
    			+ "4567/make_game_status -H "
    			+ "\"Content-Type:application/json\" ");
    	apiMessage.append("-d {\"gameInformation\":{instanceName:\"" + 
    			nameOfRoom + "\",\"playerDetails\":{");
    	
    	
        apiMessage.append("number_of_players:" + 
                this.numberOfPlayers + "");
        

        apiMessage.append(",playerNames:");
        apiMessage.append(stringArrayToJSONArray(this.active_players));
        
        apiMessage.append(",hostOfGame:\"" + 
                hostName + "\"}}");
        
        apiMessage.append(",\"gameRunning\":" + gameRunning
                + ",\"gameCrashed\":false");
        
    	System.out.println("Pac-Man Run");
    	apiMessage.append(",\"winners\":");
    	apiMessage.append(stringArrayToJSONArray(this.winners));
    	apiMessage.append(",\"losers\":");
    	apiMessage.append(stringArrayToJSONArray(this.losers));   	
    	apiMessage.append(",\"tieers\":");
    	apiMessage.append(stringArrayToJSONArray(this.tieers));
    	apiMessage.append("}\"");
    	
    	//System.out.println("Pac-Man Run2 "  + apiMessage.toString());
    	linkToGame.sendMessageToApi(apiMessage.toString());
    	//System.out.println();
    	System.out.println("Game Running is " + gameRunning);
    	if(!this.gameRunning) {
    	    this.cancel();
    	}
    	
	}
}
