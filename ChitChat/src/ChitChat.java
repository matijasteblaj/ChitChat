import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.WindowConstants;

import org.apache.http.client.ClientProtocolException;

public class ChitChat {

	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
		ChatFrame chatFrame = new ChatFrame();
		chatFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		PrimeRobot robot = new PrimeRobot(chatFrame);
//		robot.activate();
		Checker checker = new Checker(chatFrame);
		checker.activate();
		chatFrame.pack();
		chatFrame.setMinimumSize(chatFrame.getSize());
		chatFrame.setVisible(true);
		chatFrame.getSize();
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        try {
					chatFrame.odjavi();
				} catch (URISyntaxException | IOException e) {
					e.printStackTrace();
				}
		    }
		});

	}

}
