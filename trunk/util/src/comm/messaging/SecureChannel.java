package comm.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import security.RSAUtil;

public class SecureChannel {
	
	public static final int BUF_LEN = 1024;
	public static final byte EOF = (byte) 0xAA;
	public static final byte ESC = 0;
//	private static final Charset CHARSET = Charset.forName( "UTF-8" );
    private static final String ALGORITHM = "RSA";
    private static final String BLOCK_MODE = "ECB";
    private static final String PADDING = "PKCS1Padding";
    
    protected Cipher cipher = null;
	private RSAUtil rsaUtil = null;
	private String path = null;
	
	public SecureChannel(RSAUtil rsaUtil){
		this.rsaUtil = rsaUtil;
		try {
			cipher = Cipher.getInstance( ALGORITHM + "/" + BLOCK_MODE + "/" + PADDING );
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Message deSerialize(Socket socket) throws Exception{
		Message msg = new SimplMessage();
		return msg.deSerialize(readCiphered(cipher, getPublicKey(), socket));
	}
	
	public void serialize(Message msg, Socket socket) throws Exception{
		writeCiphered(cipher, getPrivateKey(), socket, msg.serialize());
	}
	
    protected static String readCiphered( Cipher cipher, PublicKey publicKey, Socket socket)
            throws Exception
        {
        cipher.init( Cipher.DECRYPT_MODE, publicKey );
        InputStream in = socket.getInputStream();
//        if(in.available() < 2){
//        	System.out.println("Sleeping");
//        	Thread.sleep(5000);
//        }
        final CipherInputStream cin = new CipherInputStream( in, cipher );
        // read big endian short length, msb then lsb
        final int messageLengthInBytes = ( cin.read() << 8 ) | cin.read();
        //System.out.println( file.length() + " enciphered bytes in file" );
        System.out.println( messageLengthInBytes + " reconstituted bytes" );
        final byte[] reconstitutedBytes = new byte[ messageLengthInBytes ];
        // we can't trust CipherInputStream to give us all the data in one shot
        int bytesReadSoFar = 0;
        int bytesRemaining = messageLengthInBytes;
        while ( bytesRemaining > 0 )
            {
            final int bytesThisChunk = cin.read( reconstitutedBytes, bytesReadSoFar, bytesRemaining );
            System.out.println("Bytes this chunk: "+bytesThisChunk);
            if ( bytesThisChunk == 0 )
                {
                throw new IOException( "Corrupt stream." );
                }
            bytesReadSoFar += bytesThisChunk;
            bytesRemaining -= bytesThisChunk;
            }
        cin.close();
        return (new String(reconstitutedBytes));//, CHARSET));
        }

    private static void writeCiphered( Cipher cipher, PrivateKey privateKey, Socket socket, String plainText )
            throws InvalidKeyException, IOException
        {
        cipher.init( Cipher.ENCRYPT_MODE, privateKey );
        final CipherOutputStream cout = new CipherOutputStream( socket.getOutputStream(), cipher );
        final byte[] plainTextBytes = plainText.getBytes();// CHARSET );
        System.out.println( plainTextBytes.length + " plaintext bytes written" );
        // prepend with big-endian short message length, will be encrypted too.
        cout.write( plainTextBytes.length >>> 8 );// msb
        cout.write( plainTextBytes.length & 0xff );// lsb
        cout.write( plainTextBytes );
        cout.flush();
        cout.close();
        }

   
	private PrivateKey getPrivateKey() throws Exception {
		return rsaUtil.readPrivateKeyFromFile();
	}

	protected PublicKey getPublicKey() throws Exception {
		return rsaUtil.readPublicKeyFromFile();
	}
	
//	public Message deSerializeMsg(BufferedInputStream in) throws Exception{
//		byte[] buf = new byte[BUF_LEN]; 
//		byte[] msgBytes = new byte[BUF_LEN];
//		System.out.println("Num bytes available to be read: "+in.available());
//		byte start = (byte)in.read();
//		System.out.println("First byte: "+start);
//		int num = in.read(buf);
//		System.out.println("Num bytes read: "+num);
//		System.out.println("Received buf: ");
//		for(int k=0; k<buf.length; k++)System.out.print(buf[k]);
//		if(buf[0] != EOF) throw new Exception("Invalid start of input");
//		for(int i=1, j=0; i<num; i++,j++){
//			if(buf[i] == ESC){
//				msgBytes[j] = buf[++i];
//				continue;
//			}
//			if(buf[i] == EOF) break;
//			msgBytes[j] = buf[i];
//		}
//		
//		Message msg = new SimplMessage();
//		return msg.deSerialize(new String(rsaUtil.decrypt(msgBytes)));
//		
//	}
//	
//	public void serializeMsg(Message msg, BufferedOutputStream out) throws Exception{
//		byte[] buf = rsaUtil.encrypt(msg.serialize().getBytes());
//		System.out.println("Number of bytes to be encoded: "+buf.length);
//		//byte[] msgBytes = new byte[BUF_LEN];
//		
//		int i = 0;
//		//msgBytes[i++] = EOF;
//		out.write(EOF);
//		//System.out.println("MsgBytes[0] = "+msgBytes[0]);
//		for(int j=0; j < buf.length; i++,j++){
//			if(buf[j] == ESC || buf[j] == EOF){
//				System.out.println("Inserting ESC: "+ESC);
//				//msgBytes[i++] = ESC;
//				out.write(ESC);
//				System.out.println("Buffer char: "+buf[j]);
//			}
//			//msgBytes[i] = buf[j];
//			out.write(buf[j]);
//		}
//		System.out.println("i: "+i);
//		//msgBytes[i] = EOF;
//		out.write(EOF);
//		//System.out.println("Sending buf: ");
//		//for(int k=0; k<=i; k++)System.out.print(msgBytes[k]+" ");
//		//System.out.println();
//		//out.write(msgBytes);
//		out.flush();
//	}
//	


}
