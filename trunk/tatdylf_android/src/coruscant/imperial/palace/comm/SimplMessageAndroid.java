package coruscant.imperial.palace.comm;

import org.json.JSONException;
import org.json.JSONObject;

import comm.messaging.Command;
import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SimplMessage;

public class SimplMessageAndroid extends SimplMessage {

	@Override
	public Command getCmd() throws JSONException {
		return Command.values()[(Integer) json.get(Param.COMMAND)];
	}

	@Override
	public Message deSerialize(String msgString) throws JSONException {
		json = new JSONObject(msgString);
		return this;
	}
	

}
