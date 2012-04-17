package communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Satellite_Coruscant extends Thread {
	
	boolean keepListening;
	int port;
	
	public Satellite_Coruscant(){
		port = 51244;
	}
	
	public Satellite_Coruscant(int port){
		this.port = port;
	}
	
	public void stopListening(){
		keepListening = false;
	}
	
	public String listen(int socket) throws IOException{
		
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(socket);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 51244.");
            System.exit(1);
        }
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                clientSocket.getInputStream()));
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            handleMessage(inputLine, out);
            if (inputLine.equals("Bye."))
               break;
       }
		
		return null;
	}
	
	private void handleMessage(String s, PrintWriter out){
		System.out.println(s);
		//Relay.sendMessage(out, "Message Received");
	}
	
	
}
