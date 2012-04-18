package security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * 
 * @author http://www.javamex.com/tutorials/cryptography/rsa_encryption.shtml
 * 
 * Most of the code in this class is taken verbatim from the site cited above
 *
 */


public class RSAUtil {
	
	private static boolean isInitialized = false;
	
	public static void initialize(){
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			saveToFile("/home/neil/public.key", pub.getModulus(),
			  pub.getPublicExponent());
			saveToFile("/home/neil/private.key", priv.getModulus(),
					  priv.getPrivateExponent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isInitialized = true;
	}
	
	public static void saveToFile(String fileName,
			  BigInteger mod, BigInteger exp) throws IOException {
			  ObjectOutputStream oout = new ObjectOutputStream(
			    new BufferedOutputStream(new FileOutputStream(fileName)));
			  try {
			    oout.writeObject(mod);
			    oout.writeObject(exp);
			  } catch (Exception e) {
			    throw new IOException("Unexpected error", e);
			  } finally {
			    oout.close();
			  }
	}
	
	public static PublicKey readPublicKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		  ObjectInputStream oin =
		    new ObjectInputStream(new BufferedInputStream(in));
		  try {
		    BigInteger m = (BigInteger) oin.readObject();
		    BigInteger e = (BigInteger) oin.readObject();
		    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		    KeyFactory fact = KeyFactory.getInstance("RSA");
		    PublicKey pubKey = fact.generatePublic(keySpec);
		    return pubKey;
		  } catch (Exception e) {
		    throw new RuntimeException("Spurious serialisation error", e);
		  } finally {
		    oin.close();
		  }
		}
	
	public static PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
		  InputStream in = new FileInputStream(keyFileName);
		    
		  ObjectInputStream oin =
		    new ObjectInputStream(new BufferedInputStream(in));
		  try {
		    BigInteger m = (BigInteger) oin.readObject();
		    BigInteger e = (BigInteger) oin.readObject();
		    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
		    KeyFactory fact = KeyFactory.getInstance("RSA");
		    PrivateKey privKey = fact.generatePrivate(keySpec);
		    return privKey;
		  } catch (Exception e) {
		    throw new RuntimeException("Spurious serialisation error", e);
		  } finally {
		    oin.close();
		  }
		}
	
	public static byte[] encrypt(byte[] data) throws Exception {
		if(!isInitialized) throw new Exception("RSAUtil has not been initialized");
		PublicKey pubKey = readPublicKeyFromFile("/home/neil/public.key");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] cipherData = cipher.doFinal(data);
		return cipherData;
	}
	
	public static byte[] decrypt(byte[] cipherData) throws Exception{
		if(!isInitialized) throw new Exception("RSAUtil has not been initialized");
		PrivateKey privKey = readPrivateKeyFromFile("/home/neil/private.key");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		byte[] data = cipher.doFinal(cipherData);
		return data;
	}

}
