import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	//final variables for HOST and PORT of connection
	private static final String HOST = "localhost";
	private static final int PORT = 1234;
	private static Socket socketConn;
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		socketConn = new Socket(HOST, PORT);

		/*Streams that read or write object. In this case object can be Reply or Request.*/
		ObjectOutputStream clientOutputStream = new ObjectOutputStream(socketConn.getOutputStream());
		ObjectInputStream clientInputStream = new ObjectInputStream(socketConn.getInputStream());

		ClientProtocol clientProtocol = new ClientProtocol();
		Request request;
		Reply reply = new Reply();
		
		
		/* First client creates a "clientProtocol" where client prepares his request.
		 * After that client sends his request to server. 
		 * Finally, client receives a relevant reply from server and show it (via processReply).*/
		while(true) {
			
			request = clientProtocol.prepareRequest();
			clientOutputStream.writeObject(request);
			reply = (Reply)clientInputStream.readObject();
			clientProtocol.processReply(reply);
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		//clientOutputStream.close();
        //clientInputStream.close();
        
        //socketConn.close();
		
	}

}

/*Here is the ClientProtocol. In here client prepares his requests or process server's Reply*/
class ClientProtocol {
	String condition; //= null;
	String ip; //= null;
	String time; //= null;
	Request request; //= null;
	Scanner scanner;
	
	/* Here is where client's request is prepared.
	 * First system asks to type a specific message depending on what he wants to do (Read or Write).
	 * Then after it processed the request, it returns it with an opcode, ip, condition and time.*/
	public Request prepareRequest() throws IOException {
		System.out.println("\n******Make your choice******");
		System.out.println("READ <Ip>");
		System.out.println("WRITE <Ip> <Condition> <Time>");
		System.out.println("DELETE <Ip>");
		
		scanner = new Scanner(System.in);
		String message = scanner.nextLine(); 
		String[] splitedMessage = message.split(" ");
		String opcode = splitedMessage[0];
		
		switch (opcode) {
		case "READ":
			if(splitedMessage.length==2) {
				ip = splitedMessage[1];
				condition = null;
				time = null;
			}
			break;
		case "WRITE":
			if(splitedMessage.length==4) {
				ip = splitedMessage[1];
				condition = splitedMessage[2];
				time = splitedMessage[3];
			}
			break;
		case "DELETE":
			if(splitedMessage.length==2) {
				ip = splitedMessage[1];
				condition = null;
				time = null;
			}
			break;
		}
		request = new Request(opcode,ip,condition,time);
		return request;
		
	}
	
	public void processReply(Reply reply) {
		System.out.println("Reply: " + reply.getSomethingToReturn());
	}
}
