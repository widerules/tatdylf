package coruscant.imperial.palace;

import android.content.Context;
import android.content.Intent;
import coruscant.imperial.palace.comm.RSAUtilImpl;
import coruscant.jedi.temple.initialization.Init;

public class LocateSatellites extends Thread {
	private boolean runInit;
	private Context context;
	private TreatyOfCoruscant treaty;
	
	public LocateSatellites(Context c, boolean init, TreatyOfCoruscant t) {
		runInit = init;
		context = c;
		treaty = t;
	}

	public void run() {
		if(runInit) {
			try {
				Init.init(context);
				MessengerDroid.updateIP(new RSAUtilImpl(context), context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	Intent intent = new Intent(treaty, TheSenate.class);
    	intent.setAction("coruscant.imperial.palace.THE_SENATE");
    	context.startService(intent);
	}
}
