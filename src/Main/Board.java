package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Board {
	//These variables should be private, but I don't want to rewrite stuff that's accessing them
	//A circular array representing the squares on the board
	public int[] housesAndKalahs;
	public final int numHouses, kalah2, house02, seedsToTie;
	
	public int kalah1(){return numHouses;}
	public int kalah2(){return kalah2;}
	
	public Board(int numHouses, int numSeeds){
		this.numHouses = numHouses;
		housesAndKalahs = new int[numHouses*2+2];
		kalah2 = housesAndKalahs.length-1;
		house02 = numHouses+1;
		seedsToTie = numSeeds*numHouses;//get this much to tie
		
//		for(int i=0; i<numHouses; ++i) housesAndKalahs[i] = numSeeds;
//		housesAndKalahs[numHouses] = 0;
//		for(int i=numHouses+1; i<kalah2; ++i) housesAndKalahs[i] = numSeeds;
//		housesAndKalahs[kalah2] = 0;
		
		for(int i=0; i<kalah2; ++i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[numHouses] = housesAndKalahs[kalah2] = 0;
	}
	
	//A constructor which takes an array of squares 
	public Board(int numHouses, int seedsToTie, int[] squares){
		this.numHouses = numHouses;
		housesAndKalahs = squares;
		kalah2 = housesAndKalahs.length-1;
		house02 = numHouses+1;
		this.seedsToTie = seedsToTie;
	}
	
	//Given a square index, move the seeds on the board following the game rules
	public int moveSeeds(int from){
		if(from == -1){
			pieRule();
			return -1;
		}
		if(housesAndKalahs[from] > housesAndKalahs.length) return moveSeedsFast(from);
		int numSeeds = housesAndKalahs[from];
		housesAndKalahs[from] = 0;
		
		boolean player1 = from < numHouses;
		
		int i = from;
		while(numSeeds > 0){
			if(++i == housesAndKalahs.length) i = 0;
			
			if(player1){
				if(i == kalah2) continue;
			}
			else{
				if(i == numHouses) continue;
			}
			
			--numSeeds;
			++housesAndKalahs[i];
		}
		
		//capture pieces on the opposite square
		if(housesAndKalahs[i] == 1){
			if(player1){
				if(i < numHouses) captureSeeds(i);
			}
			else{
				if(i > numHouses && i != kalah2) captureSeeds(i);
			}
		}
		return i;
	}
	
	//A faster version of the above method (for seed count > the size of the board)
	private int moveSeedsFast(int from){
		boolean player1 = from < numHouses;
		int each = housesAndKalahs[from] / kalah2;
		int extra = housesAndKalahs[from] % kalah2;
		housesAndKalahs[from] = 0;
		
		int i=from, land=from;
		while(true){
			if(extra == 0 && each == 0) break;
			if(++i == housesAndKalahs.length) i = 0;
			if(player1){
				if(i == kalah2) continue;
			}
			else{
				if(i == numHouses) continue;
			}
			
			housesAndKalahs[i] += each;
			if(extra != 0){
				++housesAndKalahs[i];
				if(--extra == 0) land = i;
			}
			if(i == from) break;
		}
		
		//capture pieces on the opposite square
		if(housesAndKalahs[land] == 1){
			if(player1){
				if(land < numHouses) captureSeeds(land);
			}
			else{
				if(land > numHouses && land != kalah2) captureSeeds(land);
			}
		}
		return land;
	}
	
	private void captureSeeds(int land){
		int capture = getOppositeSquare(land);
		if(housesAndKalahs[capture] == 0) return;
		
		housesAndKalahs[land < numHouses ? numHouses : kalah2]
				+= housesAndKalahs[land] + housesAndKalahs[capture];
		
		housesAndKalahs[land] = housesAndKalahs[capture] = 0;
	}
	
	//Check if a seed movement will lead to a capture
	public boolean willHitKalah(int from){
//		return housesAndKalahs[from] % kalah2 == (from < numHouses ? numHouses-from : kalah2-from);
		return housesAndKalahs[from] % kalah2 == numHouses - (from % house02);
	}
	
	//Remove extra seeds from the board and put them in the Kalahs
	public void collectLeftoverSeeds(){
		for(int i=0; i<numHouses; ++i){
			housesAndKalahs[numHouses] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
		for(int i=house02; i<kalah2; ++i){
			housesAndKalahs[kalah2] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
	}
	
	public int getScoreDifference(){
		return housesAndKalahs[numHouses] - housesAndKalahs[kalah2];
	}
	public int getFinalScoreDifference(){
		for(int i=0; i<numHouses; ++i) housesAndKalahs[numHouses] += housesAndKalahs[i];
		for(int i=house02; i<kalah2; ++i) housesAndKalahs[kalah2] += housesAndKalahs[i];
		return housesAndKalahs[numHouses] - housesAndKalahs[kalah2];
	}
	
	//Check if a given move is valid
	public boolean validMove(int i){
		return i < kalah2 && i != numHouses && housesAndKalahs[i] != 0;
	}
	
	//Randomize the seeds on the board
	void randomizeSeeds(){
		int seedCount = 1;
		for(int i=0; i<numHouses; ++i){
			seedCount += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
		
		Random rand = new Random();
		while(--seedCount > 0){
			++housesAndKalahs[rand.nextInt(numHouses)];
		}
		for(int i=0; i<numHouses; ++i){
			housesAndKalahs[i+house02] = housesAndKalahs[i];
		}
	}
	
	//swap all houses & the two Kalahs
	public void pieRule(){
		for(int i=0; i<=numHouses; ++i){
			int temp = housesAndKalahs[i];
			housesAndKalahs[i] = housesAndKalahs[i+house02];
			housesAndKalahs[i+house02] = temp;
		}
	}
	
	//Returns a deep copy of the board
	public Board getCopy(){
//		int[] squares = new int[housesAndKalahs.length];
//		for(int i=0; i<housesAndKalahs.length; ++i) squares[i] = housesAndKalahs[i];
//		return new Board(numHouses, squares);
		return new Board(numHouses, seedsToTie, Arrays.copyOf(housesAndKalahs, housesAndKalahs.length));
	}
	
	//Checks if the game ISN'T over
	public boolean gameNotOver(){
		boolean noSeeds = true;
		for(int i=0; i<numHouses; ++i) if(housesAndKalahs[i] != 0){
			noSeeds = false;
			break;
		}
		if(noSeeds) return false;
		
		for(int i=house02; i<kalah2; ++i) if(housesAndKalahs[i] != 0){
			return true;
		}
		return false;
	}
	public boolean gameOver(){
		if(housesAndKalahs[numHouses] > seedsToTie || housesAndKalahs[kalah2] > seedsToTie){
			return true;
		}
		for(int i=0; i<numHouses; ++i) if(housesAndKalahs[i] != 0) return false;
		for(int i=house02; i<kalah2; ++i) if(housesAndKalahs[i] != 0) return false;
		return true;
	}
	
	//Returns the difference between the number of seeds on this side and the other side
	public int getSeedDifference(){
		int mySeeds=0, urSeeds=0;
		for(int i=0; i<numHouses; ++i) mySeeds += housesAndKalahs[i];
		for(int i=house02; i<kalah2; ++i) urSeeds += housesAndKalahs[i];
		return mySeeds - urSeeds;
	}

	public int getOppositeSquare(int i){
		return kalah2 - 1 - i;
	}
	
	public int captureValue(int i){
		if(housesAndKalahs[i] == 0 || housesAndKalahs[i] > kalah2){
			return 0;
		}
		if(housesAndKalahs[i] == kalah2){
			return housesAndKalahs[getOppositeSquare(i)] + 3;//1=kalah,1=added to opp, 1=land seed
		}

		int land = housesAndKalahs[i] + i;
		if(i < numHouses){
			if(land < numHouses){
				if(housesAndKalahs[land] != 0)
					return housesAndKalahs[getOppositeSquare(land)]+1;//1=land seed
			}
			else if(land >= kalah2){
				land = housesAndKalahs[i] + i - kalah2;
				if(housesAndKalahs[land] != 0)
					return housesAndKalahs[getOppositeSquare(land)]+2;//1=kalah,1=land seed
			}
		}
		else{
			if(land < kalah2){
				if(housesAndKalahs[land] != 0)
					return housesAndKalahs[getOppositeSquare(land)]+1;//1=land seed
			}
			else{
				land -= kalah2;
				if(land > numHouses){
					if(housesAndKalahs[land] != 0)
						return housesAndKalahs[getOppositeSquare(land)]+2;//1=kalah,1=land seed
				}
			}
		}
		return 0;
	}

	//Get a list of possible moves based on the board's current state
	public List<Integer> getPossibleMoves(boolean player1, long turn){
		int s,e;
		if(player1){s=0; e=numHouses;}
		else{s=house02; e=kalah2;}
		
		List<Integer> moves = new ArrayList<Integer>(numHouses);
		for(int i=s; i<e; ++i) if(housesAndKalahs[i] != 0) moves.add(i);
		if(turn == 2) moves.add(-1);
		return moves;
	}
	
	//Return a list of moves in an order corresponding to their approximate value
	public List<Integer> getPossibleMovesOrdered(boolean player1, int turn){
		int s,e;
		if(player1){s=0; e=numHouses;}
		else{s=house02; e=kalah2;}
		
		LinkedList<Integer> moves = new LinkedList<Integer>();
		if(turn == 2) moves.add(-1);
		for(int i=s; i<e; ++i){
			if(captureValue(i) != 0) moves.add(i);
		}
		for(int i=s; i<e; ++i) if(housesAndKalahs[i] != 0){
			if(willHitKalah(i)) moves.addFirst(i);
			else moves.addLast(i);
		}
		return moves;
	}
}