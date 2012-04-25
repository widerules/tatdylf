package communications;

import java.net.InetAddress;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;
import comm.messaging.SecureChannel;

public class MaintenanceDroid_Coruscant extends MaintenanceDroid {

	private Message msg;
	private String toIP;
	private int toPort;
	Satellite coruscant;
	
	public MaintenanceDroid_Coruscant() {}

	public MaintenanceDroid_Coruscant(Message msg, String toIP, int toPort) {
		this.msg = msg;
		this.toIP = toIP;
		this.toPort = toPort;
	}
	
	@Override
	protected void handleMessage() throws Exception{
		
		Relay relay = new Relay();
		
		try {
			relay.getDeathstar().setIp((String) msg.getParam(Param.NEW_IP));
			System.out.println((String) msg.getParam(Param.NEW_IP));
			return;
		} catch (Exception e){}
		
		int msgId = (Integer) msg.getParam(Param.MSGID);
		Command command;
		double lat = -181;
		double lon = -181;
		Message outMsg = msg;
		if (msgId >= 0) {
			relay.handled(msg);

			int loc = relay.findMessageById(msgId);

			command = relay.getMessage(loc).getCmd();

			try {
				lat = (Double) msg.getParam(Param.LATITUDE);
				lon = (Double) msg.getParam(Param.LONGITUDE);
			} catch (Exception e) {}
		} else {
			command = Command.ASYNC_VOL;
			outMsg.addParam(Param.RESULT, "SUCCESS");
		}
		try {
			Socket socket = new Socket(InetAddress.getByName(toIP), toPort);
			RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
			rsaUtilClient.setPath("./res/relay/");
			SecureChannel channelClient = new SecureChannel(rsaUtilClient);
			
			outMsg.addParam(Param.COMMAND, command);
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
