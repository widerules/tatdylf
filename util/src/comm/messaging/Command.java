package comm.messaging;

import org.json.JSONString;

public enum Command implements JSONString{
	
	EXIT,
	INC_VOL,
	DEC_VOL,
	VIB;

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal());
	}
	
}
