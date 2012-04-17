package communications;

import java.io.PrintWriter;

public class Relay {

	private static Satellite_Deathstar deathstar;
	private static Satellite_Coruscant coruscant;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		deathstar = new Satellite_Deathstar(51244);
		coruscant = new Satellite_Coruscant(51245);
		
		deathstar.start();
		coruscant.start();
		
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		deathstar.stopListening();
		coruscant.stopListening();
		
	}

	public static void sendMessage(PrintWriter out, String string) {
		out.println(string);
	}

}
