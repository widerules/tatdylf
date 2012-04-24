package communications;

import security.RSAUtilImpl;

import comm.messaging.Message;
import comm.messaging.SecureChannel;

public class Satellite_Deathstar extends Satellite {
	public Satellite_Deathstar(){
		super();
		port = 61244;
		toIP = "";//"209.2.211.96";//"192.168.1.9";//
		toPort = 61246;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/relay/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	public Satellite_Deathstar(int port){
		super();
		this.port = port;
		toIP = "";//"209.2.211.96";//"192.168.1.9";//
		toPort = 61246;
		rsaUtilServer = new RSAUtilImpl();
		rsaUtilServer.setPath("./res/relay/");
		channelServer = new SecureChannel(rsaUtilServer);
	}
	
	@Override
	protected void handleMessage(Message msg) {
		
		MaintenanceDroid_Deathstar droid = new MaintenanceDroid_Deathstar(msg, toIP, toPort);
		droid.start();
		
	}
}
