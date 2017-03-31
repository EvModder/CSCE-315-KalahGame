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

public class MultiServer{
	
	public interface ConnectionReceiver{
		void gotConnection(Connection connection);
	}
	
	final int port = 42374;
	int MAX_CLIENTS = 2;
	ServerSocket socket;
	List<Client> clients;
	Thread connectionWaitThread;
	ConnectionReceiver receiver;
	boolean closeServer;
	
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

		@Override public void close() {
			try{socket.close();}catch(IOException e){e.printStackTrace();}
		}

		@Override public void println(String message) {
			out.println(message);
			out.flush();
		}
	}
	
	public MultiServer(ConnectionReceiver connRec){
		receiver = connRec;
		try{socket = new ServerSocket(port);}
		catch(IOException e){e.printStackTrace();return;}
		
		connectionWaitThread = new Thread(){
			@Override public void run(){
				try{
					clients = new ArrayList<Client>();
					
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
		
		new Thread(){
			@Override public void run() {
				while(!socket.isClosed() && !closeServer){
					synchronized(clients){
						Iterator<Client> it = clients.iterator();
						while(it.hasNext()){
							Client client = it.next();
							try{
								if(client.socket.isClosed()){
									it.remove();
									System.out.println("A client left the server");
								}
								else{
									if(client.in.ready()){
										client.receiver.receiveMessage(client.in.readLine());
									}
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
			for(Client client : clients) client.close();
		}
		connectionWaitThread.stop();
		if(socket != null) try{socket.close();} catch(IOException e){}
	}
}