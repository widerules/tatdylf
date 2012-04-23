package communications;

public class MaintenanceDroid extends Thread {
	
	public MaintenanceDroid() { }
	
	public void run() {
		try {
			this.handleMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void handleMessage() throws Exception{}

}
