import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

public class OnlineChecker extends TimerTask{
	private ChatFrame chat;
	private String[] seznamOnline;
	
	public OnlineChecker(ChatFrame chat) throws ClientProtocolException, IOException{
		this.chat = chat;
		this.seznamOnline = extractUsername(Get.get());
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

	public void activate(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public void run(){
		try {
			seznamOnline = extractUsername(Get.get());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		chat.refreshOnline(seznamOnline);
	}

}
