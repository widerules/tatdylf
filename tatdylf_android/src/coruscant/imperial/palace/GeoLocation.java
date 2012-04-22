package coruscant.imperial.palace;

import org.json.JSONException;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import comm.messaging.Message;
import comm.messaging.Param;

import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class GeoLocation extends Thread {
	Message msg_in;	
	boolean locationFound;
	Location loc;
	LocationManager locationMgr;
	LocationListener locListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("GeoLocation", "onStatusChanged has been called");
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			Log.d("GeoLocation", "onProviderEnabled has been called");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			Log.d("GeoLocation", "onProviderDisabled has been called");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			Log.d("GeoLocation", "onLocationChanged has been called");
			loc = location;
			locationFound = true;
		}
	};
	
	public GeoLocation(Message msg, LocationManager locMgr) {
		super();
		msg_in = msg;
		locationMgr = locMgr;
		locationFound = false;
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
		Log.d("GeoLocation", "Getting location");
		Location loc1 = locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location loc2 = locationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc1 == null && loc2 == null) {
			listenForLocation();
		}
		else {
			if (loc1 != null) {
				loc = loc1;
			}
			else if (loc2 != null) {
				loc = loc2;
			}
		}
		Log.d("GeoLocation", "location: " + loc.toString());
		locationMgr.removeUpdates(locListener);
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

	private void listenForLocation() {
		Looper.prepare();
		Criteria crit = new Criteria();
/*		crit.setAccuracy(Criteria.ACCURACY_FINE);
		crit.setAltitudeRequired(false);
		crit.setBearingRequired(false);
		crit.setCostAllowed(false);
		crit.setPowerRequirement(Criteria.POWER_MEDIUM);
		crit.setSpeedRequired(false);
		String provider = locationMgr.getBestProvider(crit, true);*/
		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, locListener);
		locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 0, locListener);
		while (!locationFound) {
			Log.d("GeoLocation", "LocationFound: " + locationFound);
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
