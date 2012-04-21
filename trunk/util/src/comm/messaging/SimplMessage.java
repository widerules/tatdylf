package comm.messaging;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

public class SimplMessage implements Message {

	private static final long serialVersionUID = 6724107325514097651L;
	private static final int INDENT = 4;
	
	protected Calendar c;
	protected JSONObject json = new JSONObject();
	
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
	public String serialize() {
		c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("0"));
		try {
			addParam("Timestamp", c.getTimeInMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		json = new JSONObject(msgString);
		return this;
	}
	
}
