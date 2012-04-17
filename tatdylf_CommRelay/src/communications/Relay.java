package communications;

import java.io.PrintWriter;
import java.net.Socket;

public class Relay {

	private static Satellite_Deathstar deathstar;
	private static Satellite_Coruscant coruscant;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		deathstar = new Satellite_Deathstar(61244);
		coruscant = new Satellite_Coruscant(61245);
		
		deathstar.start();
		coruscant.start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Socket s = deathstar.talkToSelf();
		while(!deathstar.stopListening(s)){
			System.out.println("Waiting on deathstar");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		deathstar.stopTalking(s);
		
		s = coruscant.talkToSelf();
		while(!coruscant.stopListening(s)){
			System.out.println("Waiting on coruscant");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		coruscant.stopTalking(s);
	}

	public static void sendMessage(PrintWriter out, String string) {
		out.println(string);
		System.out.println(string);
	}

}
