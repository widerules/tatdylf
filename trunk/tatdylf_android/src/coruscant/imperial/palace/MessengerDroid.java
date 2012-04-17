package coruscant.imperial.palace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class MessengerDroid extends Thread {
	Dispatcher dispatch;
	boolean keepListening;
	
	public MessengerDroid() {
	}

	public void run() {
		keepListening = true;
		ServerSocket server;
		try {
			Log.d("MessengerDroid", "Trying to create server socket");
			server = new ServerSocket( 8081, 0, InetAddress.getByAddress(new byte[]{(byte)10, (byte)0, (byte)2, (byte)15}));
			while(keepListening) {
				Log.d("MessengerDroid", "Now waiting for connections on "+server.getInetAddress());
				Socket socket = server.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String cmd = reader.readLine();
				Log.d("MessengerDroid","Received line:" + cmd);
				dispatch = new Dispatcher(cmd);
				dispatch.run();
				reader.close();
				socket.close();
			}
		} catch (IOException e) {
			Log.e("MessengerDroid", "Error in IO", e);
		}
	}
	
	public void stopThread() {
		keepListening = false;
	}
}
