package coruscant.imperial.palace;

public class Dispatcher extends Thread {
	TheForce theForce;
	String command;

	public Dispatcher(String cmd) {
		theForce = TheSenate.useTheForce();
		this.command = cmd;
	}
	
	public void run() {
		
	}

}
