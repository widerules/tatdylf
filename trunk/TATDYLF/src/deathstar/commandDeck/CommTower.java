package deathstar.commandDeck;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;

public class CommTower extends Thread {
	
	ControlRoom frame;
	int port;
	boolean keepListening;
	
	public CommTower(ControlRoom frame, int port) {
		this.frame = frame;
	}
	
	public void run() {
		try {
			listen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void listen() throws Exception {
		
		keepListening = true;
		
		RSAUtilImpl rsaDesktop = new RSAUtilImpl();
		rsaDesktop.setPath("./res/desktop/");
		SecureChannel channelDesktop = new SecureChannel(rsaDesktop);

		while (keepListening) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				System.err.println("Could not listen on port: " + port);
				System.exit(1);
			}

			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Connected");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

			Message inMsg = channelDesktop.deSerialize(clientSocket);
			
			System.out.println(inMsg.prettyPrint());
			
			serverSocket.close();
			
			handleMessage(inMsg);

		}
	}

	private void handleMessage(Message inMsg) {

		CommProtocol cp = new CommProtocol(inMsg, frame);
		
		cp.start();
		
	}

}
