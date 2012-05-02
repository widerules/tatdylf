package coruscant.imperial.palace.comm;

import org.json.JSONException;
import org.json.JSONObject;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.Result;
import comm.messaging.SimplMessage;

public class SimplMessageAndroid extends SimplMessage {

	private static final long serialVersionUID = 795888752537500613L;

	@Override
	public Command getCmd() throws JSONException {
		return Command.values()[(Integer) json.get(Param.COMMAND)];
	}

	@Override
	public Result getRes() throws JSONException {
		return (Result) json.get(Param.RESULT);
	}

	@Override
	public Message deSerialize(String msgString) throws JSONException {
		json = new JSONObject(msgString);
		return this;
	}
	

}
