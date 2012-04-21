/**
 * 
 */
package deathstar.commandDeck;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import comm.messaging.Message;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

import security.RSAUtilImpl;
import sun.java2d.loops.ProcessPath.ProcessHandler;

@SuppressWarnings("serial")
public class ControlRoom extends JFrame implements ActionListener {
	private static HoloTransmitter holotransmitter;
	private static ControlRoom frame;
	private JButton lockButton;
	private JLabel currentVolume;
	private static RSAUtilImpl rsaUtilServer;
	private static RSAUtilImpl rsaUtilClient;
	private static SecureChannel channelServer;
	private static SecureChannel channelClient;
	
	public ControlRoom(String name){
		super(name);
		setResizable(true);
		holotransmitter = new HoloTransmitter();
	}

	public void addComponentsToPane(final Container pane) {
		final JPanel welcome = new JPanel();
		welcome.add(new JLabel("Welcome!  Use the buttons below to control your phone!"));
		
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
		
		lockButton = createButton("Lock Phone", "lock");
		final JPanel lock_find = new JPanel();
		lock_find.add(lockButton);
		lock_find.add(new JLabel(""));
		lock_find.add(createButton("Where is my phone?", "gps"));
		
		final JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(6,1));
		
		controls.add(welcome);
		controls.add(volume);
		controls.add(currentVol);
		controls.add(silent);
		controls.add(vibrate);
		controls.add(lock_find);
		
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
		
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/server/");
		rsaUtilClient = new RSAUtilImpl();
		rsaUtilClient.setPath("./res/client/");
		channelServer = new SecureChannel(rsaUtilServer);
		channelClient = new SecureChannel(rsaUtilClient);
		
/*		try {
			HoloTransmitter.sendSMS();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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
		Message outMsg = new SimplMessage();
		
		try {
			outMsg.addParam("cmd", command);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		boolean wasSuccessful = false;
		try {
			sendMsg(outMsg);
			wasSuccessful = waitForResponse(61243);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		if(command.equals("volUp")){
			if(wasSuccessful){
			JOptionPane.showMessageDialog(frame,"Volume Increased!");
			} else {
				JOptionPane.showMessageDialog(frame,"Volume Not Increased!");
			}
		} else if (command.equals("volDown")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Volume Decreased!");
			} else {
				JOptionPane.showMessageDialog(frame,"Volume Not Decreased!");
			}
		} else if (command.equals("silAc")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Silent Mode Activated!");
			} else {
				JOptionPane.showMessageDialog(frame,"Silent Mode Not Activated!");
			}
		} else if (command.equals("silDeac")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Silent Mode Deactivated!");
			} else {
				JOptionPane.showMessageDialog(frame,"Silent Mode Not Deactivated!");
			}
		} else if (command.equals("vibAc")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Vibrate Mode Activated!");
			} else {
				JOptionPane.showMessageDialog(frame,"Vibrate Mode Not Activated!");
			}
		} else if (command.equals("vibDeac")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Vibrate Mode Deactivated!");
			} else {
				JOptionPane.showMessageDialog(frame,"Vibrate Mode Not Deactivated!");
			}
		} else if (command.equals("lock")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Phone is locked!");
			lockButton.setText("Unlock Phone");
			lockButton.setActionCommand("unlock");
			} else {
				JOptionPane.showMessageDialog(frame,"Phone is not locked!");
			}
		} else if (command.equals("unlock")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"Phone is unlocked!");
			lockButton.setText("Lock Phone");
			lockButton.setActionCommand("lock");
			} else {
				JOptionPane.showMessageDialog(frame,"Phone is not unlocked!");
			}
		} else if (command.equals("gps")){
			if(wasSuccessful){
				JOptionPane.showMessageDialog(frame,"I found your phone!\n(...but I'm not telling you where it is)");
			
			// code modified from: http://stackoverflow.com/questions/8348063/clickable-links-in-joptionpane
			JEditorPane ep = new JEditorPane("text/html", "<html><body>" + "Ok, fine.  Go to this address: <a href=\"http://maps.google.com/?q=40.809554,-73.960690\">My Phone!</a>" + "</body></html>");

		    // handle link events
		    ep.addHyperlinkListener(new HyperlinkListener()
		    {
		        @Override
		        public void hyperlinkUpdate(HyperlinkEvent e)
		        {
		            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} // roll your own link launcher or use Desktop if J6+
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
		
		
		channelClient.serialize(outMsg, socket);
		
	}

	private boolean waitForResponse(int port) throws Exception {

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

		RSAUtilImpl rsaUtil = new RSAUtilImpl();
		rsaUtil.setPath("./res/server/");
		SecureChannel channel = new SecureChannel(rsaUtil);
		
		Message inMsg = channel.deSerialize(clientSocket);

		serverSocket.close();
		
		return (Boolean) inMsg.getParam("success");
	}

}
