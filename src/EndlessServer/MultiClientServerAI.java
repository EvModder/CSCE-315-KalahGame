package EndlessServer;
import java.util.Random;
import GUI.MultiClientServerWindow;
import GUI.NotGUIManager;
import Main.KalahGame;
import Main.Settings;
import ServerUtils.Connection;
import ServerUtils.MultiServer;
import ServerUtils.MultiServer.ConnectionReceiver;

class MultiClientServerAI implements ConnectionReceiver{
	/* A multi-client version of EndlessServerAI. This version waits for client
	 * connections and starts a new game with each new connection it receives.
	 */
	Random rand = new Random();
	
	public static void main(String... args){new MultiClientServerAI();}
	
	MultiClientServerAI(){
		new MultiClientServerWindow();
		new MultiServer(this);
	}

	@Override
	public void gotConnection(Connection connection){
		new Thread(){@Override public void run(){
			//Randomize settings
			Settings settings = new Settings();
			settings.set("play-as-AI", true);
			settings.set("is-server", true);
			settings.set("holes-per-side", rand.nextInt(8)+3);
			settings.set("seeds-per-hole", rand.nextInt(10)+1);
			settings.set("time-limit", rand.nextInt(49001)+1000);
			settings.set("starting-player", rand.nextBoolean() ? "S" : "F");
			settings.set("game-type", rand.nextBoolean() ? "S" : "R");

			//Start a new game
			new KalahGame(new NotGUIManager(), settings, connection);
		}}.start();
	}
}