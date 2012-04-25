package deathstar.commandDeck;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

public class AttuneCommTower {
	
	public static void init(String path) throws Exception{

    	FileOutputStream privOut = new FileOutputStream(path+"private.key");
        FileOutputStream pubOut = new FileOutputStream(path+"myPublicKey");
        RSAUtilImpl.genAndWriteKeysToFile(pubOut, privOut);
        PublicKey pubKey = RSAUtilImpl.readPublicKeyFromFile(new FileInputStream(path+"myPublicKey"));
        
        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubAndroid = fact.getKeySpec(pubKey, RSAPublicKeySpec.class);
    	
        SimplMessage outMsg = new SimplMessage();
        outMsg.addParam(Param.PUB_KEY_MOD, pubAndroid.getModulus().toString());
        outMsg.addParam(Param.PUB_KEY_EXP, pubAndroid.getPublicExponent().toString());
        outMsg.addParam(Param.INIT, true);
        outMsg.addParam(Param.ENDPOINT_TYPE, Endpoint.DEATHSTAR);
        	        
	    Socket socket = new Socket(Constant.COMM_RELAY, Constant.COMM_RELAY_INIT_PORT); 
        
        SimplChannel channel = new SimplChannel();
        channel.serialize(outMsg, socket);
        
        Message inMsg = channel.deSerialize(socket);
        RSAPublicKeySpec pubCommRelay = new RSAPublicKeySpec(new BigInteger(
        			(String)inMsg.getParam(Param.PUB_KEY_MOD)), new BigInteger((String) inMsg.getParam(Param.PUB_KEY_EXP)));
        
        RSAUtilImpl.saveToFile(new FileOutputStream(path+"public.key"),
        		pubCommRelay.getModulus(), pubCommRelay.getPublicExponent());
                
        
    
	}

}
