package coruscant.imperial.palace;

import org.json.JSONException;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import comm.messaging.Message;
import comm.messaging.Param;

import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class GeoLocation extends Thread {
	Message msg_in;	
	Location loc;
	LocationManager locationMgr;
/*	LocationListener locListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) { }
		
		@Override
		public void onProviderEnabled(String provider) { }
		
		@Override
		public void onProviderDisabled(String provider) { }
		
		@Override
		public void onLocationChanged(Location location) {
			loc = location;
			this.notify();
		}
	};*/
	
	public GeoLocation(Message msg, LocationManager locMgr) {
		super();
		msg_in = msg;
		locationMgr = locMgr;
	}
	
	private SimplMessageAndroid createMessage(Object msgID, double latitude, double longitude) {
		SimplMessageAndroid msg = new SimplMessageAndroid();
		try {
			msg.addParam(Param.MSGID, msgID);
			msg.addParam(Param.RESULT, true);
			msg.addParam(Param.LATITUDE, latitude);
			msg.addParam(Param.LONGITUDE, longitude);
			return msg;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public SimplMessageAndroid generateLocationMessage() {
//		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, locListener);
		Log.d("GeoLocation", "Getting last known location");
		loc = locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.d("GeoLocation", "last known location: " + loc.toString());
		try {
			Log.d("GeoLocation", "Creating message with location");
			SimplMessageAndroid msg_out = createMessage(msg_in.getParam(Param.MSGID), loc.getLatitude(), loc.getLongitude());
			Log.d("GeoLocation", "returning message");
			return msg_out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
