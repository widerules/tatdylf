package coruscant.imperial.palace;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TheSenate extends IntentService {
//	private static TheForce theForce;
	private MessengerDroid droid;
	
	public TheSenate() {
		super("TheSenate");
//		theForce = new TheForce(getApplicationContext());
	}
	
/*	public static TheForce useTheForce() {
		return theForce;
	}*/
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d("TheSenate", "Starting MessengerDroid Thread");
		droid = new MessengerDroid();
		droid.start();
		Log.d("TheSenate", "Waiting on MessengerDroidThread");
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("TheSenate", "Stopping MessegerDroidThread");
		droid.stopThread();
		return super.onStartCommand(intent,flags,startId);
    }

    @Override
	protected void onHandleIntent(Intent arg0) {
	}
}	
