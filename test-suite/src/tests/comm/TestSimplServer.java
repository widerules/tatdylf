package tests.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SimplChannel;
import comm.messaging.SimplMessage;

public class TestSimplServer {
	
	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(61243);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 61243.");
            System.exit(1);
        }
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        
        Message inMsg = null;
        SimplChannel channel = new SimplChannel();
        inMsg = channel.deSerialize(clientSocket);
        System.out.println(inMsg.prettyPrint());
        
        Message outMsg = new SimplMessage();
        outMsg.addParam(Param.LATITUDE, 2);
        channel.serialize(outMsg, clientSocket);
        channel.cleanUp();
 
	}

}
