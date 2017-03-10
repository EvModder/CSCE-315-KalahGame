

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ServerMain extends Timer{
	int port = 42374;
	int MAX_CLIENTS = 3;
	ServerSocket socket;
	List<Client> clients;
	Thread connectionWaitThread;
	String outgoing = "";
	
	public class Client {
		PrintWriter out;
		BufferedReader in;
		String name="null";
		public Client(Socket connection){
			try {
				out = new PrintWriter(connection.getOutputStream());
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				new Thread(){@Override public void run(){
//					try{name = in.readLine();}
//					catch(IOException e){e.printStackTrace();}
//				}}.start();
			}
			catch(IOException e){e.printStackTrace();}
		}
	}
	
	public void start(){
		try{socket = new ServerSocket(port);}
		catch(IOException e){e.printStackTrace();return;}
		
		connectionWaitThread = new Thread(){
			@Override public void run(){
				try{
					clients = new ArrayList<Client>();
					
					while(clients.size() < MAX_CLIENTS){
						clients.add(new Client(socket.accept()));
						System.out.println("Got a connection to a client");
					}
				}
				catch(IOException e){e.printStackTrace();}
			}
		};
		connectionWaitThread.start();
		
		new Thread(){
			@Override public void run(){
				Scanner s = new Scanner(System.in);
				String line;
				while(!(line = s.nextLine()).equals("close")) outgoing += (line + '\n');
				s.close();
			}
		}.start();
		
		schedule(new TimerTask(){
			@Override public void run() {
				Iterator<Client> it = clients.iterator();
				while(it.hasNext()){
					Client client = it.next();
					try{
						if(client.in.ready()){
							String incoming = client.in.readLine();
							if(incoming.equals("close")){
								it.remove();
								System.out.println("A client left the server");
								client = null;
							}
							else{
								System.out.println("Recieved from client: "+incoming);
							}
						}
						if(client != null && !outgoing.isEmpty()){
							client.out.print(outgoing);
							client.out.flush();
						}
					}
					catch(IOException e){e.printStackTrace();}
				}
				outgoing = "";
			}
		}, 0, 10);
		System.out.println("Server opened");
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		if(socket != null) try{socket.close();} catch(IOException e){}
		connectionWaitThread.stop();
	}
	
	public static void main(String[] args){
		new ServerMain().start();
	}
}
