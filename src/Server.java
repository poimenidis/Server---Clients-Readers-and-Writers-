import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 1234;
	private static AirportList airportList = new AirportList();
	private static ServerSocket socketConn;
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		socketConn = new ServerSocket(PORT);

		/* First the server waits to receive a socket request for connection from a client.
		 * After that, it accepts client's request and it connects with client
		 * After the connection, server starts its thread where it passes the socket and the airportList.
		 * AirportList is the main server's list where clients can read , write, delete and update*/
		while(true) {
			System.out.println("Server is waiting...");
			Socket socket = socketConn.accept();
			System.out.println("Received request from" + socket.getInetAddress());
			ServerThread sthread = new ServerThread(socket,airportList);
			sthread.start();
		}

		//serverInputStream.close();
		//serverOutputStream.close();
		//pipe.close();
		//socketConn.close();
	}

}

/*This is the ServerThread which has a socket (the connection between server and client) and an airportlist (which is global)*/
class ServerThread extends Thread {
	private Socket socket;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;
	private AirportList airportList;
	
	public ServerThread(Socket socket, AirportList list) {
		this.socket = socket;
		/*serverInputStream is where server can read client's requests
		 *serverOutputStream is where server answers client*/
		try {
			serverInputStream = new ObjectInputStream(this.socket.getInputStream());
			serverOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			airportList = list;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*After the thread starts*/
	public void run() {
		Reply reply = new Reply();
		Request request;
		/* First server gets client's request via "serverInputStream". 
		 * After that it creates a "serverProtocol" where it passes client's request and it executes it.
		 * Finally, it passes the relevant message at server's reply and send it to client via "serverOutputStream".*/
		try {
			while(true) {
				request = (Request) serverInputStream.readObject();
				ServerProtocol serverProtocol = new ServerProtocol(airportList);
				reply = serverProtocol.processRequest(request);
				serverOutputStream.writeObject(reply);
				serverOutputStream.reset();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

/* Here it is serverProtocol where server executes client's requests.
 * It needs the AirportList and a client's request and it returns a reply for client*/
class ServerProtocol {
	private AirportList airportList;

	public ServerProtocol(AirportList list) {
		airportList = list;
	}
	public Reply processRequest(Request request) {
		Reply reply = airportList.executeRequest(request);
		return reply;
	}
}
