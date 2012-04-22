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
import java.util.Random;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

public class Satellite_Coruscant extends Satellite {
	public Satellite_Coruscant(){
		super();
		port = 61245;
		toIP = "127.0.0.1";
		toPort = 61243;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/client/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	public Satellite_Coruscant(int port){
		super();
		this.port = port;
		toIP = "127.0.0.1";
		toPort = 61243;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/client/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	@Override
	protected void handleMessage(Message msg) throws Exception{
		
		Result res = (Result) msg.getRes();
		
		boolean success = (res == Result.SUCCESS);
		
		double lat = -181;
		double lon = -181;
		try {
			lat = (Double) msg.getParam(Param.LATITUDE);
			lon = (Double) msg.getParam(Param.LONGITUDE);
		} catch (Exception e) {}
		
		try {
			Socket socket = new Socket(InetAddress.getByName(toIP), toPort);
			RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
			rsaUtilClient.setPath("./res/relay/");
			SecureChannel channelClient = new SecureChannel(rsaUtilClient);
			
			Message outMsg = new SimplMessage();
			outMsg.addParam("success", success);
			if(lat != -181){
				outMsg.addParam("lat", lat);
				outMsg.addParam("long", lon);
			}
			
			channelClient.serialize(outMsg, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
