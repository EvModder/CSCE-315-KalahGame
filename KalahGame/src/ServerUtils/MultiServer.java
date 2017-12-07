package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Like a regular server, except can connect to multiple clients.
//Each client gets its own connection object
public class MultiServer{
	
	public interface ConnectionReceiver{
		void gotConnection(Connection connection);
	}
	
	final int port = 42374;
	int MAX_CLIENTS = 20;
	ServerSocket socket;
	List<Client> clients;
	Thread connectionWaitThread;
	ConnectionReceiver receiver;
	boolean closeServer;
	
	//A clients object, created when the server gets a new connection
	class Client extends Connection{
		Socket socket;
		PrintWriter out;
		BufferedReader in;
		
		Client(Socket connection){
			super(null);
			socket = connection;
			try {
				out = new PrintWriter(connection.getOutputStream());
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			catch(IOException e){e.printStackTrace();}
		}
		@Override public boolean isClosed() {
			return socket == null || socket.isClosed();
		}

		@Override public void close(){
			try{if(socket!=null)socket.close();socket = null;}
			catch(IOException e){e.printStackTrace();}
		}

		@Override public void println(String message){
			if(isClosed()) return;
			System.out.println("Sent: "+message);
			out.println(message);
			out.flush();
		}
	}
	
	public MultiServer(ConnectionReceiver connRec){
		receiver = connRec;
		try{socket = new ServerSocket(port);}
		catch(IOException e){e.printStackTrace();return;}
		clients = new ArrayList<Client>();
		
		//Wait for new clients to join
		connectionWaitThread = new Thread(){
			@Override public void run(){
				try{
					while(!closeServer){
						Socket connection = socket.accept();
						if(clients.size() == MAX_CLIENTS){
							PrintWriter temp = new PrintWriter(connection.getOutputStream());
							temp.println("Server is full!");
							temp.flush();
							connection.close();
							continue;
						}
						synchronized(clients){
							Client newClient = new Client(connection);
							clients.add(newClient);
							connRec.gotConnection(newClient);
						}
						System.out.println("Got a connection to a client");
					}
				}
				catch(IOException e){e.printStackTrace();}
			}
		};
		connectionWaitThread.start();
		
		//ioThread
		new Thread(){
			@Override public void run() {
				//For each client, read its incoming messages and send any outgoing messages.
				while(!socket.isClosed() && !closeServer){
					synchronized(clients){
						Iterator<Client> it = clients.iterator();
						while(it.hasNext()){
							Client client = it.next();
							try{
								if(client.isClosed()){
									it.remove();
									System.out.println("A client left the server (Remaining: "+clients.size()+")");
								}
								else if(client.in.ready()){
									String message = client.in.readLine();
									System.out.println("Received: "+message);
									client.receiver.receiveMessage(message);
								}
							}
							catch(IOException e){e.printStackTrace();}
						}
					}
				}
			}
		}.start();
		
		System.out.println("Server opened");
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		closeServer = true;
		synchronized(clients){
			//Close each of the server's clients
			for(Client client : clients) client.close();
		}
		connectionWaitThread.stop();
		if(socket != null) try{socket.close();} catch(IOException e){}
	}
}