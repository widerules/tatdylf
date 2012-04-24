package communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import comm.messaging.Message;

public class Relay {

	private static Satellite_Deathstar deathstar;
	private static Satellite_Coruscant coruscant;
	private static int messageID = 0;
	private static circularMessageArray msgArr = new circularMessageArray(50);
	private static int initialPort = 61246;
	private static int deathstarPort = 61244;
	private static int coruscantPort = 61245;

	public Relay() {}
	
	public static void main(String[] args) {
		
		deathstar = new Satellite_Deathstar(deathstarPort);
		coruscant = new Satellite_Coruscant(coruscantPort);
		
		deathstar.start();
		coruscant.start();
		
		boolean keepListening = true;
		
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(initialPort);
            System.out.println("Server set up");
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + initialPort);
            System.exit(1);
        }
        
        while (keepListening) {
			Socket clientSocket = null;
			try {
				System.out.println("Waiting on a connection");
				clientSocket = serverSocket.accept();
				System.out.println("Connected");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			deathstar.toIP = clientSocket.getInetAddress().toString().substring(1);
			System.out.println(clientSocket.getInetAddress().toString());
			System.out.println(new String(clientSocket.getInetAddress().getAddress()));
			try {
				System.out.println("Starting relay");
				InitRelay.init(clientSocket, "./res/client/");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        
	}
	
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
