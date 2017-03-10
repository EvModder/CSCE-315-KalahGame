

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {
	int port = 42374;
	String host = "localhost";//50.24.144.43 | 192.168.1.58 165.91.13.218 | localhost
	String myName = "Nathaniel";
	Server server;
	Thread thread;
	
	public class Server {
		PrintWriter out;
		BufferedReader in;
		private Socket socket;
		boolean closed;
		
		public Server(String host, int port){
			try{
				socket = new Socket(host, port);
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch(UnknownHostException e){
				System.out.println("Unable to connect to server!");
				socket=null;
			}
			catch(IOException e){}
		}
		
		public String getMessage(){
			try{
				String line = in.readLine();
				closed = line == null;
				return line;
			}
			catch(IOException e){
				e.printStackTrace();
				return null;
			}
		}
		
		public void sendMessage(String msg){
			out.println(msg);
			out.flush();
		}
		
		public boolean isClosed(){
			return closed;
		}
		
		public void close(){
			try{socket.close();}
			catch(IOException e){e.printStackTrace();}
			closed = true;
		}
	}

	@SuppressWarnings("deprecation")
	public void go(){
		//load settings here
		

		Scanner scan = new Scanner(System.in);
		String line;
		do{
			server = new Server(host, port);
			if(server.socket == null){
				System.out.print("Unable to connect. Try again? ");
				line = scan.nextLine();
			}
			else{
				System.out.println("Connected to server");

				thread = new Thread(){@Override public void run(){
					String msg;
					while((msg = server.getMessage()) != null && !msg.equals("close")){
						System.out.println(msg);
					}
					server.close();
					System.out.print("Server closed. Reconnect? ");
				}};
				thread.start();
				
				
				//do stuff (like read input & send direction change messages)
				while(!(line = scan.nextLine()).equals("close") && !server.isClosed()){
					server.sendMessage(line);
				}
				if(line.equals("close")) server.sendMessage("close");
				
				System.out.println("Disconnected from server.");
				thread.stop();
			}
		}while(Boolean.parseBoolean(line));
		scan.close();
	}
	
	public static void main(String[] args){
		new ClientMain().go();
	}
}
