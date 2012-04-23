/**
 * 
 */
package deathstar.commandDeck;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.Result;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

@SuppressWarnings("serial")
public class ControlRoom extends JFrame implements ActionListener {
	private static HoloTransmitter holotransmitter;
	private static ControlRoom frame;
	private JButton lockButton;
	private JLabel currentVolume;
	private static RSAUtilImpl rsaDesktop;
	private static SecureChannel channelDesktop;
	private JTextField smsTextField;
	private JTextArea smsTextArea;
	private boolean numberSelected = true;

	public ControlRoom(String name) {
		super(name);
		setResizable(true);
		holotransmitter = new HoloTransmitter();
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
				if(numberSelected){
					outMsg.addParam("cmd", "textNumber");
					System.out.println("Adding as a number");
				} else {
					outMsg.addParam("cmd", "textName");
					System.out.println("Adding as a name");
				}
				
				outMsg.addParam("to", smsTextField.getText());
				outMsg.addParam("text", smsTextArea.getText());
				
				System.out.println("to: " + smsTextField.getText());
				System.out.println("text: " + smsTextArea.getText());
				
			} else {
				outMsg.addParam("cmd", command);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		Result res = null;
		Message msg = null;
		try {
			sendMsg(outMsg);
			msg = waitForResponse(61243);
			res = msg.getRes();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		if (command.equals("volUp")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Volume Increased!");
			} else {
				JOptionPane.showMessageDialog(frame, "Volume Increase Failed!");
			}
		} else if (command.equals("volDown")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Volume Decreased!");
			} else {
				JOptionPane.showMessageDialog(frame, "Volume Decrease Failed!");
			}
		} else if (command.equals("silAc")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Ringer Turned On!");
			} else {
				JOptionPane.showMessageDialog(frame, "Turning Ringer On Failed!");
			}
		} else if (command.equals("silDeac")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Ringer Turned Off!");
			} else {
				JOptionPane.showMessageDialog(frame, "Turning Ringer Off Failed!");
			}
		} else if (command.equals("vibAc")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Vibrate Mode Activated!");
			} else {
				JOptionPane.showMessageDialog(frame, "Activating Vibrate Mode Failed!");
			}
		} else if (command.equals("vibDeac")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Vibrate Mode Deactivated!");
			} else {
				JOptionPane.showMessageDialog(frame, "Deactivating Vibrate Mode Failed!");
			}
		} else if (command.equals("play")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Playing!");
			} else {
				JOptionPane.showMessageDialog(frame, "Play Failed!");
			}
		} else if (command.equals("text")) {
			switch (res) {
				case SUCCESS:
					JOptionPane.showMessageDialog(frame, "Text Sent!");
					break;
				case INVALID_INPUT:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nInvalid Input.");
					break;
				case MULTIPLE_CONTACTS:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nMultiple contacts with that name.");
					break;
				case MULTIPLE_NUMBERS:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nContact has multiple numbers; unable to determine which to text to.");
					break;
				case CONTACT_NOT_FOUND:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nContact not found.");
					break;
				case NUMBER_NOT_FOUND:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nNumber not found.");
					break;
				default:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nGeneral Error.");
			}
		} else if (command.equals("lock")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Phone is locked!");
				lockButton.setText("Unlock Phone");
				lockButton.setActionCommand("unlock");
			} else {
				JOptionPane.showMessageDialog(frame, "Phone is not locked!");
			}
		} else if (command.equals("unlock")) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Phone is unlocked!");
				lockButton.setText("Lock Phone");
				lockButton.setActionCommand("lock");
			} else {
				JOptionPane.showMessageDialog(frame, "Phone is not unlocked!");
			}
		} else if (command.equals("gps")) {
			if (res == Result.SUCCESS) {
				JOptionPane
						.showMessageDialog(frame,
								"I found your phone!\n(...but I'm not telling you where it is)");

				double lat = 0;
				double lon = 0;

				try {
					lat = (Double) msg.getParam("lat");
					lon = (Double) msg.getParam("long");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// code modified from:
				// http://stackoverflow.com/questions/8348063/clickable-links-in-joptionpane
				JEditorPane ep = new JEditorPane(
						"text/html",
						"<html><body>"
								+ "Ok, fine.  Go to this address: <a href=\"http://maps.google.com/?q="
								+ lat + "," + lon + "\">My Phone!</a>"
								+ "</body></html>");

				// handle link events
				ep.addHyperlinkListener(new HyperlinkListener() {
					@Override
					public void hyperlinkUpdate(HyperlinkEvent e) {
						if (e.getEventType().equals(
								HyperlinkEvent.EventType.ACTIVATED))
							try {
								Desktop.getDesktop().browse(e.getURL().toURI());
							} catch (IOException e1) {
								e1.printStackTrace();
							} catch (URISyntaxException e1) {
								e1.printStackTrace();
							}
					}
				});
				ep.setEditable(false);
				ep.setBackground(frame.getBackground());

				JOptionPane.showMessageDialog(frame, ep);
			} else {
				JOptionPane.showMessageDialog(frame, "Stuff failed.");
			}
		}
	}

	private void sendMsg(Message outMsg) throws Exception {

		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 61244);

		channelDesktop.serialize(outMsg, socket);

	}

	private Message waitForResponse(int port) throws Exception {

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
	}

}
