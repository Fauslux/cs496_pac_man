/**
 * 
 */
package server;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Kevin
 *
 */
public class TalkToLobby {

	public String sendMessageToApi(String apiMessage) {
		StringBuilder exitMessage = new StringBuilder();
		try {
			Process process = Runtime.getRuntime().exec(apiMessage);
			Scanner stdInput = new Scanner(process.getInputStream());
			while(stdInput.hasNext()) {
				exitMessage.append(stdInput.nextLine());
			}
			process.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			exitMessage.append(e.getLocalizedMessage());
		}
		return exitMessage.toString();
	}

}
