package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Main.Settings;

//A basic server connection that joins to a client
public class ServerSide extends Connection{
	ServerSocket socket;
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;
	
	@Override
	public boolean isClosed(){
		return socket == null || socket.isClosed() || clientSocket == null || clientSocket.isClosed();
	}
	
	public ServerSide(MessageReceiver rec, Settings settings){
		super(rec);
		//Open a server port & wait for a client to join
		try{socket = new ServerSocket(settings.getInt("port"));}
		catch(IOException e){e.printStackTrace();return;}
		System.out.println("Server opened, waiting for client...");
		
		try{
			clientSocket = socket.accept();
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch(IOException e){e.printStackTrace();}
		System.out.println("Client connected");
		
		//Thread to read any incoming data from client
		new Thread(){
			@Override public void run() {
				while(!socket.isClosed()){
					try{
						if(clientSocket.isClosed()){
							System.out.println("The client left the server!");
						}
						else{
							synchronized(clientSocket){
								while(in.ready()){
									String line = in.readLine();
									System.out.println("Received: "+line);
									rec.receiveMessage(line);
								}
							}
						}
					}
					catch(IOException e){e.printStackTrace();}
				}
			}
		}.start();
	}
	
	@Override
	public void close(){
		synchronized(clientSocket){ try{
			if(socket != null) socket.close();
			if(clientSocket != null) clientSocket.close();
		}catch(IOException e){}}
	}
	
	@Override
	public void println(String message){
		if(isClosed()) return;
		out.println(message);
		out.flush();
		System.out.println("Sent: "+message);
	}
}