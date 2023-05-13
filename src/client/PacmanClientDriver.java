package client;


/**
 * This is the driver for the client portion of the Pacman game.
 * @author Kevin Richardson
 * @author Dillon Orr
 * @version May 2023
 */

public class PacmanClientDriver {
    /** The port of the server socket that the client will connect to */
	private static int port;
	/** The IP address of the server socket that the client will connect to */
	static private String addr;
	/** The instance of LobbyClient that the driver will use */
	public static PacmanClient pacmanClient;
	/** The listener for incoming messages in the console */
	public static ConsoleListener conList;
	/** Name of player for verification*/
	static private String playerName;
	/** Room id this player is coming from*/
	static private String code;
	
	
	/**
	 * Constructor for a ClientDriver
	 * @param address The address of the server socket to connect to
	 * @param portNumber The port of the server socket to connect to
	 */
	public PacmanClientDriver(String address, String portNumber, 
	        String playerName, String code) {
		changeAddr(address);
		changePort(portNumber);
		changePlayerName(playerName);
		changeCode(code);
	}
	
	/**
	 * Gets the player name for this client
	 * @return A String containing a player's name.
	 */
	public static String getPlayerName() {
	    return PacmanClientDriver.playerName;
	}
	
	/** 
	 * Gets the room id code for this game instance.
	 * @return A String containing the room id code.
	 */
	public static String getCode() {
	    return PacmanClientDriver.code;
    }
	
	/**
	 * Changes player name so when sent to the server it can be verified.
	 * @param playerName - Name of Player
	 */
	private void changePlayerName(String playerName) {
	    this.playerName = playerName;
	}
	
	/**
	 * Changes code number so the game knows that this player was a part
	 * of the room when they joined.
	 * @param code - Room id coming from Olympia
	 */
	private void changeCode(String code) {
        this.code = code;
    }
	
	/**
	 * Change the address of the socket that client wants to connect to
	 * @param address The address of the socket that client wants to conect to
	 */
	private void changeAddr(String address) {
		PacmanClientDriver.addr = address;
	}
	
	/**
	 * Change the port of the socket that client wants to connect to
	 * @param port The port of the socket that client wants to conect to
	 */
	private void changePort(String portNumber) {
		PacmanClientDriver.port = Integer.parseInt(portNumber);
	}
	
    /**
     * The main method that will create a LobbyClient, call its connect() method and parse input 
     * @param args Command line arguments that should include the hostname and the port number 
     */
    public static void main(String[] args) {
    	 try {
             if(args.length == 4) {
                 PacmanClientDriver drive = new PacmanClientDriver(args[0], 
                         args[1], args[2], args[3]);
                 drive.connect();
                 PacmanClientGUI.go(args);
             } else {
            	 // Print a usage message if a hostname and port is not entered
                 System.out.println("Usage: java client.ClientDriver <hostname> <port>");
             }
         } catch (NumberFormatException nfe) {
             System.out.println(nfe.getMessage());
         }
    }
    
    /**
     * Method used to create LobbyClient and connect it to the server
     */
    public void connect() {
        pacmanClient = new PacmanClient(addr, port);
        pacmanClient.connect(conList);
    }

    /**
     * Method used to reconnect the client to the server
     */
    public static void reconnect() {
        pacmanClient.connect(conList);
    }
    
    /** 
     * Method used to send a command to the server
     * @param command The command to send to the server
     */
    public static void doCommand(String command) {
    	System.out.println("Sending message: " + command);
    	
    	// If the ConnectionAgent of LobbyClient is not null...
    	if(pacmanClient.getAgent() != null) {    		
    		// If the ConnectionAgent's socket is not cloesd...
    		if (!pacmanClient.getAgent().getSocket().isClosed()) {
                pacmanClient.send(command);
                if (command.trim().equals("close")) {
                    pacmanClient.getAgent().close();
                    System.exit(0);
                }
            }
    	}
        else {
        	// If the message being sent is "close"...
            if (command.trim().equals("close")) {
                System.exit(0);
            }
        }
    }
	
}
