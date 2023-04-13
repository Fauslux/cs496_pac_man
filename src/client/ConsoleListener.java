/**
 * 
 */
package client;

/**
 * An implementation of the Observer method that detects if 
 * the Lobby Server has sent a message.
 * @author Kevin Richardson
 * @version December 2022
 */
public interface ConsoleListener {
	
	/**
	 * Notifies any listeners that a message has appeared
	 * @param message The message that observers will be notified with
	 */
	public void statusNotify(String message);
}
