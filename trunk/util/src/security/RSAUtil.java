package security;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface RSAUtil {
	
	public PrivateKey readPrivateKeyFromFile() throws IOException;
	public PublicKey readPublicKeyFromFile() throws IOException;

}
