package coruscant.imperial.palace;

import comm.messaging.Message;

public class Dispatcher extends Thread {
	TheForce theForce;

	public Dispatcher() {
		theForce = TheSenate.useTheForce();
	}
	
	public boolean handleCommand(Message msg) {
		
		try {
			switch(msg.getCmd()){
			
			case INC_VOL: theForce.incresePhoneVolume(); break;
			case DEC_VOL: theForce.decreasePhoneVolume(); break;
						
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
