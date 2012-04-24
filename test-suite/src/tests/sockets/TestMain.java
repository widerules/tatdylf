package tests.sockets;

import java.io.IOException;


/**
 * @author neil
 *
 */
public class TestMain {

	public static void main(String[] args) throws IOException{
//		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 8081);
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//		bw.write("Go Dispatcher!");
//		bw.close();
//		socket.close();
		for(int i=0; i<8; i++){
			System.out.println(i);
			if(true){
				i+=2;
				continue;
			}
		}
		
		
	}
}

