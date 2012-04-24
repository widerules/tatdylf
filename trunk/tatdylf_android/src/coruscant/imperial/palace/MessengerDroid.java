package coruscant.imperial.palace;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import security.RSAUtil;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;
import comm.messaging.SecureChannel;

import coruscant.imperial.palace.comm.RSAUtilImpl;
import coruscant.imperial.palace.comm.SecureChannelAndroid;
import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class MessengerDroid extends Thread {
	private static boolean isRunning = false;
	Dispatcher dispatch;
	boolean keepListening;
	Resources resources;
	SecureChannelAndroid channel;
	
	public MessengerDroid(Context ctx) {
		dispatch = new Dispatcher();
		resources = ctx.getResources();
		channel = new SecureChannelAndroid(new RSAUtilImpl(ctx));
	}
	
	public static void startDroid(Context ctx) {
		if(!isRunning) {
			MessengerDroid droid = new MessengerDroid(ctx);
			droid.start();
			isRunning = true;
		}
	}
	
	public static void updateIP(RSAUtil rsaUtil) throws Exception{
		Socket s = openOutboundSocket();
		SecureChannel channnel = new SecureChannelAndroid(rsaUtil);
		Message msg = new SimplMessageAndroid();
		msg.addParam(Param.NEW_IP, s.getLocalAddress().toString().substring(1));
	}
	
	private static Socket openOutboundSocket() throws UnknownHostException, IOException {
		Socket s = new Socket(TheSenate.getCommIP(), TheSenate.getCommPort());
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
