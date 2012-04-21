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
			
			case INC_VOL: return theForce.incresePhoneVolume();
			case DEC_VOL: return theForce.decreasePhoneVolume();
			case SILENT_ON: return theForce.turnSilentOn();
			case SILENT_OFF: theForce.turnSilentOff();
			case VIB_ON: return false; //return theForce.turnVibrationOn();
			case VIB_OFF: return theForce.turnVibrationOff();
			case PLAY: return theForce.playAudio();
			case LOCK: return theForce.lock();
			case UNLOCK: return theForce.unlock();
			case LOCATE: return false;
			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
