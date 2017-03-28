package ServerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ClientMain extends Connection{
	final int port = 42374;
	String host = "165.91.48.10";//50.24.131.142 | 192.168.1.63 | localhost
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Thread ioThread;
	StringBuilder outgoing;

	@Override
	public void close(){
		try{socket.close();} catch(IOException e){e.printStackTrace();}
	}

	@Override
	public boolean isClosed(){
		return socket != null && !socket.isClosed();
	}

	public ClientMain(MessageReceiver rec){
		super(rec);
		host = JOptionPane.showInputDialog("Please enter the Host Address", "localhost");
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
							while(in.ready()){
								String line = in.readLine();
								System.out.println("Received: "+line);
								receiver.receiveMessage(line);
							}
							
//							if(outgoing != null){
//								System.out.println("Sending: "+outgoing.toString());
//								out.print(outgoing.toString());
//								out.flush();
//								outgoing = null;
//							}
						}
						catch(IOException e){e.printStackTrace();}
					}
					System.out.print("Server closed. Reconnect? ");
				}
			};
			ioThread.start();
		}
	}
	
	@Override
	public synchronized void println(String message){
//		if(outgoing == null) outgoing = new StringBuilder(message).append('\n');
//		else outgoing.append(message).append('\n');
		out.println(message);
		out.flush();
		System.out.println("Sent: "+message);
	}
}