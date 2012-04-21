package communications;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;

public class Satellite extends Thread {
	boolean keepListening, closed;
	int port, toPort;
	String toIP;
	protected RSAUtilImpl rsaUtilServer;
	protected SecureChannel channelServer;

	public Satellite() {
	}
	
	public Socket openOutboundSocket(String ipAddress, int targetPort){
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName(ipAddress), targetPort);
		} catch (Exception e) {
			return null;
		}
		return socket;
	}
	
	public void closeSocket(Socket s){
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean stopListening() {
		try {
			keepListening = false;
			
			Socket socket = openOutboundSocket("127.0.0.1", port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("Bye.");
			out.close();
			closeSocket(socket);
		} catch (Exception e) {
			keepListening = true;
			return false;
		}
		return closed;
	}

	public void run() {
		try {
			listen(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String listen(int listenerPort) throws IOException{
		
		keepListening = true;
		closed = false;
		
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(listenerPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + listenerPort);
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
			Message inMsg = null;
			try {
				inMsg = channelServer.deSerialize(clientSocket);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				handleMessage(inMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		closed = true;
		return null;
	}
	
	protected void handleMessage(Message msg) throws Exception{
		
		boolean success = true;
		
		if(msg.getParam("cmd").equals("volDown")){
			
		}
		
		Random gen = new Random();
		if(gen.nextInt(3) == 0){
			success = false;
		}
		
		try {
			Relay.sendMessage(-1, success, toPort, toIP, "Message Received");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
