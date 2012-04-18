/**
 * 
 */
package deathstar.commandDeck;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class ControlRoom extends JFrame implements ActionListener {
	private static HoloTransmitter holotransmitter;
	private static ControlRoom frame;
	private JButton lockButton;
	private JLabel currentVolume;
	
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
/*		try {
			holotransmitter.sendSMS();
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
		
		if(command.equals("volUp")){
			JOptionPane.showMessageDialog(frame,"Volume Increased!");
		} else if (command.equals("volDown")){
			JOptionPane.showMessageDialog(frame,"Volume Decreased!");
		} else if (command.equals("silAc")){
			JOptionPane.showMessageDialog(frame,"Silent Mode Activated!");
		} else if (command.equals("silDeac")){
			JOptionPane.showMessageDialog(frame,"Silent Mode Deactivated!");
		} else if (command.equals("vibAc")){
			JOptionPane.showMessageDialog(frame,"Vibrate Mode Activated!");
		} else if (command.equals("vibDeac")){
			JOptionPane.showMessageDialog(frame,"Vibrate Mode Deactivated!");
		} else if (command.equals("lock")){
			JOptionPane.showMessageDialog(frame,"Phone is locked!");
			lockButton.setText("Unlock Phone");
			lockButton.setActionCommand("unlock");
		} else if (command.equals("unlock")){
			JOptionPane.showMessageDialog(frame,"Phone is unlocked!");
			lockButton.setText("Lock Phone");
			lockButton.setActionCommand("lock");
		} else if (command.equals("gps")){
			JOptionPane.showMessageDialog(frame,"I found your phone!\n(...but I'm not telling you where it is)");
		}
	}

}
