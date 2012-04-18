package comm.messaging;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class SimplMessage implements Message {

	private static final long serialVersionUID = 6724107325514097651L;
	private static final int INDENT = 4;
	
	private JSONObject json = new JSONObject();
	Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public Command getCmd() {
		return (Command) map.get(CMD);
	}

	@Override
	public void addParam(String key, Object value) throws JSONException {
		json.put(key, value);
	}

	@Override
	public Object getParam(String key) throws JSONException{
		return json.get(key);
	}
	
	private String serialize(){
		return new JSONObject(map).toString();
	}

	@Override
	public String toString() {
		return serialize();
	}
	
	public String prettyPrint() throws JSONException{
		return new JSONObject(map).toString(INDENT);		
	}
	
	public Message deSerialize(String msgString) throws JSONException{
		SimplMessage msg = new SimplMessage();
		msg.json = new JSONObject(msgString);
		return null;
	}
	
}
