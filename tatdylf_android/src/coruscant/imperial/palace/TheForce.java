package coruscant.imperial.palace;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;

import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class TheForce {
	Context context;
	private AudioManager audioManager;
	private KeyguardManager keyGuardManager;
	private LocationManager locationManager;
	private KeyguardLock lock;
	private SmsManager smsManager;
	private String contactNumber;
	//private RingtoneManager ringtoneManager;
	
	public TheForce(Context c) {
		this.context = c;
		audioManager = (AudioManager)this.context.getSystemService(Context.AUDIO_SERVICE);
		keyGuardManager = (KeyguardManager)this.context.getSystemService(Context.KEYGUARD_SERVICE);
		lock = keyGuardManager.newKeyguardLock("TreatyOfCoruscant");
		locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
		smsManager = SmsManager.getDefault();
	}
	
	public Result setPhoneVolume(int volume) {
		audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
		return Result.SUCCESS;
	}
	
	public Result incresePhoneVolume() {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		setPhoneVolume(++vol);
		return Result.SUCCESS;
	}

	public Result decreasePhoneVolume() {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		setPhoneVolume(--vol);
		Log.d("TheForce", "Decreased volume");
		return Result.SUCCESS;
	}
	
	public Result turnSilentOn() {
		audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
		return Result.SUCCESS;
	}

	public Result turnSilentOff() {
		audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
		return Result.SUCCESS;
	}

	public Result turnVibrationOn() {
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_NOTIFICATION, audioManager.VIBRATE_SETTING_ON);
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER, audioManager.VIBRATE_SETTING_ON);
		audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
		return Result.SUCCESS;
	}
	
	public Result turnVibrationOff() {
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_NOTIFICATION, audioManager.VIBRATE_SETTING_OFF);
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER, audioManager.VIBRATE_SETTING_OFF);
		audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
		return Result.SUCCESS;
	}

	public Result playAudio() {
		Uri r_uri = RingtoneManager.getValidRingtoneUri(context);
		Ringtone ringtone = RingtoneManager.getRingtone(context, r_uri);
		ringtone.play();
		return Result.SUCCESS;
	}

	public Result unlock() {
		lock.disableKeyguard();	
		return Result.SUCCESS;
	}
	
	public Result lock() {
		lock.reenableKeyguard();
		return Result.SUCCESS;
	}
	
	public SimplMessageAndroid locate(Message msg) {
		GeoLocation geo = new GeoLocation(msg, locationManager);
		return geo.generateLocationMessage();
	}

	public Result sendText(Message msg) {
		String phoneNumber = null;
		String name = null;
		String msgText = null;
		try {
			if ((Boolean)msg.getParam(Param.TXT_BY_NAME)) {
				name = (String)msg.getParam(Param.TXT_TO);
				Log.d("TheForce", "Looking up number based on name: " + name);
				Result r = getNumberByName(name);
				if (r != Result.SUCCESS) {
					Log.d("TheForce", "Failed to find number");
					return r;
				}
				else {
					phoneNumber = this.contactNumber;
					Log.d("TheForce", "Found number: " + phoneNumber);
				}
			} else {
				phoneNumber = (String)msg.getParam(Param.TXT_TO);
				Log.d("TheForce", "Using phone number: " + phoneNumber);
			}
			msgText = (String)msg.getParam(Param.TXT_BODY);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("TheForce", "Invalid Input.  Text failed");
			return Result.INVALID_INPUT;
		}
		Log.d("TheForce", "name: "+name+" number: "+phoneNumber+" text: "+msgText);
		smsManager.sendTextMessage(phoneNumber, null, msgText, null, null);
		Log.d("TheForce", "Text sent");
		
		return Result.SUCCESS;
	}

	private Result getNumberByName(String name) {
		int contactCount = 0;
		int numberCount = 0;
		Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, name);
		Cursor idCursor = context.getContentResolver().query(lkup, null, null, null, null);
		while (idCursor.moveToNext()) {
			contactCount++;
			String id = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
			name = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			String phoneQuery = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
			String[] phoneParams = new String[]{id, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
			Cursor phoneRes = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, phoneQuery, phoneParams, null);
			while(phoneRes.moveToNext()) {
				numberCount++;
				contactNumber = phoneRes.getString(phoneRes.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				Log.d("TheForce", "contact: " + name + " number: " + contactNumber);
			}
			phoneRes.close();
		}
		idCursor.close();
		if(contactCount == 1 && numberCount == 1) {
			return Result.SUCCESS;
		}
		else if(contactCount > 1) {
			return Result.MULTIPLE_CONTACTS;
		}
		else if (numberCount > 1) {
			return Result.MULTIPLE_NUMBERS;
		}
		else if (contactCount == 0) {
			return Result.CONTACT_NOT_FOUND;
		}
		else if (numberCount == 0) {
			return Result.NUMBER_NOT_FOUND;
		}
		return Result.ERROR;
	}
}


/***
package coruscant.imperial.palace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class TheForce extends IntentService {

       public TheForce() {
               super("TheForce");
               // TODO Auto-generated constructor stub
       }

       @Override
       protected void onHandleIntent(Intent arg0) {
               // TODO Auto-generated method stub

       }

       @Override
         public int onStartCommand(Intent intent, int flags, int startId) {
               Log.d("TheForce","May the Force be with you");
               //Toast.makeText(this, "HelloService starting", Toast.LENGTH_SHORT).show();
               audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

               try {
                       Log.d("TheForce", "Trying to create server socket");
                       ServerSocket server = new ServerSocket( 8081, 0, InetAddress.getByAddress(new byte[]{(byte)10, (byte)0, (byte)2, (byte)15}));
                       Log.d("TheForce", "Now waiting for connections on "+server.getInetAddress());
                       Socket socket = server.accept();
                       BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                       String cmd = reader.readLine();
                       Log.d("TheForce","Received line:" + cmd);
                       reader.close();
                       socket.close();
                       if("VIB".equals(cmd)){
                               turnVibrationOn();
                               Log.d("TheForce", "Turned Vibration on");
                       }
               } catch (IOException e) {
                       Log.e("TheForce", "Error in IO", e);
               }
           return super.onStartCommand(intent,flags,startId);

       }


       private AudioManager audioManager;
//    private RingtoneManager ringtoneManager;

       public boolean setPhoneVolume(int volume) {
               audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
               return true;
       }

       public boolean turnVibrationOn() {
               audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_NOTIFICATION, audioManager.VIBRATE_SETTING_ON);
               audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER, audioManager.VIBRATE_SETTING_ON);
               return false;
       }

       public boolean turnVibrationOff() {
               audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_NOTIFICATION, audioManager.VIBRATE_SETTING_OFF);
               audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER, audioManager.VIBRATE_SETTING_OFF);
               return false;
       }

       public boolean playAudio() {
               Uri r_uri = RingtoneManager.getValidRingtoneUri(getApplicationContext());
               Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), r_uri);
               ringtone.play();
               return false;
       }

       public boolean unlock() {
               return false;
       }

       public boolean lock() {
               return false;
       }


}
****/