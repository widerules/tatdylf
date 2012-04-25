package coruscant.imperial.palace;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import comm.messaging.Result;
import comm.messaging.SecureChannel;

import coruscant.imperial.palace.comm.RSAUtilImpl;
import coruscant.imperial.palace.comm.SecureChannelAndroid;
import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class Dispatcher extends Thread {
	TheForce theForce;
	SimplMessageAndroid msg_in;

	public Dispatcher(SimplMessageAndroid msg) {
		theForce = TheSenate.useTheForce();
		msg_in = msg;
	}
	
	public void run() {
		SimplMessageAndroid msg_out = handleCommand(msg_in);
		sendResponse(msg_out);
	}
	
	private synchronized void sendResponse(SimplMessageAndroid msg) {
		try {
			Socket s = MessengerDroid.openOutboundSocket();
			SecureChannel channel = new SecureChannelAndroid(new RSAUtilImpl(theForce.context));
			channel.serialize(msg, s);
			Log.d("Dispatcher","Sending msg:" + msg.prettyPrint());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private SimplMessageAndroid handleCommand(SimplMessageAndroid msg) {
		
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
			e.printStackTrace();
		}
		
		return theForce.createResponse(msg, Result.ERROR);		
	}	
}
