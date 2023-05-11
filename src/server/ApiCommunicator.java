package server;

import java.util.Timer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ApiCommunicator implements Runnable {

	private String instanceIdentification;
	
	private boolean gotStartInformation;
	
	private ApiScheduledTask apiScheduledTask;
	
	private StartInformation gameStartInformation;
	
	TalkToLobby linkToLobby;	
	
	public ApiCommunicator() {
		this.gotStartInformation = false;
		linkToLobby = new TalkToLobby();
	}
	
	public String getHostName() {
	    return this.gameStartInformation.getHostOfGame();
	}
	
	public String[] getPlayers() {
        return this.gameStartInformation.getPlayerList();
    }
	
	public boolean gotStartInformation() {
	    return this.gotStartInformation;
	}
	
	public int getNumberOfPlayers() {
	    return this.gameStartInformation.getNumberOfPlayers();
	}
	
	public void addWinner(String winner) {
        this.apiScheduledTask.addPlayerToWinners(winner);
    }
	
	public void addLoser(String loser) {
        this.apiScheduledTask.addPlayerToLosers(loser);
    }
	
	public void setRunningStatus(boolean value) {
	    apiScheduledTask.setGameRunningStatus(value);
	}

	public void setInstanceIdentification(String instanceIdentification) {
	    this.instanceIdentification = instanceIdentification;
	}
	
	public void scheduleStatusUpdates() {
	    apiScheduledTask = new ApiScheduledTask(instanceIdentification, 
	            gameStartInformation.getHostOfGame());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(apiScheduledTask, 3000, 3000);
        
	}
	
	public StartInformation getStartInformation() {
		return this.gameStartInformation;
	}
	
	public String getCode() {
	    return this.gameStartInformation.getCode();
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
	
	@Override
	public void run() {
		if(!gotStartInformation) {
			String results = new String();
			StringBuilder message = new StringBuilder();
			message.append("curl -X GET http://localhost:4567"
					+ "/get_game_information -H "
	    			+ "\"Content-Type:application/json\" -d \"");
			message.append("\"{instanceName:\"" + instanceIdentification + "}\"");
			
			
			try {		
				results = linkToLobby.sendMessageToApi(message.toString());
				
				System.out.println("RESULT " + results);
				
				JSONParser resultsParser = new JSONParser();
	
				JSONObject resultJSONObject = (JSONObject) resultsParser
					.parse(results);
				
				System.out.println("GameStartInformation is " + resultJSONObject);
				
				if(resultJSONObject != null) {
					Long numberOfPlayersLong = (Long) 
							resultJSONObject.get("number_of_players");
					int numberOfPlayers = numberOfPlayersLong.intValue();
					
					String hostName = (String) 
							resultJSONObject.get("game_host");
					
					String code = (String) 
                            resultJSONObject.get("codeNumber");
					
					String[] playerList = this.jsonArrayToArray((JSONArray) 
							resultJSONObject.get("active_players"));
					
					System.out.println("PLAY " + playerList.length);
					
					gameStartInformation = new StartInformation
							(hostName, numberOfPlayers, playerList, code);
					
					gotStartInformation = true;
					scheduleStatusUpdates();
	                   
                    this.apiScheduledTask.setWinnersList(new 
                            String[numberOfPlayers]);
                    this.apiScheduledTask.setLosersList(new 
                            String[numberOfPlayers]);
                    this.apiScheduledTask.setTieersList(new 
                            String[numberOfPlayers]);
					
				}
					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Caught!");
				e.printStackTrace();
			}
		}
		
	}
}
