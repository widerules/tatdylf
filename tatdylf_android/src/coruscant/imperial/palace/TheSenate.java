package coruscant.imperial.palace;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TheSenate extends IntentService {
	private static TheForce theForce;
	private static String commIP;
	private static Integer commPort;
	private MessengerDroid droid;

	public TheSenate() {
		super("TheSenate");
	}
	
	public static TheForce useTheForce() {
		return theForce;
	}
	
	public static Integer getCommPort() {
		return commPort;
	}

	public static String getCommIP() {
		return commIP;
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d("TheSenate", "Starting MessengerDroid Thread");
    	MessengerDroid.startDroid(getApplicationContext());
    	return super.onStartCommand(intent,flags,startId);
    }

    @Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		theForce = new TheForce(getApplicationContext());
		commIP = getString(R.string.comm_relay_ip);
		commPort = Integer.parseInt(getString(R.string.comm_relay_port));
	}
	
	@Override
	protected void onHandleIntent(Intent arg0) {
	}
}	
