package communications;

public abstract class MaintenanceDroid extends Thread {
	
	public MaintenanceDroid() { }
	
	public void run() {
		try {
			this.handleMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void handleMessage() throws Exception;

}
