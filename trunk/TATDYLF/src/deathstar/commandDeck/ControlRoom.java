/**
 * 
 */
package deathstar.commandDeck;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ControlRoom extends JFrame implements ActionListener {

	private static ControlRoom frame;
	
	public ControlRoom(String name){
		super(name);
		setResizable(true);
	}

	public void addComponentsToPane(final Container pane) {
		JButton volUp = new JButton("+");
		Label vol = new Label("Volume");
		JButton volDown = new JButton("-");
		final JPanel volume = new JPanel();
		
		JButton silActivate = new JButton("Activate");
		Label sil = new Label("Silent Mode");
		JButton silDeactivate = new JButton("Deactivate");
		final JPanel silent = new JPanel();
		
		JButton vibActivate = new JButton("Activate");
		Label vib = new Label("Vibrate Mode");
		JButton vibDeactivate = new JButton("Deactivate");
		final JPanel vibrate = new JPanel();
		
		JButton lockButton = new JButton("Lock Phone");
		final JPanel lock = new JPanel();
		
		final JPanel controls = new JPanel();
		
		volume.setLayout(new GridLayout(1,3));
		silent.setLayout(new GridLayout(1,3));
		vibrate.setLayout(new GridLayout(1,3));
		lock.setLayout(new GridLayout(1,1));
		controls.setLayout(new GridLayout(4,1));

		volume.add(volUp);
		volume.add(vol);
		volume.add(volDown);
		
		silent.add(silActivate);
		silent.add(sil);
		silent.add(silDeactivate);
		
		vibrate.add(vibActivate);
		vibrate.add(vib);
		vibrate.add(vibDeactivate);
		
		lock.add(lockButton);
		
		controls.add(volume);
		controls.add(silent);
		controls.add(vibrate);
		controls.add(lock);
		
		pane.add(controls);
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

		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

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
		// TODO Auto-generated method stub

	}

}
