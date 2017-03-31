package EndlessServer;
import java.util.Random;
import Main.Settings;

class ServerAI{
	/* An infinite server that waits for a client, plays against it using an,
	 * AI, and then closes the game and starts waiting for the next client
	 */
	public static void main(String... args){
		Random rand = new Random();
		KalahNoGUI game;
		Settings.changeSetting("play-as-AI", "true");
		
		while(true){
			//Randomize settings
			Settings.changeSetting("holes-per-side", String.valueOf(rand.nextInt(9)+2));
			Settings.changeSetting("seeds-per-hole", String.valueOf(rand.nextInt(10)+1));
			Settings.changeSetting("time-limit", String.valueOf(rand.nextInt(49001)+1000));
			Settings.changeSetting("starting-player", rand.nextBoolean() ? "S" : "F");
			
			//Start a new game
			game = new KalahNoGUI(true);
			while(!game.gameOver) Thread.yield();

//			//Attempt to close all the windows (GameOverWindow, MenuWindow, etc)
//			for(Window window : Window.getWindows()) window.dispose();
//			try{Thread.sleep(1000);}catch(InterruptedException e){}
//			for(Window window : Window.getWindows()) window.dispose();
//			try{Thread.sleep(1000);}catch(InterruptedException e){}
//			for(Window window : Window.getWindows()) window.dispose();
		}
	}
}