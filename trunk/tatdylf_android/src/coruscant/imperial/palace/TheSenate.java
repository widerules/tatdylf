package coruscant.imperial.palace;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TheSenate extends IntentService {
	private static TheForce theForce;
	private MessengerDroid droid;
	
	public TheSenate() {
		super("TheSenate");
	}
	
	public static TheForce useTheForce() {
		return theForce;
	}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d("TheSenate", "Starting MessengerDroid Thread");
		droid = new MessengerDroid(getResources());
		droid.start();
		return super.onStartCommand(intent,flags,startId);
    }

    @Override
	public void onCreate() {
		super.onCreate();
		theForce = new TheForce(getApplicationContext());
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
	}
}	