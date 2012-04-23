package communications;

import comm.messaging.Message;

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
	
	private int nextInt(){
		currentLoc = (currentLoc+1)%size;
		return currentLoc;
	}
	
	public void add(Message msg){
		
	}
	
	public void handle(Message response){
		
	}
	
	public void findByMessageID(int id){
		
	}
	
}
