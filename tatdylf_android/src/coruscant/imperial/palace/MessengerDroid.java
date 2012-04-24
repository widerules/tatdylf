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
import comm.messaging.Result;
import coruscant.imperial.palace.comm.SecureChannelAndroid;
import coruscant.imperial.palace.comm.SimplMessageAndroid;

import coruscant.imperial.palace.comm.RSAUtilImpl;

public class MessengerDroid extends Thread {
	private static boolean isRunning = false;
	String computerIP;
	Dispatcher dispatch;
	boolean keepListening;
	Resources resources;
	SecureChannelAndroid channel;
	
	public MessengerDroid(Resources res) {
		dispatch = new Dispatcher();
		resources = res;
		channel = new SecureChannelAndroid(new RSAUtilImpl(res));
		computerIP = "128.59.19.241";
	}
	
	public static void startDroid(Resources res) {
		if(!isRunning) {
			MessengerDroid droid = new MessengerDroid(res);
			droid.start();
			isRunning = true;
		}
	}
	
	private Socket openOutboundSocket() throws UnknownHostException, IOException {
		Socket s = new Socket(computerIP, 61245);
		return s;
	}

	public void run() {
		keepListening = true;
		ServerSocket server = null;
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
				else if(msg_in.getCmd() == Command.LOCATE) {
					Message res = dispatch.handleLocation(msg_in);
					Socket clientSocket = openOutboundSocket();
					channel.serialize(res, clientSocket);
				}
				else {
					Result result = dispatch.handleCommand(msg_in);
					Socket clientSocket = openOutboundSocket();
					Message res = new SimplMessageAndroid();
					res.addParam(Param.MSGID, msg_in.getParam(Param.MSGID));
					Log.d("Messenger Droid", "responding to message " + msg_in.getParam(Param.MSGID));
					res.addParam(Param.RESULT, result);
					channel.serialize(res, clientSocket);
				}
			}
			server.close();
			Log.d("MessengerDroid", "exiting");
		} catch (Exception e) {
			Log.e("MessengerDroid", "Error in IO", e);
		}
		isRunning = false;
	}
	
	public void stopThread() {
		keepListening = false;
		Log.d("MessengerDroid", "stop has been called");
	}
}
