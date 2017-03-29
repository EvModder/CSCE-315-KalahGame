package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {
	public static void main(String[] args){new ClientMain();}
	
	int port = 42374;
	String host = "localhost";//165.91.12.50 | 192.168.1.5 | localhost
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Thread ioThread;
	StringBuilder outgoing;

	public void close(){
		try{socket.close();} catch(IOException e){e.printStackTrace();}
	}

	public void println(String message){
		if(outgoing == null) outgoing = new StringBuilder(message).append('\n');
		else outgoing.append(message).append('\n');
	}

	public ClientMain(){
		try{
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(UnknownHostException e){
			System.out.println("Unable to connect to server!");
			socket=null;
		}
		catch(IOException e){e.printStackTrace();}

		if(socket == null){
			System.out.print("Unable to connect.");
			return;
		}
		else{
			System.out.println("Connected to server");

			ioThread = new Thread(){
				@Override public void run(){
					while(!socket.isClosed()){
						try{
							if(in.ready()){
								String incoming = in.readLine();
								System.out.println("Received from server: "+incoming);
								//TODO: send received message to game board to process
							}
							if(outgoing != null){
								out.print(outgoing.toString());
								out.flush();
								outgoing = null;
							}
						}
						catch(IOException e){e.printStackTrace();}
					}
					System.out.print("Server closed. Reconnect? ");
				}
			};
			ioThread.start();
		}
	}
}