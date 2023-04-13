package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * This class implements the client side of the Lobby. It is used to connect to the server
 * through the ConnectionAgent, and listens to messages from the server.  
 * @author Dillon Orr
 * @author Kevin Richardson
 * @version March 2023
 */

public class PacmanClient extends MessageSource implements MessageListener {
    /** The host address it will connect to */
    private InetAddress host;
    /** The port it will connect on */
    private int port;
    /** The ConnectionAgent it will use to communicate */
    private ConnectionAgent agent; 
    /** The PrintStreamMessageListener that will print output */
    private PrintStreamMessageListener listener;
	
    /**
     * Constructor for a Pacman client
     * @param hostname the hostname
     * @param port the port number
     */
    public PacmanClient(String hostname, int port) {
        try {
            this.host = InetAddress.getByName(hostname);
            this.port = port;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
    
    
    
    /**
     * Method used to connect to the server
     */
    public void connect(ConsoleListener conList) {
        try {
            this.agent = new ConnectionAgent(new Socket(host, port));
            this.agent.addMessageListener(this);
            this.listener = new PrintStreamMessageListener(System.out, conList);
            super.addMessageListener(this.listener);
            
            Thread clientThread = new Thread(agent);
            clientThread.start();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Set the PrintMessageStreamListener
     * @param conList The new listener
     */
    public void setStreamListener(ConsoleListener conList) {
    	if (this.listener == null) {
    		this.listener = new PrintStreamMessageListener(System.out);
    	}
    	this.listener.setListener(conList);
    }
    
    @Override
    public void messageReceived(String message, MessageSource source) {
        if (message.trim().equals("close")) {
            this.agent.close();
        } else if (!message.equals("")) {
        	// If the message is not blank, then notify 
        	// !!TODO: This is a temporary solution - client is receiving 2 messages
        	// one with a 'good message' and a blank message. When the blank message is received,
        	// it sends notifies with the 'good message' again.
            notifyReceipt(message);
        }
    }
    
	@Override
	public void sourceClosed(MessageSource source) {
        closeMessageSource();		
	}
	
    /**
     * Sends the message to the ConnectionAgent which will then send it to the server.
     * @param message The message to send to the server
     */
    public void send(String message) {
        agent.sendMessage(message);
    }

    /**
     * Getter method for the ConnectionAgent agent
     * @return the ConnectionAgent
     */
    public ConnectionAgent getAgent() {
        return this.agent;
    }
}
