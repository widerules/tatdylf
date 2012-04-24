package deathstar.commandDeck;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Result;

public class CommProtocol extends Thread {
	
	ControlRoom frame;
	Message msg;
	
	public CommProtocol(Message msg, ControlRoom frame){
		this.frame = frame;
		this.msg = msg;
	}
	
	public void run(){
		try {
			showResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void showResponse() throws Exception {
		
		System.out.println(msg.prettyPrint());
		
		Command command = msg.getCmd();
		
		Result res = msg.getRes();
		
		if(res == Result.PERMISSION_DENIED){
			JOptionPane.showMessageDialog(frame, "Permission denied.  Please check your settings on your phone.");
			return;
		}
		
		if (command == Command.INC_VOL) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Volume Increased!");
			} else {
				JOptionPane.showMessageDialog(frame, "Volume Increase Failed!");
			}
		} else if (command == Command.DEC_VOL) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Volume Decreased!");
			} else {
				JOptionPane.showMessageDialog(frame, "Volume Decrease Failed!");
			}
		} else if (command == Command.SILENT_OFF) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Ringer Turned On!");
			} else {
				JOptionPane.showMessageDialog(frame, "Turning Ringer On Failed!");
			}
		} else if (command == Command.SILENT_ON) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Ringer Turned Off!");
			} else {
				JOptionPane.showMessageDialog(frame, "Turning Ringer Off Failed!");
			}
		} else if (command == Command.VIB_ON) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Vibrate Mode Activated!");
			} else {
				JOptionPane.showMessageDialog(frame, "Activating Vibrate Mode Failed!");
			}
		} else if (command == Command.VIB_OFF) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Vibrate Mode Deactivated!");
			} else {
				JOptionPane.showMessageDialog(frame, "Deactivating Vibrate Mode Failed!");
			}
		} else if (command == Command.PLAY) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Playing!");
			} else {
				JOptionPane.showMessageDialog(frame, "Play Failed!");
			}
		} else if (command == Command.TXT) {
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
				case RINGTONE_NOT_AUDIBLE:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nRinger is not audible.\nPlease turn up the volume or turn on the ringer.");
					break;
				default:
					JOptionPane.showMessageDialog(frame, "Text Failed!\nGeneral Error.");
			}
		} else if (command == Command.LOCK) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Phone is locked!");
			} else {
				JOptionPane.showMessageDialog(frame, "Phone is not locked!");
			}
		} else if (command == Command.UNLOCK) {
			if (res == Result.SUCCESS) {
				JOptionPane.showMessageDialog(frame, "Phone is unlocked!");
			} else {
				JOptionPane.showMessageDialog(frame, "Phone is not unlocked!");
			}
		} else if (command == Command.LOCATE) {
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
	
}
