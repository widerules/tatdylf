package coruscant.jedi.temple.initialization;

import java.io.FileOutputStream;
import java.io.IOException;

import security.RSAUtilImpl;
import android.content.Context;

public class Init {
	
	public static boolean isInit = false;
	
	public static void init(Context ctx)
	{
	    try
	    {
	    	String[] filenames = ctx.fileList();
	    	
	    	for(int i=0; i<filenames.length; i++){
	    		if(filenames[i].equals("privateKey")){
	    			return;
	    		}
	    	}
	    	
	        FileOutputStream privOut = ctx.openFileOutput("privateKey", Context.MODE_PRIVATE);
	        FileOutputStream pubOut = ctx.openFileOutput("publicKey", Context.MODE_PRIVATE);
	        RSAUtilImpl.genAndWriteKeysToFile(pubOut, privOut);
	    }
	    catch (IOException e) {
			e.printStackTrace();
		}
	}

}
