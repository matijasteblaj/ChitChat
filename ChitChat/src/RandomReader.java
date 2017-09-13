import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class RandomReader extends TimerTask {
	private ChatFrame chat;
	private String text;
	private int prejsen;
	
	public RandomReader(ChatFrame chat, String path){
		this.chat = chat;
		this.text = "";
		this.prejsen = -1;
		
		try {
			BufferedReader vhod =
					new BufferedReader(new FileReader(path));
			while (vhod.ready()){
				this.text = this.text + vhod.readLine() + "\n"; 
			}
			vhod.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void activate() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 2000, 10000);
	}
	

	@Override
	public void run() {
		String[] vrstice = this.text.split("\n");
		int randomInt = ThreadLocalRandom.current().nextInt(0, vrstice.length);
		//Izogibamo se temu da bi dvakrat izpisal isto vrstico
		while (randomInt == this.prejsen){
			randomInt = ThreadLocalRandom.current().nextInt(0, vrstice.length);
		}
		this.prejsen = randomInt;
		//V tab "Server" napisemo nakljucno vrstico iz dane datoteke
		chat.addMessage("Server", vrstice[randomInt], chat.tabTextAreaSlovar.get("Server"));
	}

}
