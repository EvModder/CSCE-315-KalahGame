package ServerUtils;

public abstract class Connection {
	MessageReceiver receiver;
	
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
