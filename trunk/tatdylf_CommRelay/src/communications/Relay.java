package communications;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

public class Relay {

	private static Satellite_Deathstar deathstar;
	private static Satellite_Coruscant coruscant;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		deathstar = new Satellite_Deathstar(61244);
		coruscant = new Satellite_Coruscant(61245);
		
		deathstar.start();
		coruscant.start();
		
		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(!deathstar.stopListening()){
			System.out.println("Waiting on deathstar");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		while(!coruscant.stopListening()){
			System.out.println("Waiting on coruscant");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessage(boolean success, int port, String string) throws Exception {
		System.out.println(string);
		
		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
		RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
		rsaUtilClient.setPath("./res/client/");
		SecureChannel channelClient = new SecureChannel(rsaUtilClient);
		
		Message outMsg = new SimplMessage();
		outMsg.addParam("success", success);
		
		channelClient.serialize(outMsg, socket);
		
	}
}
