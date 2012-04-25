package coruscant.imperial.palace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
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
	
	public static void updateIP(RSAUtil rsaUtil, Context context) throws Exception{
		// get ip
		URL url = new URL("http://109.74.3.85/ip/"); //http://api.externalip.net/ip/
								// http://automation.whatismyip.com/n09230945.asp
//		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String ip = in.readLine().trim();
//		String ip = content.replaceAll("[^\\.0-9]","");
		Log.d("R2D2", "ip: " + ip);
		//send ip
		Socket s = new Socket(context.getString(R.string.comm_relay_ip), Integer.parseInt(context.getString(R.string.comm_relay_outbound)));
		SecureChannel channel = new SecureChannelAndroid(rsaUtil);
		Message msg = new SimplMessageAndroid();
		msg.addParam(Param.NEW_IP, ip);
		channel.serialize(msg, s);
	}
	
	private static Socket openOutboundSocket() throws UnknownHostException, IOException {
		Socket s = new Socket(TheSenate.getCommIP(), TheSenate.getToCommPort());
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
			server = new ServerSocket(TheSenate.getFromCommPort());
			while(keepListening) {
				Log.d("MessengerDroid", "Now waiting for connections on " + server.getLocalPort());
				Socket socket = server.accept();
				Message msg_in = channel.deSerialize(socket);
				Log.d("MessengerDroid","Received msg:" + msg_in.prettyPrint());
				if(msg_in.getCmd() == Command.EXIT) {
					keepListening = false;
				}
				else {
//					Result result = dispatch.handleCommand(msg_in);
					Message res = dispatch.handleCommand((SimplMessageAndroid)msg_in);
					Socket clientSocket = openOutboundSocket();
/*					Message res = new SimplMessageAndroid();
					res.addParam(Param.MSGID, msg_in.getParam(Param.MSGID));
					Log.d("Messenger Droid", "responding to message " + msg_in.getParam(Param.MSGID));
					res.addParam(Param.RESULT, result);*/
					Log.d("MessengerDroid","Sending msg:" + res.prettyPrint());
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
