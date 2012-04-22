package tests.comm;

import org.json.JSONException;
import org.json.JSONObject;

import security.RSAUtilImpl;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.SimplMessage;

public class TestComm {

	/**
	 * @param args
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Command cmd = Command.DEC_VOL;
		
		Message msg = new SimplMessage();
		
		msg.addParam("T1", "Hello World");
		msg.addParam(Message.CMD, cmd);
		msg.addParam("T2", 2);
		msg.addParam("T3", 3.1416);
		
		System.out.println(msg.prettyPrint());
		
		// Notice that we have to cast primitives to the boxed types
		Double dbl = (Double)msg.getParam("T3");
		Integer x = (Integer)msg.getParam("T2");
		Command cmmd = msg.getCmd();
		
		System.out.println("Printing double: "+dbl);
		System.out.println("Printing int: "+x);
		System.out.println("Printing command: "+cmmd.name());
		
//		RSAUtilImpl.initialize("/home/neil/");
//		System.out.println("Encrypted message: ");
//		byte[] msgBytes = msg.serializeEncrypted();
//		System.out.println(new String(msgBytes));
//		
//		System.out.println(((byte)'\n'));

	}

}
