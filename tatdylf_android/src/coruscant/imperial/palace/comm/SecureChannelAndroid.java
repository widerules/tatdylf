package coruscant.imperial.palace.comm;

import java.net.Socket;

import security.RSAUtil;

import comm.messaging.Message;
import comm.messaging.SecureChannel;

public class SecureChannelAndroid extends SecureChannel {

	public SecureChannelAndroid(RSAUtil rsaUtil) {
		super(rsaUtil);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized Message deSerialize(Socket socket) throws Exception {
		Message msg = new SimplMessageAndroid();
		return msg.deSerialize(readCiphered(cipher, getPublicKey(), socket));
	}

}
