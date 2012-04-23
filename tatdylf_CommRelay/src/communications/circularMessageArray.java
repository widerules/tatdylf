package communications;

import comm.messaging.Message;
import comm.messaging.Param;

public class circularMessageArray {

	int size;
	int currentLoc;
	Message[] sentArray;
	boolean[] responseArray;
	
	public circularMessageArray(int size) {
		this.size = size;
		sentArray = new Message[size];
		responseArray = new boolean[size];
		currentLoc = 0;
	}
	
	private int nextLoc(){
		currentLoc = (currentLoc+1)%size;
		return currentLoc;
	}
	
	public void add(Message msg){
		
		//TODO: handle messages that get phased out
		
		sentArray[currentLoc] = msg;
		responseArray[currentLoc] = false;
		currentLoc = nextLoc();
	}
	
	public void handle(Message response) throws Exception{
		int id = (Integer) response.getParam(Param.MSGID);
		int location = findByMessageID(id);
		
		if(location == -1){
			throw new Exception("We have a problem, this message ID got a response when it was never sent.  Or this response came too late.");
		}
		
		responseArray[location] = true;
	}
	
	public int findByMessageID(int id) throws Exception{
		for(int i = (size+currentLoc-1)%size; i != currentLoc; i = (size+i-1)%size){
			if(id == (Integer)sentArray[i].getParam(Param.MSGID)){
				return i;
			}
		}
		return -1;
	}
	
	public Message getMessage(int index){
		return sentArray[index];
	}
	
}
