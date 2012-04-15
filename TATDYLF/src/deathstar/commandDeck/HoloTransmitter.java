package deathstar.commandDeck;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class HoloTransmitter {
	
	public HoloTransmitter() {
	}

	public static boolean sendSMS() throws MessagingException {
		boolean debug = false;

	    //Set the host smtp address
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "send.columbia.edu");
	    
	    // create some properties and get the default Session
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(debug);

	    // create a message
	    Message msg = new MimeMessage(session);

	    // set the from and to address
	    InternetAddress addressFrom = new InternetAddress("koala0106@gmail.com");
	    msg.setFrom(addressFrom);

	    InternetAddress[] addressTo = new InternetAddress[2]; 
        addressTo[0] = new InternetAddress("ndw2114@columbia.edu");
        addressTo[1] = new InternetAddress("nad2135@columbia.edu");
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	   

	    // Optional : You can also set your custom headers in the Email if you Want
//	    msg.addHeader("MyHeaderName", "myHeaderValue");

	    // Setting the Subject and Content Type
	    msg.setSubject("Email from Java");
	    msg.setContent("I really hope this works", "text/plain");
	    Transport.send(msg);
		return true;
	}
}
