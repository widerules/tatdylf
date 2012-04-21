package coruscant.imperial.palace;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.res.Resources;
import android.util.Log;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import coruscant.imperial.palace.comm.SecureChannelAndroid;
import coruscant.imperial.palace.comm.SimplMessageAndroid;

import coruscant.imperial.palace.comm.RSAUtilImpl;

public class MessengerDroid extends Thread {
	
	String computerIP;
	Dispatcher dispatch;
	boolean keepListening;
	Resources resources;
	SecureChannelAndroid channel;
	
	public MessengerDroid(Resources res) {
		dispatch = new Dispatcher();
		resources = res;
		channel = new SecureChannelAndroid(new RSAUtilImpl(res));
		computerIP = "192.168.1.7";
	}
	
	private Socket openOutboundSocket() throws UnknownHostException, IOException {
		Socket s = new Socket(computerIP, 61245);
		return s;
	}

	public void run() {
		keepListening = true;
		ServerSocket server;
		try {
			/* outbound communication */
/*			Log.d("MessengerDroid", "Trying to create client socket");
			Socket clientSocket = openOutboundSocket();
			Message msg_out = new SimplMessage();
			msg_out.addParam("COMM", "Hello");
			channel.serialize(msg_out, clientSocket);*/
			
			Log.d("MessengerDroid", "Trying to create server socket");
			server = new ServerSocket(61246);
			while(keepListening) {
				Log.d("MessengerDroid", "Now waiting for connections on " + server.getLocalPort());
				Socket socket = server.accept();
				Message msg_in = channel.deSerialize(socket);
				Log.d("MessengerDroid","Received msg:" + msg_in.prettyPrint());
				if(msg_in.getCmd() == Command.EXIT) {
					keepListening = false;
				}
				else {
					boolean result = dispatch.handleCommand(msg_in);
					Socket clientSocket = openOutboundSocket();
					Message res = new SimplMessageAndroid();
					res.addParam(Param.RESULT, result);
					channel.serialize(res, clientSocket);
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