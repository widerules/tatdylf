package coruscant.imperial.palace;

import android.util.Log;

public class Dispatcher extends Thread {
//	TheForce theForce;
	String command;

	public Dispatcher(String cmd) {
//		theForce = TheSenate.useTheForce();
		this.command = cmd;
	}
	
	public void run() {
		Log.d("Dispatcher", "Handling command " + command);
	}

}
