package comm.messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SimplChannel {
	
	BufferedReader br = null;
	PrintWriter pw = null;
	
	public Message deSerialize(Socket socket) throws Exception{
		
		InputStream in = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(in));
		String msgString = br.readLine();
		Message msg = new SimplMessage();
		return msg.deSerialize(msgString);
		
	}
	
	public void serialize(Message msg, Socket socket) throws Exception{
		OutputStream out = socket.getOutputStream();
		pw = new PrintWriter(new OutputStreamWriter(out));
		pw.println(msg.serialize());
		pw.flush();
	}
	
	public void cleanUp(){
		try {
			br.close();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
