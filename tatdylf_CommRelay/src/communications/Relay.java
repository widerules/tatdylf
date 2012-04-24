package communications;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;
import comm.messaging.SimplChannel;
import comm.messaging.SimplMessage;

public class Relay {

	private static Satellite_Deathstar deathstar;
	private static Satellite_Coruscant coruscant;
	private static int messageID = 0;
	private static circularMessageArray msgArr = new circularMessageArray(50);
	private static int initialPort = 61246;

	public Relay() {}
	
	public static void main(String[] args) {
		
		deathstar = new Satellite_Deathstar(61244);
		coruscant = new Satellite_Coruscant(61245);
		
		deathstar.start();
		coruscant.start();
		
		boolean keepListening = true;
		
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(initialPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + initialPort);
            System.exit(1);
        }
        
        while (keepListening) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Connected");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			try {
				InitRelay.init(clientSocket, "/res/tests/");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        
	}

	/*public static void sendMessage(int i, boolean success, int port, String toIP, String string) throws Exception {
		System.out.println(string);
		
		Socket socket = new Socket(InetAddress.getByName(toIP), port);
		RSAUtilImpl rsaUtilClient = new RSAUtilImpl();
		rsaUtilClient.setPath("./res/client/");
		SecureChannel channelClient = new SecureChannel(rsaUtilClient);
		
		Message outMsg = new SimplMessage();
		outMsg.addParam("success", success);
		
		channelClient.serialize(outMsg, socket);
		
	}*/
	
	public synchronized int getNextID(){
		return messageID++;
	}
	
	public synchronized void addToArray(Message msg){
		msgArr.add(msg);
	}
	
	public synchronized void handled(Message msg) throws Exception{
		msgArr.handle(msg);
	}
	
	public synchronized int findMessageById(int id) throws Exception {
		return msgArr.findByMessageID(id);
	}
	
	public synchronized Message getMessage(int index){
		return msgArr.getMessage(index);
	}
}
