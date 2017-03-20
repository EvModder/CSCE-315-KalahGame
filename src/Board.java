import java.util.ArrayList;

public class Board {
	int[] positions;
	boolean keepPlaying = true;
	
	private int initHouses;
	private int initSeeds;
	private int kalahPlayer1;
	private int kalahPlayer2;
	private int numHousesAndKalahs;
	
	private boolean player1Turn;
	
	public Board(int numHouses, int numSeeds) {
		this.initHouses = numHouses;
		this.initSeeds = numSeeds;
		
		kalahPlayer1 = numHouses;
		kalahPlayer2 = (numHouses*2)+1;
		
		numHousesAndKalahs = (numHouses*2)+2;
		
		player1Turn = true;
		
		positions = new int[numHousesAndKalahs];
		
		for (int i=0; i<numHousesAndKalahs; i++){
			if (i != kalahPlayer1 || i != kalahPlayer2){
				positions[i] = numSeeds;
			}
		}
	}

	public void displayTitle(){
		System.out.println("Welcome to Kalah!");
	}

	public void instructions(){
		System.out.println("");
	}
	
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
	
	public boolean hitKalah(int index){
		if (player1Turn && index == kalahPlayer1){
			return true;
		}
		else if (!player1Turn && index == kalahPlayer2){
			return true;
		}
		return false;
	}
	
	public boolean hitEmptyHouse(int index){
		if (player1Turn && index >= 0 && index < kalahPlayer1 && positions[index] == 1){
			return true;
		}
		else if (!player1Turn && index > kalahPlayer1 && index < kalahPlayer2 && positions[index] == 1){
			return true;
		}
		return false;
	}
	
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
	
	public void collectLeftoverSeeds(){
		if (player1Turn){
			int firstHouse = kalahPlayer1 + 1;
			int lastHouse = kalahPlayer2;
			for (int i = firstHouse; i<lastHouse; i++){
				positions[kalahPlayer2] += positions[i];
				positions[i] = 0;
			}
		}
		else{
			int firstHouse = 0;
			int lastHouse = kalahPlayer1;
			for (int i = firstHouse; i<lastHouse; i++){
				positions[kalahPlayer1] += positions[i];
				positions[i] = 0;
			}
		}
	}
	
	public void changeTurn(){
		if (player1Turn){
			player1Turn = false;
		}
		else{
			player1Turn = true;
		}
	}
	
	public void reset(){
		for (int i=0; i<numHousesAndKalahs; i++){
			if (i != kalahPlayer1 || i != kalahPlayer2){
				positions[i] = initSeeds;
			}
			else{
				positions[i] = 0;
			}
		}
		player1Turn = true;
	}
	
	public void replayOrQuit(String option){
		if (option == "Quit"){
			keepPlaying = false;
		}
		else if (option == "Replay"){
			for (int i=0; i<numHousesAndKalahs; i++){
				if (i != kalahPlayer1 || i != kalahPlayer2){
					positions[i] = initSeeds;
				}
				else{
					positions[i] = 0;
				}
			}
			player1Turn = true;
		}
		else{
			System.out.println("Not a valid option. Please enter Quit or Replay.");
		}
	}
	
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
	
	public int[] getAllPositions(){
		return positions;
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
