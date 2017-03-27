public class Board{
	/*
	 * Class members
	 */
	private int[] positions;
	private int initHouses;
	private int initSeeds;
	private int kalahPlayer1;
	private int kalahPlayer2;
	private int numHousesAndKalahs;
	private boolean player1Turn;
	
	private boolean hasUsedPieRule = false;
	private int numSwitches = 0;
	private int[] initRandomSeeds;
	private boolean random = false;
	
	/*
	 * Constructor with number of houses and seeds provided
	 */
	public Board(int numHouses, int numSeeds){
		this.initHouses = numHouses;
		this.initSeeds = numSeeds;
		
		kalahPlayer1 = numHouses;
		kalahPlayer2 = (numHouses*2)+1;
		
		numHousesAndKalahs = (numHouses*2)+2;
		
		player1Turn = true;
		
		positions = new int[numHousesAndKalahs];
		
		for (int i=0; i<numHousesAndKalahs; i++){
			if (i == kalahPlayer1 || i == kalahPlayer2){
				positions[i] = 0;
			}
			else{
				positions[i] = numSeeds;
			}
		}
	}
	
	/*
	 * Constructor with an array of random seeds.
	 */
	public Board(int[] randomSeeds){
		this.initHouses = randomSeeds.length;
		this.initRandomSeeds = randomSeeds;
		
		kalahPlayer1 = initHouses;
		kalahPlayer2 = (initHouses*2)+1;
		
		numHousesAndKalahs = (initHouses*2)+2;
		
		player1Turn = true;
		
		positions = new int[numHousesAndKalahs];
		
		for (int i=0; i<initHouses; i++){
			positions[i] = randomSeeds[i];
			positions[i + initHouses + 1] = randomSeeds[i];
		}
		
		positions[kalahPlayer1] = 0;
		positions[kalahPlayer2] = 0;
		
		random = true;
	}

	public void displayTitle(){
		System.out.println("Welcome to Kalah!");
	}

	public void instructions(){
		System.out.println("The objective of the game is to have the most seeds in your 'Kalah' by the end of the game. "
							+ "You and your oppenent take turns to move the seeds according to the following rules. \n"
							+ "1. You can only move the seeds on your side of the Kalah board. \n"
							+ "2. The seeds will move in a counter-clockwise direction and be placed in the next house or the player's kalah respectively. \n"
							+ "3. To move, player 1 selects a non-empty house from 0-5, and player 2 selects a non-empty house from 7-12. \n"
							+ "4. If the last seed lands on your Kalah, you get to go again. \n"
							+ "5. If the last seed lands on an empty house on your side, you get all the seeds from your opponent's house that is directly opposite from yours. \n"
							+ "*** To Reset a game type in 'R' and to get the instructions, type in 'I' *** \n");
	}
	
	/*
	 * A valid move is index 0-5 for player 1, 7-12 for player 2 (with 6,4 config)
	 */
	public boolean validMove(int index){
		if (player1Turn){
			if ((index >= 0) && (index < kalahPlayer1)){
				if (positions[index] > 0){
					return true;
				}
			}
		}
		else{
			if ((index > kalahPlayer1) && (index < kalahPlayer2)){
				if (positions[index] > 0){
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * First get the number of seeds to distribute, then go around the board
	 * and increment all houses and the kalah the player owns. We use the modulus 
	 * operator to wrap around after we hit the max amount of houses and kalahs.
	 */
	public int distributeSeeds(int index){
		int toDistribute = positions[index];
		positions[index] = 0;
		
		while (toDistribute > 0){
			index = (index + 1) % numHousesAndKalahs;
			if (player1Turn && index != kalahPlayer2){
				positions[index] += 1;
				toDistribute--;
			}
			else if (!player1Turn && index != kalahPlayer1){
				positions[index] += 1;
				toDistribute--;
			}
		}
		return index;
	}
	
	/*
	 * Checks to see if the last seed lands in the current player's kalah.
	 */
	public boolean hitKalah(int index){
		if (player1Turn && index == kalahPlayer1){
			return true;
		}
		else if (!player1Turn && index == kalahPlayer2){
			return true;
		}
		return false;
	}
	
	/*
	 * Checks to see if the current player lands the last seed into an empty house they own.
	 */
	public boolean hitEmptyHouse(int index){
		if (player1Turn && index >= 0 && index < kalahPlayer1 && positions[index] == 1){
			return true;
		}
		else if (!player1Turn && index > kalahPlayer1 && index < kalahPlayer2 && positions[index] == 1){
			return true;
		}
		return false;
	}
	
	/*
	 * This function is called whenever hitEmptyHouse returns true. It will get the amount of seeds 
	 * of the opposite house to the empty house and add it to the correct player's kalah.
	 */
	public void captureOppositeSeeds(int index){
		int oppositeIndex = (numHousesAndKalahs - 2) - index;
		
		if (positions[oppositeIndex] > 0){
			if (player1Turn){
				positions[kalahPlayer1] = positions[kalahPlayer1] + positions[index] + positions[oppositeIndex];
				positions[index] = 0;
				positions[oppositeIndex] = 0;
			}
			else{
				positions[kalahPlayer2] = positions[kalahPlayer2] + positions[index] + positions[oppositeIndex];
				positions[index] = 0;
				positions[oppositeIndex] = 0;
			}
		}
	}
	
	/*
	 * The game is over when either of the players' rows are empty.
	 */
	public boolean checkGameOver(){
		boolean housesEmptyPlayer1 = true;
		boolean housesEmptyPlayer2 = true;
		int firstHouse;
		int lastHouse;
		
		firstHouse = 0;
		lastHouse = kalahPlayer1;
		for (int i = firstHouse; i<lastHouse; i++){
			if (positions[i] != 0){
				housesEmptyPlayer1 = false;
				break;
			}
		}
		
		firstHouse = kalahPlayer1 + 1;
		lastHouse = kalahPlayer2;
		for (int i = firstHouse; i<lastHouse; i++){
			if (positions[i] != 0){
				housesEmptyPlayer2 = false;
				break;
			}
		}
		
		return (housesEmptyPlayer1 || housesEmptyPlayer2);
	}
	
	/*
	 * This function is called when the game is finished to correctly distribute seeds 
	 * that are still on the board when one player's side has completely no seeds.
	 */
	public void collectLeftoverSeeds(){
		int firstHouse = 0;
		int lastHouse = kalahPlayer1;
		for (int i = firstHouse; i<lastHouse; i++){
			positions[kalahPlayer1] += positions[i];
			positions[i] = 0;
		}
		
		int firstHouse2 = kalahPlayer1 + 1;
		int lastHouse2 = kalahPlayer2;
		for (int i = firstHouse2; i<lastHouse2; i++){
			positions[kalahPlayer2] += positions[i];
			positions[i] = 0;
		}
	}
	
	public void changeTurn(){
		if (player1Turn){
			player1Turn = false;
		}
		else{
			player1Turn = true;
		}
		numSwitches++;
	}
	
	/*
	 * Resets the game board to the original state.
	 */
	public void reset(){
		if (random){
			for (int i=0; i<initHouses; i++){
				positions[i] = initRandomSeeds[i];
				positions[i + initHouses + 1] = initRandomSeeds[i];
			}
			positions[kalahPlayer1] = 0;
			positions[kalahPlayer2] = 0;
			player1Turn = true;
			numSwitches = 0;
			hasUsedPieRule = false;
		}
		else{
			for (int i=0; i<numHousesAndKalahs; i++){
				if (i == kalahPlayer1 || i == kalahPlayer2){
					positions[i] = 0;
				}
				else{
					positions[i] = initSeeds;
				}
			}
			player1Turn = true;
			numSwitches = 0;
			hasUsedPieRule = false;
		}
	}
	
	public void pieRule(){
		if (numSwitches != 1){
			System.out.println("Cannot use pie rule here. Player one's first turn has either not begun or is passed.");
		}
		else{
			int[] tempArr = new int[initHouses];
			for (int i=0; i<initHouses; i++){
				tempArr[i] = positions[i];
			}
			
			for (int i=0; i<initHouses; i++){
				positions[i] = positions[i + initHouses + 1];
			}
			
			int index = 0;
			for (int i=kalahPlayer1+1; i<kalahPlayer2; i++){
				positions[i] = tempArr[index];
				index++;
			}
			int tempKalahP1 = positions[kalahPlayer1];
			positions[kalahPlayer1] = positions[kalahPlayer2];
			positions[kalahPlayer2] = tempKalahP1;
			player1Turn = true;
		}
		hasUsedPieRule = true;
	}
	
	/*
	 * Compares the value of seeds in the two player's kalahs.
	 */
	public void displayOutcome(){
		if (positions[kalahPlayer1] > positions[kalahPlayer2]){
			System.out.println("Player 1 is the winner.");
		}
		else if (positions[kalahPlayer1] < positions[kalahPlayer2]){
			System.out.println("Player 2 is the winner.");
		}
		else{
			System.out.println("There is a tie.");
		}
	}
	
	public int getNumSeeds(int index){
		return positions[index];
	}
	
	public int getIndexPlayer1(){
		return kalahPlayer1;
	}
	
	public int getIndexPlayer2(){
		return kalahPlayer2;
	}
	
	public boolean getPlayerTurn(){
		return player1Turn;
	}
	
	public int getNumSwitches(){
		return numSwitches;
	}
	
	public boolean getHasUsedPieRule(){
		return hasUsedPieRule;
	}
	
	public void setHasUsedPieRule(boolean newCondition){
		hasUsedPieRule = newCondition;
	}
	
	public String toString(){
		String output = "\t";
		for (int i = kalahPlayer2 - 1; i>kalahPlayer1; i--){
			output += positions[i] + " ";
		}
		output += "\n";
		output += positions[kalahPlayer2] + "\t\t\t"+ positions[kalahPlayer1] + "\n\t";
		for (int i=0; i<kalahPlayer1; i++){
			output += positions[i] + " ";
		}
		return output;
	}
}