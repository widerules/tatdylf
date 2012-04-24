package communications;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;

public abstract class Satellite extends Thread {
	boolean keepListening, closed;
	int port, toPort;
	String toIP;
	protected RSAUtilImpl rsaUtilServer;
	protected SecureChannel channelServer;

	public Satellite() {
	}
	
	public void setIp(String ip){
		toIP = ip;
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
				handleMessage(inMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closed = true;
		return null;
	}
	
	protected abstract void handleMessage(Message msg);	
}
