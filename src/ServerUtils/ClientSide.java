package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import Main.Utils;

public class ClientSide extends Connection{
	final int port = 42374;
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Thread ioThread;

	@Override
	public void close(){
		try{socket.close();} catch(IOException e){e.printStackTrace();}
	}

	@Override
	public boolean isClosed(){
		return socket == null || socket.isClosed();
	}

	public ClientSide(MessageReceiver rec){
		super(rec);
		String host = Utils.getHostNameWindow();
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

			//ioThread
			new Thread(){
				@Override public void run(){
					while(!socket.isClosed()){
						try{
							while(in.ready()){
								String line = in.readLine();
								System.out.println("Received: "+line);
								receiver.receiveMessage(line);
							}
						}
						catch(IOException e){e.printStackTrace();}
					}
				}
			}.start();
		}
	}
	
	@Override
	public void println(String message){
		out.println(message);
		out.flush();
		System.out.println("Sent: "+message);
	}
}