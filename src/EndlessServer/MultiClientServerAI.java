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
	public static void main(String... args){new MultiClientServerAI();}
	/* A multi-client version of EndlessServerAI. This version waits for client
	 * connections and starts a new game with each new connection it receives.
	 */
	Random rand = new Random();
	MultiClientServerWindow gui;
	Settings settings = new Settings();
	
	MultiClientServerAI(){
		gui = new MultiClientServerWindow();
		new MultiServer(this);
	}

	@Override
	public void gotConnection(Connection connection){
		new Thread(){@Override public void run(){
			settings.set("use-GUI", false);
			settings.set("is-server", true);
			settings.set("AI-name", "MinMaxAI");
			
			//Randomize settings
			settings.set("holes-per-side", rand.nextInt(10)+3);
			settings.set("seeds-per-hole", rand.nextInt(16)+1);
			settings.set("time-limit", rand.nextInt(8000)+2000);
			settings.set("starting-player", rand.nextBoolean() ? "S" : "F");
			settings.set("game-type", rand.nextBoolean() ? "S" : "R");

			//Start a new game
			KalahGame game = new KalahGame(new NotGUIManager(), settings, connection);
			while(!game.isGameOver()) Thread.yield();
			gui.updateScore(game.iWon());
		}}.start();
	}
}