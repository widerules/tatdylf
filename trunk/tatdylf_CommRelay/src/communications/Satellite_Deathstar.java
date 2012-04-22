package communications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import security.RSAUtilImpl;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

public class Satellite_Deathstar extends Satellite {
	public Satellite_Deathstar(){
		super();
		port = 61244;
		toIP = "192.168.1.9";
		toPort = 61246;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/relay/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	public Satellite_Deathstar(int port){
		super();
		this.port = port;
		toIP = "192.168.1.9";
		toPort = 61246;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/relay/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	@Override
	protected void handleMessage(Message msg) throws Exception{
		
		Message outMsg = new SimplMessage();
		
		Command command = Command.INC_VOL;
		
		if(msg.getParam("cmd").equals("volDown")){
			command = Command.DEC_VOL;
		} else if(msg.getParam("cmd").equals("volUp")){
			command = Command.INC_VOL;
		} else if(msg.getParam("cmd").equals("silAc")){
			command = Command.SILENT_OFF;
		} else if(msg.getParam("cmd").equals("silDeac")){
			command = Command.SILENT_ON;
		} else if(msg.getParam("cmd").equals("vibAc")){
			command = Command.VIB_ON;
		} else if(msg.getParam("cmd").equals("vibDeac")){
			command = Command.VIB_OFF;
		} else if(msg.getParam("cmd").equals("lock")){
			command = Command.LOCK;
		} else if(msg.getParam("cmd").equals("unlock")){
			command = Command.UNLOCK;
		} else if(msg.getParam("cmd").equals("gps")){
			command = Command.LOCATE;
		} else if(msg.getParam("cmd").equals("textNumber")){
			command = Command.TXT;
			outMsg.addParam(Param.TXT_TO_NUMBER, msg.getParam("to"));
			outMsg.addParam(Param.TXT_BODY, msg.getParam("text"));
		} else if(msg.getParam("cmd").equals("textName")){
			command = Command.TXT;
			outMsg.addParam(Param.TXT_TO_NAME, msg.getParam("to"));
			outMsg.addParam(Param.TXT_BODY, msg.getParam("text"));
		}
		
		try {
			Socket socket = new Socket(InetAddress.getByName(toIP), toPort);
			RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
			rsaUtilClient.setPath("./res/client/");
			SecureChannel channelClient = new SecureChannel(rsaUtilClient);
			
			outMsg.addParam(Param.COMMAND, command);
			outMsg.addParam(Param.MSGID, 1);
			
			channelClient.serialize(outMsg, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
