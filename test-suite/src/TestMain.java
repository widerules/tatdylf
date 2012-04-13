import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class TestMain {

	public static void main(String[] args) throws IOException{
		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 8081);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		bw.write("VIB");
		bw.close();
		socket.close();
	}
}

