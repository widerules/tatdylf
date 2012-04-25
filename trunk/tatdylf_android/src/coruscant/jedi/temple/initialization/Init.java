package coruscant.jedi.temple.initialization;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import security.RSAUtilImpl;
import android.content.Context;
import android.util.Log;

import comm.messaging.Endpoint;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SimplChannel;
import comm.messaging.SimplMessage;

import coruscant.imperial.palace.comm.SimplMessageAndroid;

public class Init {
	
	public static boolean isInit = false;
	
	public static void init(Context ctx) throws Exception
	{
	    try
	    {
	    	String[] filenames = ctx.fileList();
	    	
	    	for(int i=0; i<filenames.length; i++){
	    		if(filenames[i].equals("privateKey")){
	    			return;
	    		}
	    	}
	    	
	    	Log.d("Init", "Init started");
	    	
	        FileOutputStream privOut = ctx.openFileOutput("privateKey", Context.MODE_PRIVATE);
	        FileOutputStream pubOut = ctx.openFileOutput("myPublicKey", Context.MODE_PRIVATE);
	        RSAUtilImpl.genAndWriteKeysToFile(pubOut, privOut);
	        PublicKey pubKey = RSAUtilImpl.readPublicKeyFromFile(ctx.openFileInput("myPublicKey"));
	        
	        KeyFactory fact = KeyFactory.getInstance("RSA");
	        RSAPublicKeySpec pubAndroid = fact.getKeySpec(pubKey, RSAPublicKeySpec.class);
			
	        SimplMessage outMsg = new SimplMessageAndroid();
	        outMsg.addParam(Param.PUB_KEY_MOD, pubAndroid.getModulus().toString());
	        outMsg.addParam(Param.PUB_KEY_EXP, pubAndroid.getPublicExponent().toString());
	        outMsg.addParam(Param.INIT, true);
	        outMsg.addParam(Param.ENDPOINT_TYPE, Endpoint.CORUSCANT);
	        
	        String commRelayAddr = ctx.getString(R.string.comm_relay_ip);
	        int commRelayPort = Integer.parseInt(ctx.getString(R.string.comm_relay_inbound));
	        Socket socket = new Socket(commRelayAddr, commRelayPort);
	        
	        SimplChannel channel = new SimplChannel();
	        channel.serialize(outMsg, socket);
	        
	        Message inMsg = channel.deSerialize(socket);
	        RSAPublicKeySpec pubCommRelay = new RSAPublicKeySpec(new BigInteger(
	        			(String)inMsg.getParam(Param.PUB_KEY_MOD)), new BigInteger((String) inMsg.getParam(Param.PUB_KEY_EXP)));
	        
	        RSAUtilImpl.saveToFile(ctx.openFileOutput("publicKey", Context.MODE_PRIVATE),
	        		pubCommRelay.getModulus(), pubCommRelay.getPublicExponent());
	        
	        
	    }
	    catch (IOException e) {
			e.printStackTrace();
		}
	}

}
