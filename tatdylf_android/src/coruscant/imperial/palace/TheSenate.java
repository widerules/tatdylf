package coruscant.imperial.palace;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TheSenate extends IntentService {
	private static TheForce theForce;
	private static String commIP;
	private static Integer toCommPort;
	private static Integer fromCommPort;

	public TheSenate() {
		super("TheSenate");
	}
	
	public static TheForce useTheForce() {
		return theForce;
	}
	
	public static Integer getToCommPort() {
		return toCommPort;
	}

	public static Integer getFromCommPort() {
		return fromCommPort;
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
		toCommPort = Integer.parseInt(getString(R.string.comm_relay_outbound));
		fromCommPort = Integer.parseInt(getString(R.string.comm_relay_inbound));
	}
	
	@Override
	protected void onHandleIntent(Intent arg0) {
	}
}	
