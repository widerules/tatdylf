package coruscant.imperial.palace;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class TheForce extends Service {
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
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return null;
	}
}
