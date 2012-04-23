package tests.comm;

import comm.messaging.Message;
import comm.messaging.Param;
import comm.messaging.SimplMessage;
import communications.circularMessageArray;

public class TestCircularArray {

	/**
	 * @param args
	 * @throws Exception 
	 * 
	 * Should error
	 */
	public static void main(String[] args) throws Exception {
		circularMessageArray cma = new circularMessageArray(3);
		Message m1 = new SimplMessage();
		m1.addParam(Param.MSGID, 1);
		Message r1 = new SimplMessage();
		r1.addParam(Param.MSGID, 1);
		Message m2 = new SimplMessage();
		m2.addParam(Param.MSGID, 2);
		Message r2 = new SimplMessage();
		r2.addParam(Param.MSGID, 2);
		Message m3 = new SimplMessage();
		m3.addParam(Param.MSGID, 3);
		Message r3 = new SimplMessage();
		r3.addParam(Param.MSGID, 3);
		Message m4 = new SimplMessage();
		m4.addParam(Param.MSGID, 4);
		Message r4 = new SimplMessage();
		r4.addParam(Param.MSGID, 4);
		cma.add(m1);
		cma.add(m2);
		cma.handle(r2);
		cma.add(m3);
		cma.add(m4);
		cma.handle(r1);
		cma.handle(r4);

	}

}
