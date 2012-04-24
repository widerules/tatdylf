package tests.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;



public class TestSecureServer {
    public static void main(String[] args) throws Exception {
 
    	ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(61243);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 61244.");
            System.exit(1);
        }
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
 
        //OutputStream out = clientSocket.getOutputStream();
//        InputStream in = clientSocket.getInputStream();
        
//        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(
//                clientSocket.getInputStream()));
//        
//        String inputLine, outputLine;
//        KnockKnockProtocol kkp = new KnockKnockProtocol();
// 
//        outputLine = kkp.processInput(null);
//        Message outMsg = new SimplMessage();
        Message inMsg = null;
//        outMsg.addParam("COMM", outputLine);
        RSAUtilImpl rsaUtil = new RSAUtilImpl();
        rsaUtil.setPath("../tatdylf/res/desktop/");
        SecureChannel channel = new SecureChannel(rsaUtil);
        //channel.serialize(outMsg, clientSocket);
//        System.out.println("Server: "+outputLine);
        
        inMsg = channel.deSerialize(clientSocket);
        String inputLine = (String)inMsg.getParam("success");
        System.out.println("Server recvd: "+inputLine);
// 
//        while ((inMsg = channel.deSerialize(clientSocket)) != null) {
//        	 inputLine = (String)inMsg.getParam("COMM");
//             outputLine = kkp.processInput(inputLine);
//             outMsg.addParam("COMM", outputLine);
//             channel.serialize(outMsg, clientSocket);
//             if (outputLine.equals("Bye."))
//                break;
//        }
//        out.close();
//        in.close();
//        clientSocket.close();
        serverSocket.close();
    }


}
