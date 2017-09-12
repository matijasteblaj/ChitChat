import java.io.IOException;
import java.net.URISyntaxException;
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
		timer.scheduleAtFixedRate(this, 0, 1000);
	}
	
	private String[] extractUsernames(String string) {
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
	
	private String[] extractMessages(String string){
		List<String> seznam = new ArrayList<String>();
		String pattern = "\"global\":(?<global>.*?),.*?\"sender\":\"(?<posiljatelj>.*?)\",\"text\":\"(?<sporocilo>.*?)\"";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(string);
		while(m.find()){
			seznam.add(m.group("global"));
			seznam.add(m.group("posiljatelj"));
			seznam.add(m.group("sporocilo"));
		}
		String[] navadenSeznam = new String[seznam.size()];
		seznam.toArray(navadenSeznam);
		return navadenSeznam;
	}
	
	@Override
	public void run(){
		try {
			if (chat.prijavljen){
				String prejemnik = chat.vzdevekInput.getText();
				String[] seznamSporocil = extractMessages(Receive.receive(prejemnik));
				chat.osveziSporocila(seznamSporocil);
			}
			chat.osveziOnline(extractUsernames(Get.get()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

	}
}
