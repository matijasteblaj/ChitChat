import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.http.client.ClientProtocolException;

@SuppressWarnings("serial")
public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	
	private JTextArea output;
	private JTextField input;
	private JPanel vzdevek;
	JTextField vzdevekInput;
	private JPanel online;
	private JTextArea onlineOutput;
	private JButton prijava;
	private JButton odjava;
	private JPanel inputPanel;
	private JTextField prejemnik;
	private JLabel inputLabel;
	private JLabel prejemnikLabel;
	JTabbedPane tabbedPane;
	HashMap<String, JTextArea> tabTextAreaSlovar;
	private String[] seznamOnline;
	boolean prijavljen;
	
	public ChatFrame() throws ClientProtocolException, URISyntaxException, IOException {
		super();
		setTitle("ChitChat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		//JPanel vzdevek, ki vsebuje napis, vzdevekInput in gumba prijava, odjava
		this.vzdevek = new JPanel();
		JLabel napis = new JLabel("Vzdevek:");
		this.vzdevekInput = new JTextField(System.getProperty("user.name"), 40);
		FlowLayout vzdevekFlow = new FlowLayout();
		vzdevek.setLayout(vzdevekFlow);
		vzdevek.add(napis);
		vzdevek.add(vzdevekInput);
		this.prijava = new JButton("Prijava");
		prijava.addActionListener(this);
		prijava.setActionCommand("Prijava");
		vzdevek.add(prijava);
		this.odjava = new JButton("Odjava");
		odjava.setEnabled(false);
		odjava.addActionListener(this);
		odjava.setActionCommand("Odjava");
		vzdevek.add(odjava);
		GridBagConstraints vzdevekConstraint = new GridBagConstraints();
		vzdevekConstraint.gridx = 0;
		vzdevekConstraint.gridy = 0;
		pane.add(vzdevek, vzdevekConstraint);
		vzdevek.addKeyListener(this);
		
		//JPanel online, ki vsebuje napisOnline, onlineOutput
		this.online = new JPanel();
		JLabel napisOnline = new JLabel("Online:");
		GridBagConstraints napisOnlineConstraint = new GridBagConstraints();
		this.onlineOutput = new JTextArea(30,12);
		this.onlineOutput.setEditable(false);
		JScrollPane onlineOutputScrollPane = new JScrollPane(onlineOutput);
		GridBagConstraints onlineOutputConstraint = new GridBagConstraints();
		online.add(napisOnline, napisOnlineConstraint);
		online.add(onlineOutputScrollPane, onlineOutputConstraint);
		GridBagConstraints onlineConstraint = new GridBagConstraints();
		onlineConstraint.gridx = 1;
		onlineConstraint.gridy = 1;
		onlineConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(online, onlineConstraint);
		online.addKeyListener(this);
		
		//JTabbedPane tabbedPane, katerega prvi tab je output
		this.output = new JTextArea(20, 40);
		this.output.setEditable(false);
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 1;
		outputConstraint.fill = GridBagConstraints.BOTH;
		outputConstraint.weightx = 1.0;
		outputConstraint.weighty = 1.0;
		
		JScrollPane scrollPane = new JScrollPane(output);
		this.tabbedPane = new JTabbedPane();
		//Slovar, ki bo povezoval imena tab-a in njegov JTextArea
		this.tabTextAreaSlovar = new HashMap<String, JTextArea>();
		tabbedPane.add("Server", scrollPane);
		tabTextAreaSlovar.put("Server", output);
		pane.add(tabbedPane, outputConstraint);
		
		//Jpanel inputPanel, ki vsebuje input, inputLabel, prejemnikLabel, prejemnik
		this.inputPanel = new JPanel();
		this.input = new JTextField(40);
		this.prejemnikLabel = new JLabel("Prejemnik:");
		this.inputLabel = new JLabel("Sporocilo:");
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.fill = GridBagConstraints.HORIZONTAL;
		inputConstraint.weightx = 1.0;
		input.addKeyListener(this);
		this.prejemnik = new JTextField("Server", 20);
		inputPanel.add(prejemnikLabel);
		inputPanel.add(prejemnik);
		inputPanel.add(inputLabel);
		inputPanel.add(input);
		pane.add(inputPanel, inputConstraint);
		
		//Ko se klikne na tab, se nastavi prejemnik na naslov taba, in tab se pobarva na default barvo
		tabbedPane.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				prejemnik.setText(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
				tabbedPane.setBackgroundAt(tabbedPane.getSelectedIndex(), new Color(184, 207, 229));
			}
		});
		
		//Ko se okno odpre, gre fokus na textfield input
		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e) {
				input.requestFocusInWindow();
			}
		});
		
		this.prijavljen = false;		
	}

	//Izpise sporocilo "message" od osebe "person" v textarea output
	public void addMessage(String person, String message, JTextArea output) {
		if (this.tabTextAreaSlovar.keySet().size() == 0){
			this.addTab("Vsi", true);
		}
		String chat = output.getText();
		output.setText(chat + person + ": " + message + "\n");
		String key = "";
		for (String kljuc : this.tabTextAreaSlovar.keySet()){
			if (this.tabTextAreaSlovar.get(kljuc) == output){
				key = kljuc;
			}
		}
		int index = this.tabbedPane.indexOfTab(key);
		if (!(this.tabbedPane.getSelectedIndex() == index)){
			this.tabbedPane.setBackgroundAt(index, new Color(255, 204, 135));
		}
	}
	
	//Nastavi this.seznamOnline na "seznamOnline" in ustrezno popravi text v textarea onlineOutput
	public void osveziOnline(String[] seznamOnline){
		this.seznamOnline = seznamOnline;
		String text = "";
		for (String oseba : this.seznamOnline){
			text = text + oseba + "\n";
		}
		this.onlineOutput.setText(text);
	}
	
	//Iz seznama prejetih sporocil "seznamSporocil" razbere posiljatelja in sporocilo, in doda sporocilo v ustrezen tab
	public void osveziSporocila(String[] seznamSporocil){
		for (int i=0; i < seznamSporocil.length; i += 3){
			if (seznamSporocil[i].equals("true")){
				this.addTab("Vsi", false);
				this.addMessage(seznamSporocil[i+1], seznamSporocil[i+2], this.tabTextAreaSlovar.get("Vsi"));
			} else if(seznamSporocil[i].equals("false")) {
				this.addTab(seznamSporocil[i+1], false);
				this.addMessage(seznamSporocil[i+1], seznamSporocil[i+2], this.tabTextAreaSlovar.get(seznamSporocil[i+1]));
			} else {
				System.out.println("Nekaj cudnega se je zgodilo");
			}

		}
	}
	
	//Ce tab z imenom "prejemnik" se ne obstaja, ga naredi in se glede na "fokus" prestavi v njega ali pa ne
	private void addTab(String prejemnik, boolean fokus){
		if (!this.tabTextAreaSlovar.containsKey(prejemnik)){
			JTextArea textArea = new JTextArea(20, 40);
			textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textArea);
			this.tabbedPane.add(prejemnik, scrollPane);
			this.tabTextAreaSlovar.put(prejemnik, textArea);
			
			//Ime taba in gumb za zapiranje
			JPanel panelTab = new JPanel();
			JLabel tabTitle = new JLabel(prejemnik);
			JButton closeButton = new JButton("x");
			closeButton.setFont(new Font("Arial", Font.PLAIN, 10));
			Dimension size = new Dimension(15, 15);
			closeButton.setPreferredSize(size);
			closeButton.setMargin(new Insets(0, 0, 0, 0));
			panelTab.add(tabTitle);
			panelTab.add(closeButton);
			
			this.tabbedPane.setTabComponentAt(this.tabbedPane.indexOfTab(prejemnik), panelTab);
			closeButton.addActionListener(this);
			closeButton.setActionCommand("Zbrisi " + prejemnik);
		}
		if (fokus){
			this.tabbedPane.setSelectedIndex(this.tabbedPane.indexOfTab(prejemnik));
		}
	}
	
	//Poskusi prijaviti uporabnika na streznik, vrne true ce je prijava uspela, false sicer
	public boolean prijavi() throws ClientProtocolException, URISyntaxException, IOException{
		if (!this.jePrijavljen(this.vzdevekInput.getText())){
			Post.post(vzdevekInput.getText());
			return true;
		} else {
			return false;
		}
		
	}
	
	//Poskusi odjaviti uporabnika iz streznika, vrne true ce je odjava uspela, false sicer
	public boolean odjavi() throws ClientProtocolException, URISyntaxException, IOException{
		if (this.jePrijavljen(this.vzdevekInput.getText())){
			Delete.delete(vzdevekInput.getText());
			return true;
		} else {
			return false;
		}
	}


	//Funkcija, ki vrne true ce je uporabnik prijavljen na streznik in false sicer
	public boolean jePrijavljen(String oseba) throws ClientProtocolException, IOException{
		return Arrays.asList(this.seznamOnline).contains(this.vzdevekInput.getText());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		//Kliknjen je bil gumb prijava
		if (action.equals("Prijava")){
			try {
				if (this.prijavi()){
					this.prijava.setEnabled(false);
					this.odjava.setEnabled(true);
					this.vzdevekInput.setEditable(false);
					this.prijavljen = true;
					if (this.tabTextAreaSlovar.keySet().size() == 0){
						this.addTab("Vsi", true);
					}
					this.addMessage("Server", "Uporabnik uspesno prijavljen", this.tabTextAreaSlovar.get("Server"));
				} else if (!this.prijavi()) {
					this.addMessage("Server", "Prijava neuspesna, morda je ta uporabnik ze prijavljen?",
							this.tabTextAreaSlovar.get("Server"));
				}
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		//Kliknje je bil gumb odjava
		} else if(action.equals("Odjava")){
			try {
				if (this.odjavi()){
					ArrayList<String> seznamTabov = new ArrayList<String>();
					for (String tab: this.tabTextAreaSlovar.keySet()){
						if(!(tab == "Server")){
							seznamTabov.add(tab);
							this.tabbedPane.remove(this.tabbedPane.indexOfTab(tab));
						}
					}
					for (String tab: seznamTabov){
						this.tabTextAreaSlovar.remove(tab);
					}
					this.prijava.setEnabled(true);
					this.odjava.setEnabled(false);
					this.vzdevekInput.setEditable(true);
					this.prijavljen = false;
					if (this.tabTextAreaSlovar.keySet().size() == 0){
						this.addTab("Vsi", true);
					}
					this.addMessage("Server", "Uporabnik uspesno odjavljen",this.tabTextAreaSlovar.get("Server"));
				} else if (!this.odjavi()) {
					if (this.tabTextAreaSlovar.keySet().size() == 0){
						this.addTab("Vsi", true);
					}
					this.addMessage("Server", "Odjava neuspesna, morda ta uporabnik ni prijavljen?",
							this.tabTextAreaSlovar.get("Server"));
				}
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		//Kliknjen je bil gumb za zapiranje taba
		}  else if(action.contains("Zbrisi")){
			String prejemnik = action.split(" ")[1];
			this.tabTextAreaSlovar.remove(prejemnik);
			this.tabbedPane.remove(this.tabbedPane.indexOfTab(prejemnik));
			
		}
			
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				if (this.prejemnik.getText().length() == 0){
					this.addMessage("Server", "Vnesite vzdevek prejemnika",this.tabTextAreaSlovar.get("Server"));
				} else if (this.prejemnik.getText() == "Server"){
					this.addMessage(this.vzdevekInput.getText(), this.input.getText(), this.tabTextAreaSlovar.get("Server"));
				} else if (this.input.getText().length() == 0){
					;
				} else {
					try {
						if (this.jePrijavljen(this.vzdevekInput.getText())){
							this.addTab(this.prejemnik.getText(), true);
							if (this.prejemnik.getText().equals("Server")){
								;
							}else if (this.prejemnik.getText().equals("Vsi")){
								Send.sendAll(this.vzdevekInput.getText(), this.input.getText());
							} else {
								Send.send(this.vzdevekInput.getText(), this.prejemnik.getText(), this.input.getText());
							}
							this.addMessage(this.vzdevekInput.getText(), this.input.getText(),
									this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));
							this.input.setText("");
						} else {
							if (this.tabTextAreaSlovar.keySet().size() == 0){
								this.addTab("Vsi", true);
							}
							this.addMessage("Server", "Za posiljanje sporocil morate biti prijavljeni",
									this.tabTextAreaSlovar.get("Server"));
						}
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		;
	}
}
