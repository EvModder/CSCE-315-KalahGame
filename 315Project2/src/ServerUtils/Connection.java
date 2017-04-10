package ServerUtils;

//An abstract representation of a network connection (either a server of a client)
public abstract class Connection {
	MessageReceiver receiver;
	
	//A receiver to send incoming messages to
	public interface MessageReceiver{
		void receiveMessage(String message);
	}
	
	public Connection(MessageReceiver rec){
		receiver = rec;
	}
	
	public final void setReceiver(MessageReceiver rec){
		receiver = rec;
	}
	
	public abstract boolean isClosed();
	
	public abstract void close();
	
	public abstract void println(String message);
}
