import java.util.*;

public class Kalah{
	private Board gameBoard;
	
	public Kalah(Board input){
		this.gameBoard = input;
	}
	
	public void displayIntro(){
		gameBoard.displayTitle();
		gameBoard.instructions();
	}
	
	public void play(){
		Scanner sc = new Scanner(System.in);
		String userInput = "";
		int userIndex;
		boolean keepPlaying = true;
		
		while (keepPlaying){
			while (!gameBoard.checkGameOver()){
				/*
					Get user input and convert it to an integer. If this throws
					an NumberFormatException, then the user has inputted a string
					and so we check if it is the option flags for reset and instructions.
					Else print an error message and go to the next iteration of the 
					while loop.
				*/
				try{
					System.out.println("\n" + gameBoard + "\n");
					if (gameBoard.getPlayerTurn()){
						System.out.println("Player 1's Turn: ");
						userInput = sc.nextLine();
						userIndex = Integer.parseInt(userInput);
					}
					else{
						System.out.println("Player 2's Turn: ");
						userInput = sc.nextLine();
						userIndex = Integer.parseInt(userInput);
					}
				}
				catch(NumberFormatException error){
					if (userInput.equals("R")){
						gameBoard.reset();
						continue;
					}
					else if (userInput.equals("I")){
						gameBoard.instructions();
						continue;
					}
					else{
						System.err.println("Enter an integer, 'R' for reset, or 'I' for instructions");
						continue;
					}
				}
				
				/* 
					Check to see if the index the user inputted is a valid move such that
					they can only select houses they own.
				*/
				if (!gameBoard.validMove(userIndex)){
					System.err.println("Not a valid move.");
					continue;
				}
				
				/*
					Distribute seeds according to the house the user selected. If it hits their
					own kalah, then we continue to the next iteration of the while loop. This 
					emulates the user getting another turn, as we don't switch the turn to the next
					user in this case.
				*/
				int index = gameBoard.distributeSeeds(userIndex);
				if (gameBoard.hitKalah(index)){
					continue;
				}
				/*
					Check to see if the user hits an empty house they own. If they did, then call 
					the appropriate function to collect seeds from the house opposite of it.
				*/
				else{
					if (gameBoard.hitEmptyHouse(index)){
						gameBoard.captureOppositeSeeds(index);
					}
					gameBoard.changeTurn();
				}
			}
			/*
				When the game ends, clean up the board such that there are no more remaining seeds.
				Then display the final board state, the outcome of the game, and the respective
				scores.
			*/
			gameBoard.collectLeftoverSeeds();
			System.out.println(gameBoard);
			gameBoard.displayOutcome();
			System.out.println("Player 1: " + gameBoard.getNumSeeds(gameBoard.getIndexPlayer1()));
			System.out.println("Player 2: " + gameBoard.getNumSeeds(gameBoard.getIndexPlayer2()));
			
			/*
				Prompt the user if they would like to quit the game or replay.
			*/
			
			boolean improper_input = true;
			while (improper_input){
				System.out.println("Enter Replay or Quit");
				userInput = sc.nextLine();
				if (userInput.equals("Quit") || userInput.equals("Replay")){
					improper_input = false;
					break;
				}
				System.out.println("Improper input.");
			}
			if (userInput.equals("Quit")){
				keepPlaying = false;
			}
			else if (userInput.equals("Replay")){
				gameBoard.reset();
			}
		}
	}
}
