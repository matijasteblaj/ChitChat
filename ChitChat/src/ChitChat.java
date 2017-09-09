import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.WindowConstants;

import org.apache.http.client.ClientProtocolException;

public class ChitChat {

	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
		ChatFrame chatFrame = new ChatFrame();
		chatFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		PrimeRobot robot = new PrimeRobot(chatFrame);
		robot.activate();
		OnlineChecker onlineChecker = new OnlineChecker(chatFrame);
		onlineChecker.activate();
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
