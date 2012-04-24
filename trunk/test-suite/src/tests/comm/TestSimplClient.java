package tests.comm;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SimplChannel;
import comm.messaging.SimplMessage;

public class TestSimplClient {

	public static void main(String[] args) throws Exception{
		Socket kkSocket = null;
        //OutputStream out = null;
        //InputStream in = null;
 
        //Scanner s = new Scanner(System.in);
        
        try {
            kkSocket = new Socket("127.0.0.1", 61243);
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: taranis.");
            System.exit(1);
        }

        Message outMsg = new SimplMessage();
        outMsg.addParam(Param.COMMAND, Command.DEC_VOL);
        SimplChannel channel = new SimplChannel();
        channel.serialize(outMsg, kkSocket);
        
        Message inMsg = channel.deSerialize(kkSocket);
        System.out.println(inMsg.prettyPrint());
        channel.cleanUp();
        
	}
}
