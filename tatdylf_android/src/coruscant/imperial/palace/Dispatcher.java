package coruscant.imperial.palace;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;

public class Dispatcher extends Thread {
	TheForce theForce;

	public Dispatcher() {
		theForce = TheSenate.useTheForce();
	}
	
	public Result handleCommand(Message msg) {
		
		try {
			switch(msg.getCmd()){
			
			case INC_VOL: return theForce.incresePhoneVolume();
			case DEC_VOL: return theForce.decreasePhoneVolume();
			case SILENT_ON: return theForce.turnSilentOn();
			case SILENT_OFF: return theForce.turnSilentOff();
			case VIB_ON: return theForce.turnVibrationOn();
			case VIB_OFF: return theForce.turnVibrationOff();
			case PLAY: return theForce.playAudio();
			case LOCK: return theForce.lock();
			case UNLOCK: return theForce.unlock();
			case TXT: return theForce.sendText(msg);
			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Result.ERROR;
	}
	
	public Message handleLocation(Message msg) {
		Message res = theForce.locate(msg);
		return res;
	}
	
}
