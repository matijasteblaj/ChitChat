import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public class ChitChat {

	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
		ChatFrame chatFrame = new ChatFrame();
		PrimeRobot robot = new PrimeRobot(chatFrame);
		robot.activate();
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
