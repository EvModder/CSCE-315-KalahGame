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
import java.util.Timer;

public class ServerMain extends Timer{
	public static void main(String[] args){new ServerMain();}
	
	int port = 42374;
	int MAX_CLIENTS = 3;
	ServerSocket socket;
	List<Client> clients;
	Thread connectionWaitThread, ioThread;
	StringBuilder outgoing;
	
	class Client {
		Socket socket;
		PrintWriter out;
		BufferedReader in;
		Client(Socket connection){
			socket = connection;
			try {
				out = new PrintWriter(connection.getOutputStream());
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			catch(IOException e){e.printStackTrace();}
		}
	}
	
	public ServerMain(){
		try{socket = new ServerSocket(port);}
		catch(IOException e){e.printStackTrace();return;}
		
		connectionWaitThread = new Thread(){
			@Override public void run(){
				try{
					clients = new ArrayList<Client>();
					
					while(true){
						Socket connection = socket.accept();
						if(clients.size() == MAX_CLIENTS){
							PrintWriter temp = new PrintWriter(connection.getOutputStream());
							temp.println("Server is full!");
							temp.flush();
							connection.close();
							continue;
						}
						clients.add(new Client(connection));
						System.out.println("Got a connection to a client");
					}
				}
				catch(IOException e){e.printStackTrace();}
			}
		};
		connectionWaitThread.start();
		
		ioThread = new Thread(){
			@Override public void run() {
				while(!socket.isClosed()){
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
									String incoming = client.in.readLine();
									System.out.println("Received from client: "+incoming);
									//TODO: send received message to game board to process
								}
								if(outgoing != null){
									client.out.print(outgoing.toString());
									client.out.flush();
								}
							}
						}
						catch(IOException e){e.printStackTrace();}
					}
					outgoing = null;
				}
			}
		};
		ioThread.start();
		
		System.out.println("Server opened");
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		connectionWaitThread.stop();
		if(socket != null) try{socket.close();} catch(IOException e){}
	}
	
	public int numClients(){
		return clients.size();
	}
	
	public void println(String message){
		if(outgoing == null) outgoing = new StringBuilder(message).append('\n');
		else outgoing.append(message).append('\n');
	}
}