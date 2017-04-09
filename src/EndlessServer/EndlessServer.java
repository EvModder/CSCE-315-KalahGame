package EndlessServer;
import java.util.Random;

import GUI.NotGUIManager;
import Main.KalahGame;
import Main.Settings;

class EndlessServer{
	/* An infinite server that waits for a client, plays against it using an,
	 * AI, and then closes the game and starts waiting for the next client
	 */
	public static void main(String... args){
		Random rand = new Random();
		Settings settings = new Settings();
		KalahGame game;
		settings.set("AI-name", "MinMaxAIFinal");
		settings.set("use-GUI", false);
		settings.set("is-server", true);
		
		while(true){
			//Randomize settings
			settings.set("holes-per-side", rand.nextInt(10)+3);
			settings.set("seeds-per-hole", rand.nextInt(20)+1);
			settings.set("time-limit", rand.nextInt(10001)+800);
			settings.set("starting-player", rand.nextBoolean() ? "S" : "F");
			settings.set("game-type", rand.nextBoolean() ? "S" : "R");
//			
			//Start a new game
			game = new KalahGame(new NotGUIManager(), settings);
			while(!game.isGameOver()) Thread.yield();
		}
	}
}