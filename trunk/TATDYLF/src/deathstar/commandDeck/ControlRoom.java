/**
 * 
 */
package deathstar.commandDeck;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

@SuppressWarnings("serial")
public class ControlRoom extends JFrame implements ActionListener {
//	private static HoloTransmitter holotransmitter;
	private static ControlRoom frame;
	private JButton lockButton;
	private JLabel currentVolume;
	private static RSAUtilImpl rsaDesktop;
	private static SecureChannel channelDesktop;
	private JTextField smsTextField;
	private JTextArea smsTextArea;
	private boolean numberSelected = true;
	private static int port = 61243;

	public ControlRoom(String name) {
		super(name);
		setResizable(true);
//		holotransmitter = new HoloTransmitter();
	}

	public void addComponentsToPane(final Container pane) {
		final JPanel welcome = new JPanel();
		welcome.add(new JLabel(
				"Welcome!  Use the buttons below to control your phone!"));

		final JPanel volume = new JPanel();
		volume.add(new JLabel("Adjust phone volume: "));
		volume.add(createButton("+", "volUp"));
		volume.add(createButton("-", "volDown"));

		currentVolume = new JLabel("unknown");
		final JPanel currentVol = new JPanel();
		currentVol.add(new JLabel("Your phone's current volume is: "));
		currentVol.add(currentVolume);

		final JPanel silent = new JPanel();
		silent.add(new JLabel("Turn phone ringer: "));
		silent.add(createButton("On", "silAc"));
		silent.add(createButton("Off", "silDeac"));

		final JPanel vibrate = new JPanel();
		vibrate.add(new JLabel("Turn vibrator: "));
		vibrate.add(createButton("On", "vibAc"));
		vibrate.add(createButton("Off", "vibDeac"));

		final JPanel sms = new JPanel();
		sms.add(new JLabel("Send text to:"));
		smsTextField = new JTextField(8);
		smsTextField.setMaximumSize(smsTextField.getSize());
		sms.add(smsTextField);
		sms.add(new JLabel("Which is a:"));
		JRadioButton radioNumber = new JRadioButton("Phone Number");
		radioNumber.setActionCommand("radio_number");
		radioNumber.addActionListener(this);
		radioNumber.setSelected(true);
		JRadioButton radioName = new JRadioButton("Contact's Name");
		radioName.setActionCommand("radio_name");
		radioName.addActionListener(this);
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radioNumber);
		radioGroup.add(radioName);
		sms.add(radioNumber);
		sms.add(radioName);

		final JPanel smsText = new JPanel();
		smsText.add(new JLabel("Message:"));
		smsTextArea = new JTextArea(2, 40);
		smsTextArea.setLineWrap(true);
		smsTextArea.setWrapStyleWord(true);
		smsTextArea.setMaximumSize(smsTextArea.getSize());
		JScrollPane jsp = new JScrollPane(smsTextArea);
		smsText.add(jsp);
		smsText.add(createButton("Send Text!", "text"));

		lockButton = createButton("Lock Phone", "lock");
		final JPanel lock_find = new JPanel();
		lock_find.add(lockButton);
		lock_find.add(new JLabel(""));
		lock_find.add(createButton("Unlock Phone", "unlock"));
		lock_find.add(new JLabel(""));
		lock_find.add(createButton("Where is my phone?", "gps"));
		
		final JPanel playSound = new JPanel();
		playSound.add(createButton("Make some noise!", "play"));

		final JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(9, 1));

		controls.add(welcome);
		controls.add(volume);
		controls.add(currentVol);
		controls.add(silent);
		controls.add(vibrate);
		controls.add(sms);
		controls.add(smsText);
		controls.add(lock_find);
		controls.add(playSound);

		pane.add(controls);
	}

	private JButton createButton(String text, String actionCommand) {
		JButton button = new JButton(text);
		button.setActionCommand(actionCommand);
		button.addActionListener(this);
		return button;
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		frame = new ControlRoom("May the Sith be with you.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		frame.addComponentsToPane(frame.getContentPane());
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello, Android!");

		rsaDesktop = new RSAUtilImpl();
		rsaDesktop.setPath("./res/desktop/");
		channelDesktop = new SecureChannel(rsaDesktop);
		
		CommTower ct = new CommTower(frame, port);
		ct.start();

		/* Use an appropriate Look and Feel */
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();

		if (command.equals("radio_number")) {
			numberSelected = true;
			return;
		} else if (command.equals("radio_name")) {
			numberSelected = false;
			return;
		}

		Message outMsg = new SimplMessage();

		try {
			if (command.equals("text")) {
				
				String to = smsTextField.getText();
				String text = smsTextArea.getText();
				
				if (numberSelected) {
					to = to.replaceAll("\\)|\\(|-|\\.|\\s", "");
					if (to.matches(".*\\D.*")){
						JOptionPane.showMessageDialog(frame, "Invalid number");
						JOptionPane.showMessageDialog(frame, to);
						return;
					}
					outMsg.addParam("cmd", "textNumber");
					System.out.println("Adding as a number");
				} else {
					outMsg.addParam("cmd", "textName");
					System.out.println("Adding as a name");
				}
				
				outMsg.addParam("to", to);
				outMsg.addParam("text", text);
				
				System.out.println("to: " + smsTextField.getText());
				System.out.println("text: " + smsTextArea.getText());
				
			} else {
				outMsg.addParam("cmd", command);
			}
			sendMsg(outMsg);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	private void sendMsg(Message outMsg) throws Exception {

		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 61244);

		channelDesktop.serialize(outMsg, socket);

	}

	/*private Message waitForResponse(int port) throws Exception {

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(1);
		}

		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
			System.out.println("Connected");
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}

		Message inMsg = channelDesktop.deSerialize(clientSocket);
		
		System.out.println(inMsg.prettyPrint());
		
		serverSocket.close();

		return inMsg;
	}*/

}
