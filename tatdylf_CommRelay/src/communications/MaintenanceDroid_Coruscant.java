package communications;

import java.net.InetAddress;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;
import comm.messaging.SecureChannel;

public class MaintenanceDroid_Coruscant extends MaintenanceDroid {

	private Message msg;
	private String toIP;
	private int toPort;
	
	public MaintenanceDroid_Coruscant() {}

	public MaintenanceDroid_Coruscant(Message msg, String toIP, int toPort) {
		this.msg = msg;
		this.toIP = toIP;
		this.toPort = toPort;
	}
	
	@Override
	protected void handleMessage() throws Exception{
		
		Result res = msg.getRes();
		
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
			
			Message outMsg = msg;
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
