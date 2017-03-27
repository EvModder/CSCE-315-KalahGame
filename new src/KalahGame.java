
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ServerUtils.*;
import ServerUtils.Connection.MessageReceiver;

class KalahGame implements MessageReceiver{
	private BoardFrame board;
	public static int move;
	public static boolean waitingForMove=true;
	
	Connection connection;
	boolean waitingForInfo=true, waitingForReady=true, waitingForOK=true, waitingForYourMove=true;
	boolean isServer, myTurn, gameOver, illegal;
	
	JFrame source;
	public KalahGame(boolean isServer, JFrame source){
		this.isServer = isServer;
		this.source = source;
		
		//open wait for clients window <<<<<<<<<<<<<<<<<
		source.setVisible(false);
		
		connection = isServer ? new ServerMain(this) : new ClientMain(this);
		
		//Main game thread
		new Thread(){
		@Override public void run(){
			if(!connection.isClosed()){
				JOptionPane.showMessageDialog(null, "Error - Unable to connect!",
							"Connection issue", JOptionPane.ERROR_MESSAGE);
				source.setVisible(true);
				return;
			}
			if(isServer){
				//load settings
				Map<String,String> settings = FileIO.loadYaml("settings.yml",
								getClass().getResourceAsStream("/settings.yml"));
				
				int houses = Integer.parseInt(settings.get("num-houses"));
				int seeds = Integer.parseInt(settings.get("num-seeds"));
				String first = settings.get("starting-player");
				String type = settings.get("game-type");
				board = new BoardFrame(houses, seeds);
				myTurn = !first.equals("F");
				
				//print INFO
				connection.println("WELCOME");
				connection.println("INFO "+houses+" "+seeds+" "+first+" "+type);
				
				while(waitingForReady) yield();//wait for client to be ready
			}
			else{
				while(waitingForInfo) yield();//wait for INFO
				connection.println("READY");
			}
//			if(!myTurn) board.disableButtons();
			//close wait window here, set board visible <<<<<<<<<<<<<<<<<
			board.setVisible(true);
			
			System.out.println("Starting game!");
			
			while(gameNotOver()){
				//if it is my turn
				if(myTurn){
					System.out.println("My turn!");
					board.enableButtons();
					StringBuilder message = new StringBuilder("");
					//wait for this player to move, make moves as long as they hit their Kalah
					while(waitingForMove && !gameOver){
						while(waitingForMove && !gameOver) yield();
						message.append(move+board.numHouses+1);
						if(board.moveSeeds(move) == board.numHouses){
							message.append(' ');
							waitingForMove = true;
						}
					}
					waitingForMove = true;
					board.disableButtons();
					
					connection.println(message.toString());
					
					while(waitingForOK && !gameOver) yield();//wait for opponent to confirm move
					waitingForOK = true;
					myTurn = false;
				}
				else{
					System.out.println("Waiting for opponent");
					while(waitingForYourMove && !gameOver) yield();//wait for opponent
					waitingForYourMove = true;
					myTurn = true;
				}
			}
			if(isServer){
				board.collectLeftoverSeeds();
				if(illegal || board.isWinning()){
					connection.println("LOSER");
					JOptionPane.showMessageDialog(null, "You win!");
				}
				else{
					connection.println("WINNER");
					JOptionPane.showMessageDialog(null, "You lose!");
				}
				gameOver = true;
			}
			else{
				while(!gameOver) yield();//wait for results
			}
			
//			System.out.println("Score1 = "+board.housesAndKalahs[board.numHouses]);
//			System.out.println("Score2 = "+board.housesAndKalahs[board.numHouses*2+1]);
			
			connection.close();
			board.setVisible(false);
			board.dispose();
			source.setVisible(true);
		}}.start();
	}
	
	boolean gameNotOver(){
		if(gameOver) return false;
		
		int i=0, len = board.numHouses;
		boolean noSeeds = true;
		for(; i<len; ++i) if(board.housesAndKalahs[i].getSeeds() != 0){
			noSeeds = false;
			break;
		}
		if(noSeeds) return false;
		
		len += len + 1; ++i;
		for(; i<len; ++i) if(board.housesAndKalahs[i].getSeeds() != 0){
			noSeeds = false;
			break;
		}
		return !noSeeds;
	}
	
	
	//---------- I/O ----------------------------------------------------
	public boolean parseServerMessage(String... args){
		if(args[0].equals("WELCOME")){
			System.out.println("Welcomed to server");
		}
		else if(args[0].equals("INFO")){// INFO 4 1 5000 F S
			
			board = new BoardFrame(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			myTurn = args[3].equals("F"); //gameType = args[3]
			waitingForInfo = false;
		}
		else if(args[0].equals("LOSER")){
			JOptionPane.showMessageDialog(null, "You lose!");
			gameOver = true;
		}
		else if(args[0].equals("WINNER")){
			JOptionPane.showMessageDialog(null, "You win!");
			gameOver = true;
		}
		else if(args[0].equals("TIE")){
			JOptionPane.showMessageDialog(null, "The game was a tie.");
			gameOver = true;
		}
		else if(args[0].equals("TIME")){
			JOptionPane.showMessageDialog(null, "You timed out on your move.");
			gameOver = true;
		}
		else if(args[0].equals("ILLEGAL")){
			//wth server! I was playing fair :(
		}
		else return false;
		return true;
	}
	
	public boolean parseClientMessage(String... args){
		if(args[0].equals("READY")){
			waitingForReady = false;
		}
		else return false;
		return true;
	}
	
	@Override
	public void receiveMessage(String message) {
//		System.out.println("Received message: "+message); 
		boolean unableToParse = false;
		
		String[] args = message.split(" ");
		
		if(args[0].matches("^\\d+$")){
			int land = board.housesAndKalahs.length-1;
			for(String str : args){
				int move = Integer.parseInt(str);
				if(!board.validMove(move) || land != board.housesAndKalahs.length-1){
					if(isServer){
						System.out.println("land="+land);
						connection.println("ILLEGAL");
						gameOver = true;
					}
					return;
				}
				land = board.moveSeeds(move);
			}
			waitingForYourMove = false;
			connection.println("OK");
		}
		else if(args[0].equals("OK")){
			waitingForOK = false;
		}
		else if(isServer) unableToParse = !parseClientMessage(args);
		else unableToParse = !parseServerMessage(args);
		
		if(unableToParse){
			System.out.println("Unable to parse message!");
		}
	}
}