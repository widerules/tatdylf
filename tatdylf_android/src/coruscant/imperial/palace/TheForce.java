package coruscant.imperial.palace;

import org.json.JSONException;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
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
	private SharedPreferences preferences;

	// private RingtoneManager ringtoneManager;

	public TheForce(Context c) {
		this.context = c;
		audioManager = (AudioManager) this.context
				.getSystemService(Context.AUDIO_SERVICE);
		keyGuardManager = (KeyguardManager) this.context
				.getSystemService(Context.KEYGUARD_SERVICE);
		lock = keyGuardManager.newKeyguardLock("TreatyOfCoruscant");
		locationManager = (LocationManager) this.context
				.getSystemService(Context.LOCATION_SERVICE);
		smsManager = SmsManager.getDefault();
		PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public Result setPhoneVolume(int volume) {
		if (preferences.getBoolean(context.getString(R.string.volume_key), false)) {
			audioManager.setStreamVolume(AudioManager.STREAM_RING, volume,
					AudioManager.FLAG_SHOW_UI);
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Volume permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result incresePhoneVolume() {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		return setPhoneVolume(++vol);
	}

	public Result decreasePhoneVolume() {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		return setPhoneVolume(--vol);
	}

	public Result turnSilentOn() {
		if (preferences.getBoolean(context.getString(R.string.silent_key), false)) {
			audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Silent permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result turnSilentOff() {
		if (preferences.getBoolean(context.getString(R.string.silent_key), false)) {
			audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Silent permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result turnVibrationOn() {
		if (preferences.getBoolean(context.getString(R.string.vibration_key), false)) {
			audioManager.setVibrateSetting(
					audioManager.VIBRATE_TYPE_NOTIFICATION,
					audioManager.VIBRATE_SETTING_ON);
			audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER,
					audioManager.VIBRATE_SETTING_ON);
			audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Vibration permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result turnVibrationOff() {
		if (preferences.getBoolean(context.getString(R.string.vibration_key), false)) {
			audioManager.setVibrateSetting(
					audioManager.VIBRATE_TYPE_NOTIFICATION,
					audioManager.VIBRATE_SETTING_OFF);
			audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER,
					audioManager.VIBRATE_SETTING_OFF);
			audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Vibration permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result playAudio() {
		if (preferences.getBoolean(context.getString(R.string.ringtone_key), false)) {
			if(audioManager.getRingerMode() == audioManager.RINGER_MODE_SILENT || audioManager.getStreamVolume(audioManager.STREAM_RING) == 0) {
				return Result.RINGTONE_NOT_AUDIBLE;
			}
			Uri r_uri = RingtoneManager.getValidRingtoneUri(context);
			Ringtone ringtone = RingtoneManager.getRingtone(context, r_uri);
			ringtone.play();
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Play audio permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result unlock() {
		if (preferences.getBoolean(context.getString(R.string.lock_key), false)) {
			lock.disableKeyguard();
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Unlock permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public Result lock() {
		if (preferences.getBoolean(context.getString(R.string.lock_key), false)) {
			lock.reenableKeyguard();
			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Lock permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	public SimplMessageAndroid locate(Message msg) {
		if (preferences.getBoolean(context.getString(R.string.location_key), false)) {
			GeoLocation geo = new GeoLocation(msg, locationManager);
			return geo.generateLocationMessage();
		} else {
			Log.d("TheForce", "Locate permission denied");
			try {
				SimplMessageAndroid msg_out = new SimplMessageAndroid();
				msg_out.addParam(Param.MSGID, msg.getParam(Param.MSGID));
				msg_out.addParam(Param.RESULT, Result.PERMISSION_DENIED);
				return msg_out;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public Result sendText(Message msg) {
		if (preferences.getBoolean(context.getString(R.string.text_key), false)) {
			String phoneNumber = null;
			String name = null;
			String msgText = null;
			try {
				if ((Boolean) msg.getParam(Param.TXT_BY_NAME)) {
					name = (String) msg.getParam(Param.TXT_TO);
					Log.d("TheForce", "Looking up number based on name: "
							+ name);
					Result r = getNumberByName(name);
					if (r != Result.SUCCESS) {
						Log.d("TheForce", "Failed to find number");
						return r;
					} else {
						phoneNumber = this.contactNumber;
						Log.d("TheForce", "Found number: " + phoneNumber);
					}
				} else {
					phoneNumber = (String) msg.getParam(Param.TXT_TO);
					Log.d("TheForce", "Using phone number: " + phoneNumber);
				}
				msgText = (String) msg.getParam(Param.TXT_BODY);
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("TheForce", "Invalid Input.  Text failed");
				return Result.INVALID_INPUT;
			}
			Log.d("TheForce", "name: " + name + " number: " + phoneNumber
					+ " text: " + msgText);
			smsManager.sendTextMessage(phoneNumber, null, msgText, null, null);
			Log.d("TheForce", "Text sent");

			return Result.SUCCESS;
		} else {
			Log.d("TheForce", "Sending text permission denied");
			return Result.PERMISSION_DENIED;
		}
	}

	private Result getNumberByName(String name) {
		int contactCount = 0;
		int numberCount = 0;
		Uri lkup = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_FILTER_URI, name);
		Cursor idCursor = context.getContentResolver().query(lkup, null, null,
				null, null);
		while (idCursor.moveToNext()) {
			contactCount++;
			String id = idCursor.getString(idCursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			name = idCursor.getString(idCursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			String phoneQuery = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";
			String[] phoneParams = new String[] { id,
					ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
			Cursor phoneRes = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI, null, phoneQuery,
					phoneParams, null);
			while (phoneRes.moveToNext()) {
				numberCount++;
				contactNumber = phoneRes
						.getString(phoneRes
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				Log.d("TheForce", "contact: " + name + " number: "
						+ contactNumber);
			}
			phoneRes.close();
		}
		idCursor.close();
		if (contactCount == 1 && numberCount == 1) {
			return Result.SUCCESS;
		} else if (contactCount > 1) {
			return Result.MULTIPLE_CONTACTS;
		} else if (numberCount > 1) {
			return Result.MULTIPLE_NUMBERS;
		} else if (contactCount == 0) {
			return Result.CONTACT_NOT_FOUND;
		} else if (numberCount == 0) {
			return Result.NUMBER_NOT_FOUND;
		}
		return Result.ERROR;
	}
}