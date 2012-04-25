package coruscant.jedi.temple;

import java.net.Socket;

import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import comm.messaging.Param;
import comm.messaging.SecureChannel;

import coruscant.imperial.palace.R;
import coruscant.imperial.palace.comm.RSAUtilImpl;
import coruscant.imperial.palace.comm.SecureChannelAndroid;
import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class C3PO extends BroadcastReceiver {
	private AudioManager audioManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("C3PO", "Received media button change!");
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		int max_vol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		SimplMessageAndroid msg_out = new SimplMessageAndroid();
		try {
			msg_out.addParam(Param.MSGID, -1);
			msg_out.addParam(Param.CURRENT_VOLUME, vol);
			msg_out.addParam(Param.MAX_VOLUME, max_vol);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			Socket s = new Socket(context.getString(R.string.comm_relay_ip), Integer.parseInt(context.getString(R.string.comm_relay_outbound)));
			SecureChannel channel = new SecureChannelAndroid(new RSAUtilImpl(context));
			channel.serialize(msg_out, s);
			Log.d("C3PO", "Sent notification of change");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
