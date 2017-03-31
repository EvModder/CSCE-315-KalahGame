package EndlessServer;
import java.util.Random;

import Main.Settings;
import ServerUtils.Connection;
import ServerUtils.MultiServer;
import ServerUtils.MultiServer.ConnectionReceiver;

class MultiClientServerAI implements ConnectionReceiver{
	/*
	 * A multi-client version of EndlessServerAI. This version waits for client
	 * connections and starts a new game with each new connection it receives.
	 */
	Random rand = new Random();
	
	public static void main(String... args){
		Settings.changeSetting("play-as-AI", "true");
		
		new MultiServer(new MultiClientServerAI());
	}

	@Override
	public void gotConnection(Connection connection){
		new Thread(){@Override public void run(){
			Settings.changeSetting("holes-per-side", String.valueOf(rand.nextInt(9)+2));
			Settings.changeSetting("seeds-per-hole", String.valueOf(rand.nextInt(10)+1));
			Settings.changeSetting("time-limit", String.valueOf(rand.nextInt(49001)+1000));
			Settings.changeSetting("starting-player", rand.nextBoolean() ? "S" : "F");
			
			KalahNoGUI game = new KalahNoGUI(true, connection);
			while(!game.gameOver) Thread.yield();
			
		}}.start();
	}
}