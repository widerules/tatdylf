package comm.messaging;

import org.json.JSONString;

public enum Endpoint implements JSONString {
	CORUSCANT, DEATHSTAR;

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal());
	}
	
	public static Endpoint toEndpoint(Object obj){
		
		if(obj instanceof Integer){
			return values()[(Integer) obj];
		} else if(obj instanceof String){
			return valueOf((String)obj);
		}
		return null;
	}
}
