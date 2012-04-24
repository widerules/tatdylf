package communications;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;

public class Satellite_Coruscant extends Satellite {
	public Satellite_Coruscant(){
		super();
		port = 61245;
		toIP = "";
		toPort = 61243;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/client/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	public Satellite_Coruscant(int port){
		super();
		this.port = port;
		toIP = "";
		toPort = 61243;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/client/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	@Override
	protected void handleMessage(Message msg) {
		
		MaintenanceDroid_Coruscant droid = new MaintenanceDroid_Coruscant(msg, toIP, toPort);
		droid.start();
		
	}
}
