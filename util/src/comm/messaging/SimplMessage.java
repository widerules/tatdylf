package comm.messaging;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import security.RSAUtil;

public class SimplMessage implements Message {

	private static final long serialVersionUID = 6724107325514097651L;
	private static final int INDENT = 4;
	
	private JSONObject json = new JSONObject();
	Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public Command getCmd() throws JSONException {
		return (Command) json.get(CMD);
	}

	@Override
	public void addParam(String key, Object value) throws JSONException {
		json.put(key, value);
	}

	@Override
	public Object getParam(String key) throws JSONException{
		return json.get(key);
	}
	
	@Override
	public String serialize(){
		return new JSONObject(map).toString();
	}
	
	@Override
	public byte[] serializeEncrypted() throws Exception{
		return RSAUtil.encrypt(serialize().getBytes());
	}

	@Override
	public String toString() {
		return serialize();
	}
	
	@Override
	public String prettyPrint() throws JSONException{
		return new JSONObject(map).toString(INDENT);		
	}
	
	@Override
	public Message deSerialize(String msgString) throws JSONException{
		SimplMessage msg = new SimplMessage();
		msg.json = new JSONObject(msgString);
		return null;
	}
	
	@Override
	public Message deSerializeEncrypted(byte[] data) throws Exception{
		return deSerialize(new String(RSAUtil.decrypt(data)));
	}
	
}
