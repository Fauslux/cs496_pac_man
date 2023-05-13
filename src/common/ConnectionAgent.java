package common;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The class is responsible for sending messages and receiving messages from remote hosts.
 * @author Fernando Rodriguez 
 * @author Dillon Orr 
 * @version Decemeber 2021 
 */
public class ConnectionAgent extends MessageSource implements Runnable {
    /** The socket that the agent corresponds to */
    private Socket socket;
    /** Scanner to receive input */
    private Scanner in;
    /** PrintStream to generate output */
    private PrintStream out;
    /** Thread that corresponds to a specific connection agent */
    private Thread thread;
    
    /**
     * Constructor that creates a ConnectionAgent using a socket parameter
     * @param socket: the socket that corresponds to the connection agent
     */
    public ConnectionAgent(Socket socket) {
        super(); 
        this.socket = socket;
        try {
            this.in = new Scanner(this.socket.getInputStream()); 
            this.out = new PrintStream(this.socket.getOutputStream());  
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        };
    }

    /**
     * Method that is responsible for sending a message to all listeners
     * @param message: the message to send to all listeners
     */
    public void sendMessage(String message) {
        // super.notifyReceipt(message);
        out.println(message);
    }

    /**
     * Determines if the connection agent is connected from the socket
     * @return true if the agent is connected
     */
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    /**
     * Closes the connection agent and thus the connection
     */
    public void close() {
        try {
            this.socket.close();
            this.in.close();
            this.out.close();
            thread.interrupt();
            closeMessageSource();
        } catch(IOException ioe) {
            System.out.println("Caught from Conection Agent" + ioe.getMessage());
        }
    }

    /** 
     * Method that runs whenever the thread corresponding to this agent is started
     */
    public void run() {
        //sets the thread to current thead being executed
        this.thread = Thread.currentThread(); 

        while(!this.thread.isInterrupted() && !this.socket.isClosed()) {
            if(in.hasNextLine()) {
                String message = in.nextLine();
                super.notifyReceipt(message);
            }
        }
    }

    /**
     * Gets the socket
     * @return the socket
     */
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Gets the output stream
     * @return output stream
     */
    public PrintStream getOut() {
        return this.out;
    }
}
