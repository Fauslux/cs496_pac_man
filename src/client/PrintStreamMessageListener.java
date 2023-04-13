package client;

import common.MessageListener;
import common.MessageSource;
import java.io.PrintStream;

/**
 * This class is responsible for for writing messages to a PrintStream.
 * It implements the MessageListener inteface indicating itplays the role of "observer"
 * in an instance of the observer pattern.
 * @author Dillon Orr
 * @author Kevin Richardson
 * @version December 2022
 */
public class PrintStreamMessageListener implements MessageListener {
    /** The stream to print a message to the client */
    PrintStream out;
    /** StringBuilder used to hold messages */
    static StringBuilder oldMessages;
    /** ConsoleListener that notifies using messages */
    ConsoleListener conListener;
    
    /**
     * The constuctor for a PrintStreamMessageListener
     * @param out Output stream
     */
    public PrintStreamMessageListener(PrintStream out) {
        this.out = out;
        oldMessages = new StringBuilder();
    }

    /**
     * The constuctor for a PrintStreamMessageListener
     * @param out Output stream
     * @param cl The ConsoleListener used to notify with messages
     */
    public PrintStreamMessageListener(PrintStream out, ConsoleListener cl) {
        this.out = out;
        oldMessages = new StringBuilder();
        this.conListener = cl;
    }

    /**
     * Set the listener of this PrintStreamMessageListener
     * @param cl The ConsoleListener to listen to
     */
    public void setListener(ConsoleListener cl) {
    	this.conListener = cl;
    	System.out.println("Stream has a new listener!");
    }
    
    /**
     * Used to notify observers that the subject has received a message.
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {
    	// If the length of the message is greater than 2, clear oldMessages
    	if(message.length() > 2) {
    		oldMessages.delete(0, oldMessages.length());
    	}
    	// If the index of a message (and newline) is not found, append it
    	if(oldMessages.indexOf(message + "\n") == -1) {
    		oldMessages.append(message + "\n");
    	}
    	// If the console listener isn't null, notify with the message
    	if(this.conListener != null) {
    		System.out.println("Listen to this" + this.conListener + " " + oldMessages.toString());
    		this.conListener.statusNotify(oldMessages.toString());
    		
    	}
    	else
    		System.out.println("Listen to this" + message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     * @param source The MessageSource that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {
        this.out.close();
    }
    
    /**
     * Testing toString used for the PrintStreamMessageListener
     */
    public String toString() {
    	return "Printer";
    }
}
