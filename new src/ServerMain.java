package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain extends Connection{
	final int port = 42374;
	ServerSocket socket;
	Client client;
	Thread ioThread;
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
	
	@Override
	public boolean isClosed(){
		return socket != null && !socket.isClosed() && client != null && !client.socket.isClosed();
	}
	
	public ServerMain(MessageReceiver rec){
		super(rec);
		try{socket = new ServerSocket(port);}
		catch(IOException e){e.printStackTrace();return;}
		System.out.println("Server opened, waiting for client...");
		
		try{client = new Client(socket.accept());}
		catch(IOException e){e.printStackTrace();}
		System.out.println("Client connected");
		
		ioThread = new Thread(){
			@Override public void run() {
				while(!socket.isClosed()){
					try{
						if(client.socket.isClosed()){
							System.out.println("The client left the server!");
						}
						else{
							while(client.in.ready()){
								String line = client.in.readLine();
								System.out.println("Received: "+line);
								rec.receiveMessage(line);
							}
//							if(outgoing != null){
//								System.out.println("Sending: "+outgoing.toString());
//								client.out.print(outgoing.toString());
//								client.out.flush();
//								outgoing = null;
//							}
						}
					}
					catch(IOException e){e.printStackTrace();}
				}
			}
		};
		ioThread.start();
	}
	
	@Override
	public void close(){
		if(socket != null) try{socket.close();} catch(IOException e){}
	}
	
	@Override
	public void println(String message){
//		if(outgoing == null) outgoing = new StringBuilder(message).append('\n');
//		else outgoing.append(message).append('\n');
		client.out.println(message);
		client.out.flush();
		System.out.println("Sent: "+message);
	}
}