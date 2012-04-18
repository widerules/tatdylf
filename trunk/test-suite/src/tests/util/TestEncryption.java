package tests.util;

import security.RSAUtil;

public class TestEncryption {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RSAUtil.initialize();
		System.out.println("RSAUtil initialized");
		
		StringBuilder sb = new StringBuilder();
		
		byte[] cipher = RSAUtil.encrypt("Hello World".getBytes());
		System.out.println(new String(cipher));
		byte[] data = RSAUtil.decrypt(cipher);
		String str = new String(data);
		System.out.println(str);
		
	}

}
