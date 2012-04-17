package communications;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Satellite_Coruscant extends Satellite {
	public Satellite_Coruscant(){
		super();
		port = 61244;
	}
	
	public Satellite_Coruscant(int port){
		super();
		this.port = port;
	}
}
