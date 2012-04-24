package communications;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import security.RSAUtilImpl;

import comm.messaging.Endpoint;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SimplChannel;
import comm.messaging.SimplMessage;

public class InitRelay {
	
	public static void init(Socket socket, Satellite deathstar, Satellite coruscant) throws Exception{

	    try
	    {
	    	String path;
	    	SimplChannel channel = new SimplChannel();
	        
	        Message inMsg = channel.deSerialize(socket);
	        
	        Endpoint type = Endpoint.toEndpoint((Integer)inMsg.getParam(Param.ENDPOINT_TYPE));
	        String ip = socket.getInetAddress().toString().substring(1);
	        
	        switch(type){
	        	case CORUSCANT:
	        		path = "./res/client/";
	        		deathstar.setIp(ip);
	        		break;
	        	
	        	case DEATHSTAR:
	        		path = "./res/relay/";
	        		corsucant.setIp(ip);
	        }
	        
	    	FileOutputStream privOut = new FileOutputStream(path+"private.key");
	        FileOutputStream pubOut = new FileOutputStream(path+"myPublicKey");
	        RSAUtilImpl.genAndWriteKeysToFile(pubOut, privOut);
	        PublicKey pubKey = RSAUtilImpl.readPublicKeyFromFile(new FileInputStream(path+"myPublicKey"));
	        
	        KeyFactory fact = KeyFactory.getInstance("RSA");
	        RSAPublicKeySpec pubAndroid = fact.getKeySpec(pubKey, RSAPublicKeySpec.class);

	        
	        RSAPublicKeySpec pubCommRelay = new RSAPublicKeySpec(new BigInteger(
	        			(String)inMsg.getParam(Param.PUB_KEY_MOD)), new BigInteger((String) inMsg.getParam(Param.PUB_KEY_EXP)));
	        
	        RSAUtilImpl.saveToFile(new FileOutputStream(path+"public.key"),
	        		pubCommRelay.getModulus(), pubCommRelay.getPublicExponent());
	        
	        SimplMessage outMsg = new SimplMessage();
	        outMsg.addParam(Param.PUB_KEY_MOD, pubAndroid.getModulus().toString());
	        outMsg.addParam(Param.PUB_KEY_EXP, pubAndroid.getPublicExponent().toString());
	        
	        channel.serialize(outMsg, socket);
	        
	        
	    }
	    catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}

}
