
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
		Scanner scan = new Scanner(System.in);
		String userInput = "";
		int userIndex;
		boolean keepPlaying = true;
		
		while (keepPlaying){
			while (!gameBoard.checkGameOver()){
				/*
				 * Get user input and convert it to an integer. If this throws an NumberFormatException, 
				 * then the user has inputted a string and so we check if it is the option flags for 
				 * reset and instructions. Else print an error message and go to the next iteration of 
				 * the while loop.
				 */
				try{
					System.out.println("\n" + gameBoard + "\n");
					if (gameBoard.getNumSwitches() == 1 && !gameBoard.getHasUsedPieRule())
					{
						System.out.println("Would player 2 like to use a pie rule? Y or N");
						AI2 myAI = new AI2();
						userInput = myAI.checkPie(gameBoard);
						System.out.println("Computer has chosen: " + userInput);
						//userInput = scan.nextLine();
						//userInput = Utils.getTimeLimitedInput(10*1000);
						if (userInput.equals("Y")){
							gameBoard.pieRule();
							continue;
						}
						else if (userInput.equals("N")){
							gameBoard.setHasUsedPieRule(true);
						}
						else if (!userInput.equals("Y") || !userInput.equals("N")){
							continue;
						}
					}
					if (gameBoard.getPlayerTurn()){
						System.out.println("Player 1's Turn: ");
						userInput = scan.nextLine();//Utils.getTimeLimitedInput(10*1000);//10s limit
						userIndex = Integer.parseInt(userInput);
						//TODO: make move
					}
					else{
						/*
						System.out.println("Player 2's Turn: ");
						userInput = Utils.getTimeLimitedInput(10*1000);//10s limit
						userIndex = Integer.parseInt(userInput);
						//TODO: make move*/
						AI2 myAI = new AI2();
						userIndex = myAI.generateMove(gameBoard, 9);
						System.out.println("Computer chose: " + userIndex);
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
				 * Check to see if the index the user inputted is a valid move such that 
				 * they can only select houses they own.
				 */
				if (!gameBoard.validMove(userIndex)){
					System.err.println("Not a valid move: "+userIndex);
					continue;
				}
				
				/*
				 * Distribute seeds according to the house the user selected. If it hits their own 
				 * kalah, then we continue to the next iteration of the while loop. This emulates 
				 * the user getting another turn, as we don't switch the turn to the next user in this case.
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
			 * When the game ends, clean up the board such that there are no more remaining seeds. 
			 * Then display the final board state, the outcome of the game, and the respective scores.
			 */
			gameBoard.collectLeftoverSeeds();
			System.out.println(gameBoard);
			gameBoard.displayOutcome();
			System.out.println("Player 1: " + gameBoard.getNumSeeds(gameBoard.getIndexPlayer1()));
			System.out.println("Player 2: " + gameBoard.getNumSeeds(gameBoard.getIndexPlayer2()));
			
			/*
			 * Prompt the user if they would like to quit the game or replay.
			 */
			
			boolean improper_input = true;
			while (improper_input){
				System.out.println("Enter Replay or Quit");
				userInput = scan.nextLine();
				if (userInput.equals("Quit") || userInput.equals("Replay")){
					improper_input = false;
				}
				else{
					System.out.println("Improper input.");
				}
			}
			if (userInput.equals("Quit")){
				keepPlaying = false;
			}
			else if (userInput.equals("Replay")){
				gameBoard.reset();
			}
		}
		scan.close();
	}
}