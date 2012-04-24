package tests.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class TestEncryption {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		KeyPairGenerator kpg = null;
		//this.path = path;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();
		
		KeyFactory fact;
		RSAPublicKeySpec pub = null;
		RSAPrivateKeySpec priv = null;
		
		try {
			fact = KeyFactory.getInstance("RSA");
			pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		priv.getModulus();
		pub.getModulus();
	}

}
