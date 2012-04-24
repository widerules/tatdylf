import java.io.BufferedReader;
import java.io.InputStreamReader;


public class TestPhoneConnectivity {
	public static void main(String args[]) {
		//Socket socket = null;
		String input;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!(input = stdIn.readLine()).equals("End")) {
//				socket = new Socket(InetAddress.getByName(""), 61246);
				System.out.println("received: " + input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
