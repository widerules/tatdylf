package tests.comm;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;
import comm.messaging.SimplMessage;

public class TestSecureClient {
	
	public static void main(String args[]) throws Exception{

		 
        Socket kkSocket = null;
        //OutputStream out = null;
        //InputStream in = null;
 
        //Scanner s = new Scanner(System.in);
        
        try {
            kkSocket = new Socket("192.168.1.7", 61243);
//            out = kkSocket.getOutputStream();
//            in = kkSocket.getInputStream();
//            out = new PrintWriter(kkSocket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: taranis.");
            System.exit(1);
        }
 
//        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//        String fromServer;
//        String fromUser;
// 
//        Message inMsg = null;
        Message outMsg = new SimplMessage();
        
        RSAUtilImpl rsaUtil = new RSAUtilImpl();
        rsaUtil.setPath("../tatdylf_CommRelay/res/relay/");
        
        SecureChannel channel = new SecureChannel(rsaUtil);
//        fromUser = stdIn.readLine();
//        if (fromUser != null) {
//              System.out.println("Client: " + fromUser);
              outMsg.addParam("success", false);
              channel.serialize(outMsg, kkSocket);
//        }
        //inMsg = channel.deSerialize(kkSocket);
    	//fromServer = (String) inMsg.getParam("COMM");
        //System.out.println("Client: recvd:" + fromServer);
    
        
      
        
//        while ((inMsg = channel.deSerialize(kkSocket)) != null) {
//        	fromServer = (String) inMsg.getParam("COMM");
//            System.out.println("Client: recvd:" + fromServer);
//            if (fromServer.equals("Bye."))
//                break;
//             
//            fromUser = stdIn.readLine();
//        if (fromUser != null) {
//                System.out.println("Client: " + fromUser);
//                outMsg.addParam("COMM", fromUser);
//                channel.serialize(outMsg, kkSocket);
//        	}
//        }
 
        //out.close();
        //in.close();
//        stdIn.close();
//        kkSocket.close();
    
	}

}
