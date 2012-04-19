package coruscant.imperial.palace;

import android.util.Log;

public class Dispatcher extends Thread {
	TheForce theForce;

	public Dispatcher() {
		theForce = TheSenate.useTheForce();
	}
	
	public boolean handleCommand(String command) {
		if (command.equals("Volume +")) {
			return theForce.incresePhoneVolume();
		}
		else if (command.equals("Volume -")) {
			return theForce.decreasePhoneVolume();
		}
		return false;
	}
	
}
