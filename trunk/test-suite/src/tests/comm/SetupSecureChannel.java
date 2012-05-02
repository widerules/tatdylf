package tests.comm;

import security.RSAUtilImpl;

public class SetupSecureChannel {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		RSAUtilImpl rsaServer = new RSAUtilImpl("/home/neil/commRelay/");
		RSAUtilImpl rsaClient = new RSAUtilImpl("/home/neil/client/");
		
		System.out.println("RSA keys generated");

	}

}
