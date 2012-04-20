package coruscant.imperial.palace;

import java.net.ServerSocket;
import java.net.Socket;

import android.content.res.Resources;
import android.util.Log;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;
import coruscant.imperial.palace.comm.RSAUtilImpl;

public class MessengerDroid extends Thread {
	
	Dispatcher dispatch;
	boolean keepListening;
	Resources resources;
	SecureChannel channel;
	
	public MessengerDroid(Resources res) {
		dispatch = new Dispatcher();
		resources = res;
		channel = new SecureChannel(new RSAUtilImpl(res));
	}
	

	public void run() {
		/* Test inbound connection */
		keepListening = true;
		ServerSocket server;
		try {
			
			Log.d("MessengerDroid", "Trying to create client socket");
			Socket socket = new Socket("10.0.2.15", 61246);
			Message msg = new SimplMessage();
			msg.addParam("COMM", "Hello");
			channel.serialize(msg, socket);
			
//			Log.d("MessengerDroid", "Trying to create server socket");
//			server = new ServerSocket(61246);
//			while(keepListening) {
//				Log.d("MessengerDroid", "Now waiting for connections on " + server.getLocalPort());
//
//				Socket socket = server.accept();
//				
//				//BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				//String cmd = reader.readLine();
//				
//				//Message msg = SecureChannel.deSerializeMsg(socket);
//				Message msg = null;
//				Log.d("MessengerDroid","Received msg:" + msg.prettyPrint());
//				if(msg.getCmd() == Command.EXIT) {
//					keepListening = false;
//				}
//				else {
//					//PrintWriter out = new PrintWriter(socket.getOutputStream(),	true);
//					//BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
//					dispatch.handleCommand(msg);
//					//Message res = new SimplMessage();
//					//res.addParam("RESPONSE", "Message Dispatched");
//					//SecureChannel.serializeMsg(res, out);
//					//out.close();
//					//socket.close();
//				}
//			}
//			Log.d("MessengerDroid", "exiting");
		} catch (Exception e) {
			Log.e("MessengerDroid", "Error in IO", e);
		}
	}
	
	public void stopThread() {
		keepListening = false;
		Log.d("MessengerDroid", "stop has been called");
	}
}
