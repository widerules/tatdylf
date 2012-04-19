package coruscant.imperial.palace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class MessengerDroid extends Thread {
	Dispatcher dispatch;
	boolean keepListening;
	
	public MessengerDroid() {
		dispatch = new Dispatcher();
	}

	public void run() {
		/* Test outbound connection */
/*		try {
			Log.d("MessengerDroid", "Sleeping");
			Thread.sleep(10000);
			Socket socket = new Socket("128.59.22.39", 61244);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.print("I'm alive!");
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("MessengerDroid", "Error!");
		}*/
		/* Test inbound connection */
		keepListening = true;
		ServerSocket server;
		try {
			Log.d("MessengerDroid", "Trying to create server socket");
			server = new ServerSocket(61246);
			while(keepListening) {
				Log.d("MessengerDroid", "Now waiting for connections on " + server.getLocalPort());
				Socket socket = server.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String cmd = reader.readLine();
				Log.d("MessengerDroid","Received line:" + cmd);
				if(cmd.equals("exit")) {
					keepListening = false;
				}
				else {
					PrintWriter out = new PrintWriter(socket.getOutputStream(),	true);
					boolean result = dispatch.handleCommand(cmd);
					if (result) {
						out.println("Success");
					} else {
						out.println("Failure");
					}
					out.close();
					reader.close();
					socket.close();
				}
			}
			Log.d("MessengerDroid", "exiting");
		} catch (Exception e) {
			Log.e("MessengerDroid", "Error in IO", e);
		}
	}
	
	public void stopThread() {
		keepListening = false;
		Log.d("MessengerDroid", "stop has been called");
	}
}
