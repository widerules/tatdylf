package coruscant.imperial.palace;

import comm.messaging.Message;

import coruscant.imperial.palace.comm.SimplMessageAndroid;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class TheForce {
	Context context;
	private AudioManager audioManager;
	private KeyguardManager keyGuardManager;
	private LocationManager locationManager;
	private KeyguardLock lock;
	private SmsManager smsManager;
	//private RingtoneManager ringtoneManager;
	
	public TheForce(Context c) {
		this.context = c;
		audioManager = (AudioManager)this.context.getSystemService(Context.AUDIO_SERVICE);
		keyGuardManager = (KeyguardManager)this.context.getSystemService(Context.KEYGUARD_SERVICE);
		lock = keyGuardManager.newKeyguardLock("TreatyOfCoruscant");
		locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
		smsManager = SmsManager.getDefault();
	}
	
	public boolean setPhoneVolume(int volume) {
		audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
		return true;
	}
	
	public boolean incresePhoneVolume() {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		setPhoneVolume(++vol);
		return true;
	}

	public boolean decreasePhoneVolume() {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		setPhoneVolume(--vol);
		Log.d("TheForce", "Decreased volume");
		return true;
	}
	
	public boolean turnSilentOn() {
		audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
		return true;
	}

	public boolean turnSilentOff() {
		audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
		return true;
	}

	public boolean turnVibrationOn() {
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_NOTIFICATION, audioManager.VIBRATE_SETTING_ON);
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER, audioManager.VIBRATE_SETTING_ON);
		audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
		return false;
	}
	
	public boolean turnVibrationOff() {
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_NOTIFICATION, audioManager.VIBRATE_SETTING_OFF);
		audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER, audioManager.VIBRATE_SETTING_OFF);
		audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
		return false;
	}

	public boolean playAudio() {
		Uri r_uri = RingtoneManager.getValidRingtoneUri(context);
		Ringtone ringtone = RingtoneManager.getRingtone(context, r_uri);
		ringtone.play();
		return false;
	}

	public boolean unlock() {
		lock.disableKeyguard();	
		return false;
	}
	
	public boolean lock() {
		lock.reenableKeyguard();
		return false;
	}
	
	public SimplMessageAndroid locate(Message msg) {
		GeoLocation geo = new GeoLocation(msg, locationManager);
		return geo.generateLocationMessage();
	}

	public boolean sendText(String recipient, String textBody) {
		smsManager.sendTextMessage(recipient, null, textBody, null, null);
		Log.d("TheForce", "Text sent");
		return true;
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