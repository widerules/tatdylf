package comm.messaging;

import org.json.JSONException;
import org.json.JSONObject;

public class SimplMessage implements Message {

	private static final long serialVersionUID = 6724107325514097651L;
	private static final int INDENT = 4;
	
	private JSONObject json = new JSONObject();
	
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
		return json.toString();
	}
	
	
	@Override
	public String toString() {
		return serialize();
	}
	
	@Override
	public String prettyPrint() throws JSONException{
		return json.toString(INDENT);		
	}
	
	@Override
	public Message deSerialize(String msgString) throws JSONException{
		SimplMessage msg = new SimplMessage();
		msg.json = new JSONObject(msgString);
		return msg;
	}
	
}
