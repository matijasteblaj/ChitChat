import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private String[] extractUsername(String string) {
		List<String> seznam = new ArrayList<String>();
		String pattern = "username\":\"(?<username>.*?)\"";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(string);
		while (m.find()){
			seznam.add(m.group("username"));
		}
		String[] navadenSeznam = new String[seznam.size()];
		seznam.toArray(navadenSeznam);
		return navadenSeznam;
	}
	
	@Override
	public void run(){
		String[] seznamOnline;
		try {
			seznamOnline = extractUsername(Get.get());
			chat.osvezi(seznamOnline);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
