package tests.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import security.RSAUtilImpl;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

public class TestText {

	public static void main(String[] args) throws Exception {
		
		String toIP = "192.168.1.9";
		int toPort = 61246;
		
		Socket socket = new Socket(InetAddress.getByName(toIP), toPort);
		RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
		rsaUtilClient.setPath("./res/client/");
		SecureChannel channelClient = new SecureChannel(rsaUtilClient);
		
		Message outMsg = new SimplMessage();
		outMsg.addParam(Param.COMMAND, Command.TXT);
		outMsg.addParam(Param.TXT_TO, "3149226150");
		outMsg.addParam(Param.TXT_BODY, "This is a test.  It is only a test.  You should be alarmed.");
		outMsg.addParam(Param.MSGID, 1);
		
		channelClient.serialize(outMsg, socket);
		
	}
	
}
