import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
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

import org.apache.http.client.ClientProtocolException;

@SuppressWarnings("serial")
public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	
	private JTextArea output;
	private JTextField input;
	private JPanel vzdevek;
	private JTextField vzdevekInput;
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
		odjava.addActionListener(this);
		odjava.setActionCommand("Odjava");
		vzdevek.add(odjava);
		GridBagConstraints vzdevekConstraint = new GridBagConstraints();
		vzdevekConstraint.gridx = 0;
		vzdevekConstraint.gridy = 0;
		pane.add(vzdevek, vzdevekConstraint);
		vzdevek.addKeyListener(this);
		
		
		this.online = new JPanel();
		JLabel napisOnline = new JLabel("Online:");
		this.onlineOutput = new JTextArea(30,12);
		this.onlineOutput.setEditable(false);
		FlowLayout onlineFlow = new FlowLayout();
		online.setLayout(onlineFlow);
		online.add(napisOnline);
		online.add(onlineOutput);
		GridBagConstraints onlineConstraint = new GridBagConstraints();
		onlineConstraint.gridx = 1;
		onlineConstraint.gridy = 0;
		onlineConstraint.gridheight = 2;
		pane.add(online, onlineConstraint);
		online.addKeyListener(this);
		

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
		this.tabTextAreaSlovar = new HashMap<String, JTextArea>();
		tabbedPane.add("Vsi", scrollPane);
		tabTextAreaSlovar.put("Vsi", output);
		pane.add(tabbedPane, outputConstraint);

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
		this.prejemnik = new JTextField("Vsi", 20);
		inputPanel.add(prejemnikLabel);
		inputPanel.add(prejemnik);
		inputPanel.add(inputLabel);
		inputPanel.add(input);
		pane.add(inputPanel, inputConstraint);
		
		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e) {
				input.requestFocusInWindow();
			}
		});
		
		this.prijavljen = false;		
	}

	/**
	 * @param person - the person sending the message
	 * @param message - the message content
	 * @param output - the JTextArea to which the message will be added
	 */
	public void addMessage(String person, String message, JTextArea output) {
		String chat = output.getText();
		output.setText(chat + person + ": " + message + "\n");
	}
	
	public void osveziOnline(String[] seznamOnline){
		this.seznamOnline = seznamOnline;
		String text = "";
		for (String oseba : this.seznamOnline){
			text = text + oseba + "\n";
		}
		this.onlineOutput.setText(text);
	}
	
	public void osveziSporocila(String[] seznamSporocil){
		for (int i=0; i+2 < seznamSporocil.length; i += 2){
			this.addTab(seznamSporocil[i]);
			this.addMessage(seznamSporocil[i], seznamSporocil[i+1], this.tabTextAreaSlovar.get(seznamSporocil[i]));
		}
	}
	
	private void addTab(String prejemnik){
		if (!this.tabTextAreaSlovar.containsKey(prejemnik)){
			JTextArea textArea = new JTextArea(20, 40);
			textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textArea);
			this.tabbedPane.add(prejemnik, scrollPane);
			this.tabTextAreaSlovar.put(prejemnik, textArea);
		}
		this.tabbedPane.setSelectedIndex(this.tabbedPane.indexOfTab(prejemnik));
	}
	
	public boolean prijavi() throws ClientProtocolException, URISyntaxException, IOException{
		if (!this.jePrijavljen(this.vzdevekInput.getText())){
			Post.post(vzdevekInput.getText());
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean odjavi() throws ClientProtocolException, URISyntaxException, IOException{
		if (this.jePrijavljen(this.vzdevekInput.getText())){
			Delete.delete(vzdevekInput.getText());
			return true;
		} else {
			return false;
		}
	}


	
	public boolean jePrijavljen(String oseba) throws ClientProtocolException, IOException{
		return Arrays.asList(this.seznamOnline).contains(this.vzdevekInput.getText());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Prijava")){
			try {
				if (this.prijavi()){
					//this.prijava.setEnabled(false);
					//this.vzdevekInput.setEditable(false);
					//this.prijavljen = true
					this.addMessage("Server", "Uporabnik uspesno prijavljen", this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));
				} else if (!this.prijavi()) {
					this.addMessage("Server", "Prijava neuspesna, morda je ta uporabnik ze prijavljen?", this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));
				}
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		} else if(action.equals("Odjava")){
			try {
				if (this.odjavi()){
					//this.prijava.setEnabled(true);
					//this.vzdevekInput.setEditable(true);
					//this.prijavljen = false;
					this.addMessage("Server", "Uporabnik uspesno odjavljen", this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())) );
				} else if (!this.odjavi()) {
					this.addMessage("Server", "Odjava neuspesna, morda ta uporabnik ni prijavljen?", this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));
				}
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		} 
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				if (this.prejemnik.getText().length() == 0){
					this.addMessage("Server", "Vnesite vzdevek prejemnika", this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));
				} else {
					this.addTab(this.prejemnik.getText());
					try {
						if (this.jePrijavljen(this.vzdevekInput.getText())){
							Send.send(this.vzdevekInput.getText(), this.prejemnik.getText(), this.input.getText());
							this.addMessage(this.vzdevekInput.getText(), this.input.getText(), this.tabTextAreaSlovar.get(this.tabbedPane.getTitleAt(this.tabbedPane.getSelectedIndex())));
							this.input.setText("");
						}
					} catch (ClientProtocolException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
