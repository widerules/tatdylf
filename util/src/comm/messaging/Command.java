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
	LOCATE,
	TXT,
	ASYNC_VOL;

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal()); 
	}
	
	public static Command toCommand(String s){
		if(s.equals("EXIT") || s.equals("0")) {
			return Command.EXIT;
		} else if (s.equals("INC_VOL") || s.equals("1")) {
			return Command.INC_VOL;
		} else if (s.equals("DEC_VOL") || s.equals("2")) {
			return Command.DEC_VOL;
		} else if (s.equals("VIB_ON") || s.equals("3")) {
			return Command.VIB_ON;
		} else if (s.equals("VIB_OFF") || s.equals("4")) {
			return Command.VIB_OFF;
		} else if (s.equals("SILENT_ON") || s.equals("5")) {
			return Command.SILENT_ON;
		} else if (s.equals("SILENT_OFF") || s.equals("6")) {
			return Command.SILENT_OFF;
		} else if (s.equals("PLAY") || s.equals("7")) {
			return Command.PLAY;
		} else if (s.equals("LOCK") || s.equals("8")) {
			return Command.LOCK;
		} else if (s.equals("UNLOCK") || s.equals("9")) {
			return Command.UNLOCK;
		} else if (s.equals("LOCATE") || s.equals("10")) {
			return Command.LOCATE;
		} else if (s.equals("TXT") || s.equals("11")) {
			return Command.TXT;
		} else if (s.equals("ASYNC_VOL") || s.equals("12")) {
			return Command.ASYNC_VOL;
		}
		
		return null;
	}
	
}
