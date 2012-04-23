package coruscant.jedi.temple;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import coruscant.imperial.palace.TheSenate;

public class R2D2 extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		Log.d("R2D2","Received intent");
		NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		State state = networkInfo.getState();

		Intent service = new Intent(ctx, TheSenate.class);
    	service.setAction("coruscant.imperial.palace.THE_SENATE");
		
    	boolean noConnectivity =
	            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
	    
    	if(!noConnectivity)
	        {
	        	ctx.stopService(service);
	        }else if(state == State.CONNECTED){
	        
	        	int type = networkInfo.getType(); 
	        	if(type == ConnectivityManager.TYPE_MOBILE || type == ConnectivityManager.TYPE_WIFI
	        			|| type == ConnectivityManager.TYPE_WIMAX || type == ConnectivityManager.TYPE_MOBILE_DUN
	        			|| type == ConnectivityManager.TYPE_MOBILE_HIPRI
	        			){
	        		ctx.startService(service);
	        	}
	        }
	}

}
