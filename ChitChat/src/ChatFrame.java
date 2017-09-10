import java.awt.Component;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private JTabbedPane tabbedPane;
	private HashMap<String, JTextArea> tabSlovar;
	
	public ChatFrame() throws ClientProtocolException, URISyntaxException, IOException {
		super();
		setTitle("ChitChat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		
		this.vzdevek = new JPanel();
		JLabel napis = new JLabel("Vzdevek");
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
		tabbedPane.addTab("Default", scrollPane);
		pane.add(tabbedPane, outputConstraint);
		this.tabSlovar = new HashMap<String, JTextArea>();
		tabSlovar.put("Default", this.output);

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
		
	}

	/**
	 * @param person - the person sending the message
	 * @param message - the message content
	 */
	public void addMessage(String person, String message) {
		//Doda besedilo iz "input" v trenutno izbrani tab
		JTextArea output = this.tabSlovar.get(this.tabbedPane.getTitleAt(
				this.tabbedPane.getSelectedIndex()));
		String chat = output.getText();
		output.setText(chat + person + ": " + message + "\n");
	}
	
	public void refreshOnline(String[] seznamOnline){
		String text = "";
		for (String oseba : seznamOnline){
			text = text + oseba + "\n";
		}
		this.onlineOutput.setText(text);
	}
	
	private void addTab(String prejemnik){
		JTextArea textArea = new JTextArea(20, 40);
		textArea.setEditable(false);
		this.tabbedPane.add(prejemnik, textArea);
		this.tabSlovar.put(prejemnik, textArea);
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
	
	public void osvezi(){
		String[] seznamOnline;
		try {
			seznamOnline = extractUsername(Get.get());
			refreshOnline(seznamOnline);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Prijava")){
			try {
				this.addMessage("Server", Post.post(vzdevekInput.getText()));
			} catch (URISyntaxException | IOException e1) {
				e1.printStackTrace();
			}
		} else if(action.equals("Odjava")){
			try {
				this.addMessage("Server", Delete.delete(vzdevekInput.getText()));
				this.vzdevekInput.setEditable(true);
			} catch (URISyntaxException | IOException e1) {
				e1.printStackTrace();
			}
		} 
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				this.addMessage(this.vzdevekInput.getText(), this.input.getText());
				this.input.setText("");
				this.addTab(this.prejemnik.getText());
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
