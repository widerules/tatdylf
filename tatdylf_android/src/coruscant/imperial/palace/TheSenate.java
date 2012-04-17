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
    public int onStartCommand(Intent intent, int flags, int startId) {
		droid = new MessengerDroid();
		droid.start();
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		droid.stopThread();
		return super.onStartCommand(intent,flags,startId);
    }

    @Override
	protected void onHandleIntent(Intent intent) {
	}
}	
