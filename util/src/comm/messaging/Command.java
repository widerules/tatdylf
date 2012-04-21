package comm.messaging;

import org.json.JSONString;

public enum Command implements JSONString{
	
	EXIT,
	INC_VOL,
	DEC_VOL,
	VIB_ON,
	VIB_OFF,
	SILENT_ON,
	SILENT_OFF,
	PLAY,
	LOCK,
	UNLOCK,
	LOCATE;

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal());
	}
	
}
