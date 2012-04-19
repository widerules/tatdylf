package comm.messaging;

import java.io.Serializable;

import org.json.JSONException;

public interface Message extends Serializable{
	
	public static final String CMD = "COMMAND";
	
	public Command getCmd() throws JSONException;
	public void addParam (String key, Object value) throws JSONException;
	public Object getParam(String key) throws JSONException;
	
	public String serialize();
	public byte[] serializeEncrypted() throws Exception;
	public String prettyPrint() throws Exception

}
