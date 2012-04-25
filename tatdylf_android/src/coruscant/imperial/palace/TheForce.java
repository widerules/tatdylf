package coruscant.imperial.palace;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
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

import comm.messaging.Param;
import comm.messaging.Result;

import coruscant.imperial.palace.comm.SimplMessageAndroid;
import coruscant.jedi.temple.C3PO;

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

		ComponentName c3po = new ComponentName(context.getPackageName(),
                C3PO.class.getName());
		audioManager.registerMediaButtonEventReceiver(c3po);
	}
	
	public SimplMessageAndroid createResponse(SimplMessageAndroid msg_in, Result result) {
		SimplMessageAndroid msg_out = new SimplMessageAndroid();
		try {
			msg_out.addParam(Param.MSGID, msg_in.getParam(Param.MSGID));
			msg_out.addParam(Param.RESULT, result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg_out;
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

	public SimplMessageAndroid incresePhoneVolume(SimplMessageAndroid msg_in) {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		int max_vol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		Result res = setPhoneVolume(++vol);
		int newVol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		SimplMessageAndroid msg_out = createResponse(msg_in, res);
		try {
			msg_out.addParam(Param.CURRENT_VOLUME, newVol);
			msg_out.addParam(Param.MAX_VOLUME, max_vol);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg_out;
	}

	public SimplMessageAndroid decreasePhoneVolume(SimplMessageAndroid msg_in) {
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		int max_vol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		Result res = setPhoneVolume(--vol);
		int newVol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		SimplMessageAndroid msg_out = createResponse(msg_in, res);
		try {
			msg_out.addParam(Param.CURRENT_VOLUME, newVol);
			msg_out.addParam(Param.MAX_VOLUME, max_vol);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg_out;
	}

	public SimplMessageAndroid turnSilentOn(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.silent_key), false)) {
			audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Silent permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid turnSilentOff(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.silent_key), false)) {
			audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Silent permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid turnVibrationOn(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.vibration_key), false)) {
			audioManager.setVibrateSetting(
					audioManager.VIBRATE_TYPE_NOTIFICATION,
					audioManager.VIBRATE_SETTING_ON);
			audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER,
					audioManager.VIBRATE_SETTING_ON);
			audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Vibration permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid turnVibrationOff(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.vibration_key), false)) {
			audioManager.setVibrateSetting(
					audioManager.VIBRATE_TYPE_NOTIFICATION,
					audioManager.VIBRATE_SETTING_OFF);
			audioManager.setVibrateSetting(audioManager.VIBRATE_TYPE_RINGER,
					audioManager.VIBRATE_SETTING_OFF);
			audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Vibration permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid playAudio(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.ringtone_key), false)) {
			if(audioManager.getRingerMode() == audioManager.RINGER_MODE_SILENT || audioManager.getStreamVolume(audioManager.STREAM_RING) == 0) {
				result = Result.RINGTONE_NOT_AUDIBLE;
			}
			Uri r_uri = RingtoneManager.getValidRingtoneUri(context);
			Ringtone ringtone = RingtoneManager.getRingtone(context, r_uri);
			ringtone.play();
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Play audio permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid unlock(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.lock_key), false)) {
			lock.disableKeyguard();
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Unlock permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid lock(SimplMessageAndroid msg_in) {
		Result result = null;
		if (preferences.getBoolean(context.getString(R.string.lock_key), false)) {
			lock.reenableKeyguard();
			result = Result.SUCCESS;
		} else {
			Log.d("TheForce", "Lock permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	public SimplMessageAndroid locate(SimplMessageAndroid msg_in) {
		if (preferences.getBoolean(context.getString(R.string.location_key), false)) {
			GeoLocation geo = new GeoLocation(msg_in, locationManager);
			return geo.generateLocationMessage();
		} else {
			Log.d("TheForce", "Locate permission denied");
			return createResponse(msg_in, Result.PERMISSION_DENIED);
		}
	}

	public SimplMessageAndroid sendText(SimplMessageAndroid msg_in) {
		Result result = null;
		SimplMessageAndroid msg_out = null;
		if (preferences.getBoolean(context.getString(R.string.text_key), false)) {
			String phoneNumber = null;
			String name = null;
			String msgText = null;
			try {
				if ((Boolean) msg_in.getParam(Param.TXT_BY_NAME)) {
					name = (String) msg_in.getParam(Param.TXT_TO);
					Log.d("TheForce", "Looking up number based on name: "
							+ name);
					msg_out = getNumberByName(name, msg_in);
					if(msg_out.getRes() == Result.SUCCESS) {
						phoneNumber = contactNumber;
					}
					else {
						return msg_out;
					}
				} else {
					phoneNumber = (String) msg_in.getParam(Param.TXT_TO);
					Log.d("TheForce", "Using phone number: " + phoneNumber);
				}
				msgText = (String) msg_in.getParam(Param.TXT_BODY);
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("TheForce", "Invalid Input.  Text failed");
				result = Result.INVALID_INPUT;
			}
			Log.d("TheForce", "name: " + name + " number: " + phoneNumber
					+ " text: " + msgText);
			smsManager.sendTextMessage(phoneNumber, null, msgText, null, null);
			Log.d("TheForce", "Text sent");

			result = Result.SUCCESS;
			if (msg_out != null) {
				return msg_out;
			}
		} else {
			Log.d("TheForce", "Sending text permission denied");
			result = Result.PERMISSION_DENIED;
		}
		return createResponse(msg_in, result);
	}

	private SimplMessageAndroid getNumberByName(String name, SimplMessageAndroid msg_in) {
		ArrayList<String> Contacts = new ArrayList<String>();
		ArrayList<String> Numbers = new ArrayList<String>();
		Uri lkup = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_FILTER_URI, name);
		Cursor idCursor = context.getContentResolver().query(lkup, null, null,
				null, null);
		while (idCursor.moveToNext()) {
			String id = idCursor.getString(idCursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			name = idCursor.getString(idCursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Contacts.add(name);

			String phoneQuery = ContactsContract.Data.CONTACT_ID + " = ? AND "
					+ ContactsContract.Data.MIMETYPE + " = ?";
			String[] phoneParams = new String[] { id,
					ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
			Cursor phoneRes = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI, null, phoneQuery,
					phoneParams, null);
			while (phoneRes.moveToNext()) {
				contactNumber = phoneRes
						.getString(phoneRes
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				Log.d("TheForce", "contact: " + name + " number: "
						+ contactNumber);
				Numbers.add(contactNumber);
			}
			phoneRes.close();
		}
		idCursor.close();
		SimplMessageAndroid msg_out = null;
		if (Contacts.size() == 1 && Numbers.size() == 1) {
			msg_out = createResponse(msg_in, Result.SUCCESS);
		} else if (Contacts.size() > 1) {
			msg_out = createResponse(msg_in, Result.MULTIPLE_CONTACTS);
			try {
				msg_out.addParam(Param.CONTACT_RESULTS, Contacts.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (Numbers.size() > 1) {
			msg_out = createResponse(msg_in, Result.MULTIPLE_NUMBERS);
			try {
				msg_out.addParam(Param.PHONE_NUMBERS, Numbers.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (Contacts.size() == 0) {
			msg_out = createResponse(msg_in, Result.CONTACT_NOT_FOUND);
		} else if (Numbers.size() == 0) {
			msg_out = createResponse(msg_in, Result.NUMBER_NOT_FOUND);
		}
		else {
			msg_out = createResponse(msg_in, Result.NUMBER_NOT_FOUND);
		}
		return msg_out;
	}
}