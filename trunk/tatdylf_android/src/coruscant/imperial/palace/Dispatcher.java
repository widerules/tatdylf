package coruscant.imperial.palace;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;
import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class Dispatcher extends Thread {
	TheForce theForce;

	public Dispatcher() {
		theForce = TheSenate.useTheForce();
	}
	
	public SimplMessageAndroid handleCommand(SimplMessageAndroid msg) {
		
		try {
			switch(msg.getCmd()){
			
			case INC_VOL: return theForce.incresePhoneVolume(msg);
			case DEC_VOL: return theForce.decreasePhoneVolume(msg);
			case SILENT_ON: return theForce.turnSilentOn(msg);
			case SILENT_OFF: return theForce.turnSilentOff(msg);
			case VIB_ON: return theForce.turnVibrationOn(msg);
			case VIB_OFF: return theForce.turnVibrationOff(msg);
			case PLAY: return theForce.playAudio(msg);
			case LOCK: return theForce.lock(msg);
			case UNLOCK: return theForce.unlock(msg);
			case TXT: return theForce.sendText(msg);
			case LOCATE: return theForce.locate(msg);
			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return theForce.createResponse(msg, Result.ERROR);
	}	
}
