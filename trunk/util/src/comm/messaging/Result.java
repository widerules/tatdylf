package comm.messaging;

import org.json.JSONString;

public enum Result implements JSONString{
	
	SUCCESS,
	ERROR,
	INVALID_INPUT,
	MULTIPLE_CONTACTS,
	MULTIPLE_NUMBERS;
	

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal());
	}
	
}
