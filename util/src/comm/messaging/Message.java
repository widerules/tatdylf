package comm.messaging;

import java.io.Serializable;

public interface Message extends Serializable{
	
	public static final String CMD = "COMMAND";
	public static final String RES = "RESULT";
	
	public Command getCmd() throws Exception;
	public Result getRes() throws Exception;
	public void addParam (String key, Object value) throws Exception;
	public Object getParam(String key) throws Exception;
	
	public String serialize();
	public String prettyPrint() throws Exception;
	
	public Message deSerialize(String msgString) throws Exception;
	
}
