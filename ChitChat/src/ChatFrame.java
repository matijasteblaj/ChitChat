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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	
	private JTextArea output;
	private JTextField input;
	private JPanel vzdevek;
	private JTextField vzdevekInput;
	private JPanel online;
	private JTextArea onlineOutput;
	private JButton prijava;
	private JButton odjava;

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
		odjava.setEnabled(false);
		vzdevek.add(odjava);
		GridBagConstraints vzdevekConstraint = new GridBagConstraints();
		vzdevekConstraint.gridx = 0;
		vzdevekConstraint.gridy = 0;
		pane.add(vzdevek, vzdevekConstraint);
		vzdevek.addKeyListener(this);
		
		this.online = new JPanel();
		JLabel napisOnline = new JLabel("Online:");
		this.onlineOutput = new JTextArea(20,10);
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
		pane.add(scrollPane, outputConstraint);
		
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.fill = GridBagConstraints.HORIZONTAL;
		inputConstraint.weightx = 1.0;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		
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
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Prijava")){
			try {
				this.addMessage("Server", Post.post(vzdevekInput.getText()));
				this.vzdevekInput.setEditable(false);
				this.prijava.setEnabled(false);
				this.odjava.setEnabled(true);
			} catch (URISyntaxException | IOException e1) {
				e1.printStackTrace();
			}
		} else if(action.equals("Odjava")){
			try {
				this.addMessage("Server", Delete.delete(vzdevekInput.getText()));
				this.vzdevekInput.setEditable(true);
				this.prijava.setEnabled(true);
				this.odjava.setEnabled(false);
			} catch (URISyntaxException | IOException e1) {
				// TODO Auto-generated catch block
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
