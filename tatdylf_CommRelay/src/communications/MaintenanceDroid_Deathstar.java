package communications;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

public class MaintenanceDroid_Deathstar extends MaintenanceDroid {

	private Message msg;
	private String toIP;
	private int toPort;
	
	public MaintenanceDroid_Deathstar() {}

	public MaintenanceDroid_Deathstar(Message msg, String toIP, int toPort) {
		this.msg = msg;
		this.toIP = toIP;
		this.toPort = toPort;
	}
	
	@Override
	protected void handleMessage() throws Exception{
		
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
		} else if(msg.getParam("cmd").equals("play")){
			command = Command.PLAY;
		} else if(msg.getParam("cmd").equals("textNumber")){
			command = Command.TXT;
			outMsg.addParam(Param.TXT_BY_NAME, false);
			outMsg.addParam(Param.TXT_TO, msg.getParam("to"));
			outMsg.addParam(Param.TXT_BODY, msg.getParam("text"));
		} else if(msg.getParam("cmd").equals("textName")){
			command = Command.TXT;
			outMsg.addParam(Param.TXT_BY_NAME, true);
			outMsg.addParam(Param.TXT_TO, msg.getParam("to"));
			outMsg.addParam(Param.TXT_BODY, msg.getParam("text"));
		}
		
		try {
			sendMessage(outMsg, command);
		} catch (ConnectException ce) {
			System.out.println("The IP has changed.  Use the right one");
			System.out.println("Wrong IP: " + toIP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void sendMessage(Message outMsg, Command command) throws ConnectException, Exception {
		Socket socket = new Socket(InetAddress.getByName(toIP), toPort);
		RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
		rsaUtilClient.setPath("./res/client/");
		SecureChannel channelClient = new SecureChannel(rsaUtilClient);
		
		outMsg.addParam(Param.COMMAND, command);
		outMsg.addParam(Param.MSGID, new Relay().getNextID());
		
		new Relay().addToArray(outMsg);
		
		channelClient.serialize(outMsg, socket);
	}

}
