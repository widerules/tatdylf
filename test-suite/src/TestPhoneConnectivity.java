import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class TestPhoneConnectivity {
	public static void main(String args[]) {
		Socket socket = null;
		String input;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!(input = stdIn.readLine()).equals("End")) {
				socket = new Socket(InetAddress.getByName("209.2.236.210"), 61246);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.println(input);
				input = in.readLine();
				System.out.println("received: " + input);
				in.close();
				out.close();
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
