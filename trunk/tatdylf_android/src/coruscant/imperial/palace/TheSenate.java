package coruscant.imperial.palace;

import android.app.IntentService;
import android.content.Intent;

public class TheSenate extends IntentService {
	private static TheForce theForce;
	private MessengerDroid droid;
	
	public TheSenate() {
		super("The Senate");
		theForce = new TheForce(getApplicationContext());
	}
	
	public static TheForce useTheForce() {
		return theForce;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		droid = new MessengerDroid();
		droid.start();
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		droid.stopThread();
	}
}	
