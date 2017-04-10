package Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Board {
	//A circular array representing the squares on the board
	public int[] housesAndKalahs;
	public final int numHouses, kalah2;
	
	public int kalah1(){return numHouses;}
	public int kalah2(){return kalah2;}
	
	public Board(int numHouses, int numSeeds){
		this.numHouses = numHouses;
		housesAndKalahs = new int[numHouses*2+2];
		kalah2 = housesAndKalahs.length-1;
		
		for(int i=0; i<numHouses; ++i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[kalah1()] = 0;
		
		for(int i=kalah2()-1; i>numHouses; --i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[kalah2()] = 0;
	}
	
	//A constructor which takes an array of squares 
	public Board(int numHouses, int[] squares){
		this.numHouses = numHouses;
		housesAndKalahs = squares;
		kalah2 = housesAndKalahs.length-1;
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
			
			if((player1 && i == kalah2()) || (!player1 && i == kalah1())) continue;
			
			--numSeeds;
			++housesAndKalahs[i];
		}
		
		//capture pieces on the opposite square
		if(housesAndKalahs[i] == 1 &&
				((player1 && i < kalah1()) || (!player1 && i > kalah1() && i < kalah2)))
		{
			captureSeeds(i);
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
			if((player1 && i == kalah2) || (!player1 && i == numHouses)) continue;
			
			housesAndKalahs[i] += each;
			if(extra != 0){
				++housesAndKalahs[i];
				if(--extra == 0){land = i;}
			}
			if(i == from) break;
		}
		if(housesAndKalahs[land] == 1 &&
				((player1 && land < numHouses) || (!player1 && land > numHouses && land < kalah2)))
		{
			captureSeeds(land);
		}
		return land;
	}
	
	private void captureSeeds(int land){
		boolean player1 = land < numHouses;
		
		int capture = housesAndKalahs.length - 2 - land;
		if(housesAndKalahs[capture] == 0) return;
		
		int seeds = housesAndKalahs[land] + housesAndKalahs[capture];
		
		if(player1) housesAndKalahs[kalah1()] += seeds;
		else housesAndKalahs[kalah2()] += seeds;
		
		housesAndKalahs[land] = housesAndKalahs[capture] = 0;
	}
	
	//Check if a seed movement will lead to a capture
	public boolean willHitKalah(int from){
		return housesAndKalahs[from] % kalah2
				== (from < numHouses ? numHouses-from : kalah2-from);
	}
	
	//Remove extra seeds from the board and put them in the Kalahs
	public void collectLeftoverSeeds(){
		for(int i=0; i<numHouses; ++i){
			housesAndKalahs[kalah1()] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
		for(int i=housesAndKalahs.length-2; i>numHouses; --i){
			housesAndKalahs[kalah2()] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
	}
	
	public int getScoreDifference(){
		return housesAndKalahs[numHouses] - housesAndKalahs[housesAndKalahs.length-1];
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
			housesAndKalahs[i+numHouses+1] = housesAndKalahs[i];
		}
	}
	
	//swap all houses & the two Kalahs
	public void pieRule(){
		for(int i=0; i<=numHouses; ++i){
			int temp = housesAndKalahs[i];
			housesAndKalahs[i] = housesAndKalahs[i+numHouses+1];
			housesAndKalahs[i+numHouses+1] = temp;
		}
	}
	
	//Returns a deep copy of the board
	public Board getCopy(){
		int[] squares = new int[housesAndKalahs.length];
		for(int i=0; i<housesAndKalahs.length; ++i){
			squares[i] = housesAndKalahs[i];
		}
		return new Board(numHouses, squares);
	}
	
	//Checks if the game ISN'T over
	public boolean gameNotOver(){
		boolean noSeeds = true;
		for(int i=0; i<numHouses; ++i) if(housesAndKalahs[i] != 0){
			noSeeds = false;
			break;
		}
		if(noSeeds) return false;
		
		for(int i=housesAndKalahs.length-2; i>numHouses; --i) if(housesAndKalahs[i] != 0){
			return true;
		}
		return false;
	}
	
	//Returns the difference between the number of seeds on this side and the other side
	public int getSeedDifference(){
		int mySeeds=0, urSeeds=0;
		for(int i=0; i<numHouses; ++i) mySeeds += housesAndKalahs[i];
		for(int i=housesAndKalahs.length-2; i>numHouses; --i) urSeeds += housesAndKalahs[i];
		return mySeeds - urSeeds;
	}
	
	//Get a list of possible moves based on the board's current state
	public List<Integer> getPossibleMoves(boolean player1, long turn){
		int s,e;
		if(player1){s=0; e=numHouses;}
		else{s=numHouses+1; e=kalah2;}
		
		List<Integer> moves = new ArrayList<Integer>(numHouses);
		for(int i=s; i<e; ++i) if(housesAndKalahs[i] != 0) moves.add(i);
		if(turn == 2) moves.add(-1);
		return moves;
	}
	
	//Return a list of moves in an order corresponding to their approximate value
	public List<Integer> getPossibleMovesOrdered(boolean player1, long turn){
		int s,e;
		if(player1){s=0; e=numHouses;}
		else{s=numHouses+1; e=kalah2;}
		
		LinkedList<Integer> moves = new LinkedList<Integer>();
		if(turn == 2) moves.add(-1);
		for(int i=s; i<e; ++i) if(housesAndKalahs[i] != 0){
			if(willHitKalah(i)) moves.addFirst(i);
			else moves.addLast(i);
		}
		return moves;
	}
}