package comm.messaging;

import org.json.JSONString;

public enum Param implements JSONString{
	
	TIMESTAMP,
	LATITUDE,
	LONGITUDE,
	RESULT,
	COMMAND;

	@Override
	public String toJSONString() {
		return String.valueOf(ordinal());
	}
	
}
