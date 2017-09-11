import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.ClientProtocolException;

public class Checker extends TimerTask{
	private ChatFrame chat;
	
	public Checker(ChatFrame chat) throws ClientProtocolException, IOException{
		this.chat = chat;
	}

	public void activate(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public void run(){
		chat.osvezi();
	}
}
