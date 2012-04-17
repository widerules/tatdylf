package communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Satellite extends Thread {
	boolean keepListening, closed;
	int port;

	public Satellite() {
	}
	
	public Socket talkToSelf(){
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
		} catch (Exception e) {
			return null;
		}
		return socket;
	}
	
	public void stopTalking(Socket s){
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean stopListening(Socket socket) {
		try {
			keepListening = false;
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("Bye.");
			out.close();
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
	
	public String listen(int socket) throws IOException{
		
		keepListening = true;
		closed = false;
		
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(socket);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 51245.");
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

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String inputLine;

			out.println("I see you!");

			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("Bye.")){
					out.println("Fine, I don't need you!  Just leave!");
					break;
				}
				handleMessage(inputLine, out);
			}
		}
		closed = true;
		return null;
	}
	
	private void handleMessage(String s, PrintWriter out){
		System.out.println(s);
		Relay.sendMessage(out, "Message Received");
	}	
}
