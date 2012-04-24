package comm.messaging;

import org.json.JSONString;

public enum Endpoint implements JSONString {
	CORUSCANT, DEATHSTAR;

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal());
	}
	
	public static Endpoint toEndpoint(int ord){
		return values()[ord];
	}

}
