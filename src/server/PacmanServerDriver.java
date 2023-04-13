package server;

import java.io.IOException;

/**
 * The driver class for the Pacman game server
 * @author Dillon Orr
 * @version March 2023
 */

public class PacmanServerDriver {
	/** The default port number */
    private static final int DEFAULT_PORT = 5699;

    /**
     * The main method that will initialize a PacmanServer and call its listen method. 
     * @param args a String list of arguments
     */
    public static void main(String[] args) {
        
        int port = DEFAULT_PORT;
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            } else{
                System.out.println("Usage: java server.PacmanServerDriver <port>");
                System.exit(1);
            }
            // Create the LobbyServer and tell it to start listening for incoming messages
            PacmanServer pacServer = new PacmanServer(port);
			System.out.println("LS");
            pacServer.listen(); 
        
        } catch(NumberFormatException nfe) {
            System.out.println("Number Format Exception:"  + nfe.getMessage());
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch(IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        }
    }
	
	
}
