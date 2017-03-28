

import java.util.Map;
import javax.swing.JOptionPane;
import AIs.*;
import ServerUtils.*;
import ServerUtils.Connection.MessageReceiver;

class KalahGame implements MessageReceiver{
	private BoardFrame board;
	public static int move;
	public static boolean waitingForMove=true;
	
	Connection connection;
	boolean waitingForInfo=true, waitingForReady=true, waitingForOK=true, waitingForYourMove=true;
	boolean isServer, myTurn, gameOver, playAsAI;
	int pieRuleChooser, timeLimit;
	
	public KalahGame(boolean isServer){
		this.isServer = isServer;
		
		Utils.showWaitingWindow();
		Utils.menuFrame.setVisible(false);
		
		//Main game thread
		new Thread(){@Override public void run(){
			connection = isServer ? new ServerMain(KalahGame.this) : new ClientMain(KalahGame.this);
			
			if(!connection.isClosed()){
				JOptionPane.showMessageDialog(null, "Error - Unable to connect!",
							"Connection issue", JOptionPane.ERROR_MESSAGE);
				Utils.menuFrame.setVisible(true);
				Utils.closeWaitingWindow();
				return;
			}
			//load settings
			Map<String,String> settings = Utils.getSettings();
			playAsAI = Boolean.parseBoolean(settings.get("play-as-AI"));
			AI ai = new DumbAI();
			
			if(isServer){
				int houses = Integer.parseInt(settings.get("holes-per-side"));
				int seeds = Integer.parseInt(settings.get("seeds-per-hole"));
				int timeLimit = Integer.parseInt(settings.get("time-limit"));
				String first = settings.get("starting-player");
				String type = settings.get("game-type");
				board = new BoardFrame(houses, seeds);
				myTurn = !first.equals("F");
				
				//print INFO
				connection.println("WELCOME");
				StringBuilder builder = new StringBuilder("INFO ").append(houses).append(' ')
						.append(seeds).append(' ')
						.append(timeLimit).append(' ')
						.append(first).append(' ').append(type);
				
				if(type.equals("R")){
					//randomize board, then send it to client
					board.randomizeSeeds();
					for(int i=0; i<board.numHouses; ++i){
						builder.append(' ').append(board.housesAndKalahs[i].getSeeds());
					}
				}
				
				connection.println(builder.toString());
				
				while(waitingForReady) yield();//wait for client to be ready
			}
			else{
				while(waitingForInfo) yield();//wait for INFO
				connection.println("READY");
			}
			board.setVisible(true);
			Utils.closeWaitingWindow();
			
			System.out.println("Starting game!");
			pieRuleChooser = myTurn ? 2 : 1;
			
			while(board.gameNotOver() && !gameOver){
				//if it is my turn
				if(myTurn){
					//TODO: time my own move. If I timeout, make myself lose.
//					System.out.println("Waiting for myself to move");
					
					if(pieRuleChooser == 1){
						pieRuleChooser = 0;
						if((!playAsAI && JOptionPane.showConfirmDialog(null,
								"Do you want to take the pie rule?",
								"Pie Rule Decision",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
							|| (playAsAI && ai.doPieRule(board.getSquaresAsInts(), timeLimit))){
							
							connection.println("P");
							board.pieRule();
							//wait for opponent to confirm move
							while(waitingForOK && !gameOver) yield();
							waitingForOK = true;
							myTurn = false;
							continue;
						}
					}
					
					StringBuilder message = new StringBuilder("");
					if(playAsAI){//get ai move
						for(int move : ai.getMove(board.getSquaresAsInts(), timeLimit)){
							message.append(move+1);
							if(board.moveSeeds(move) == board.numHouses) message.append(' ');
						}
					}
					else{//get player move
						board.enableButtons();
						//wait for this player to move, make moves as long as they hit their Kalah
						while(waitingForMove && !gameOver){
							while(waitingForMove && !gameOver) yield();
							message.append(move+1);
							if(board.moveSeeds(move) == board.numHouses && board.gameNotOver()){
								message.append(' ');
								waitingForMove = true;
							}
						}
						waitingForMove = true;
						board.disableButtons();
					}
					//send move
					connection.println(message.toString());
					
					while(waitingForOK && !gameOver) yield();//wait for opponent to confirm move
					waitingForOK = true;
					myTurn = false;
				}
				else{
					//TODO: if I am the server, time the opponent's move, if they timeout make them lose.
					//System.out.println("Waiting for opponent to move");
					
					while(waitingForYourMove && !gameOver) yield();//wait for opponent
					waitingForYourMove = true;
					if(pieRuleChooser == 2) pieRuleChooser = 0;
					myTurn = true;
				}
			}
			//if I am the server and the game needs to be ended, end it naturally
			if(isServer && !gameOver){
				if(Boolean.parseBoolean(Utils.getSettings().get("count-leftovers"))){
					board.collectLeftoverSeeds();
				}
				int score = board.getScoreDifference();
				if(score > 0){
					connection.println("LOSER");
					connection.close();
					JOptionPane.showMessageDialog(null,"You win!","Game Over",JOptionPane.DEFAULT_OPTION);
				}
				else if(score == 0){
					connection.println("TIE");
					connection.close();
					JOptionPane.showMessageDialog(null, "The game was a tie.", "Game Over",
							JOptionPane.DEFAULT_OPTION);
				}
				else{
					connection.println("WINNER");
					connection.close();
					JOptionPane.showMessageDialog(null,"You lose!","Game Over",JOptionPane.DEFAULT_OPTION);
				}
			}
			else{
				while(!gameOver) yield();//wait for results
				connection.close();
			}
			
//			System.out.println("Score1 = "+board.housesAndKalahs[board.numHouses]);
//			System.out.println("Score2 = "+board.housesAndKalahs[board.numHouses*2+1]);
			
			board.setVisible(false);
			board.dispose();
			Utils.menuFrame.setVisible(true);
		}}.start();
	}
	
	
	//---------- I/O ----------------------------------------------------
	public boolean parseServerMessage(String... args){
		if(args[0].equals("WELCOME")){
			System.out.println("Welcomed to server");
		}
		else if(args[0].equals("INFO")){// INFO 4 1 5000 F S
			
			board = new BoardFrame(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			timeLimit = Integer.parseInt(args[3]);
			myTurn = args[4].equals("F");
			if(args[5].equals("R")){
				for(int i=0; i<board.numHouses; ++i){
					int seeds = Integer.parseInt(args[i+6]);
					board.housesAndKalahs[i].setSeeds(seeds);
					board.housesAndKalahs[i+board.numHouses+1].setSeeds(seeds);
				}
			}
			waitingForInfo = false;
		}
		else if(args[0].equals("LOSER")){
			JOptionPane.showMessageDialog(null,"You lose!","Game Over",JOptionPane.DEFAULT_OPTION);
			gameOver = true;
		}
		else if(args[0].equals("WINNER")){
			JOptionPane.showMessageDialog(null,"You win!","Game Over",JOptionPane.DEFAULT_OPTION);
			gameOver = true;
		}
		else if(args[0].equals("TIE")){
			JOptionPane.showMessageDialog(null,"The game was a tie.","Game Over",JOptionPane.DEFAULT_OPTION);
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
				int move = Integer.parseInt(str)+board.numHouses;
				if(!board.validMove(move) || land != board.housesAndKalahs.length-1){
					//They moved more times than they should have
					if(isServer) connection.println("ILLEGAL\nLOSER");
					gameOver = true;
					return;
				}
				land = board.moveSeeds(move);
			}
			if(land == board.housesAndKalahs.length-1 && board.gameNotOver()){
				//They stopped sending moves sooner they should have
				if(isServer) connection.println("ILLEGAL\nLOSER");
				gameOver = true;
				return;
			}
			waitingForYourMove = false;
			connection.println("OK");
		}
		else if(args[0].equals("P")){
			if(pieRuleChooser == 2){
				board.pieRule();
				waitingForYourMove = false;
				connection.println("OK");
			}
			else if(isServer) connection.println("ILLEGAL\nLOSER");
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