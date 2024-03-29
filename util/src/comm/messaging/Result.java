package comm.messaging;

import org.json.JSONString;

public enum Result implements JSONString{
	
	SUCCESS,
	ERROR,
	INVALID_INPUT,
	MULTIPLE_CONTACTS,
	MULTIPLE_NUMBERS,
	CONTACT_NOT_FOUND,
	NUMBER_NOT_FOUND,
	PERMISSION_DENIED,
	RINGTONE_NOT_AUDIBLE,
	LOCATION_DISABLED;
	

	@Override
	public String toJSONString() {
		return Integer.toString(ordinal());
	}
	
	public static Result toResult(String s){
		if(s.equals("SUCCESS")){
			return Result.SUCCESS;
		} else if (s.equals("ERROR")){
			return Result.ERROR;
		} else if (s.equals("INVALID_INPUT")){
			return Result.INVALID_INPUT;
		} else if (s.equals("MULTIPLE_CONTACTS")){
			return Result.MULTIPLE_CONTACTS;
		} else if (s.equals("MULTIPLE_NUMBERS")){
			return Result.MULTIPLE_NUMBERS;
		} else if (s.equals("CONTACT_NOT_FOUND")){
			return Result.CONTACT_NOT_FOUND;
		} else if (s.equals("NUMBER_NOT_FOUND")){
			return Result.NUMBER_NOT_FOUND;
		} else if (s.equals("PERMISSION_DENIED")){
			return Result.PERMISSION_DENIED;
		} else if (s.equals("RINGTONE_NOT_AUDIBLE")){
			return Result.RINGTONE_NOT_AUDIBLE;
		} else if (s.equals("LOCATION_DISABLED")){
			return Result.LOCATION_DISABLED;
		}
		
		return null;
	}
	
}
