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
import java.net.UnknownHostException;

public class Satellite_Deathstar extends Satellite {
	public Satellite_Deathstar(){
		super();
		port = 61244;
	}
	
	public Satellite_Deathstar(int port){
		super();
		this.port = port;
	}

}
