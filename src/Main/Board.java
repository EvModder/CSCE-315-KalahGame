package Main;

import java.util.Random;

public class Board {
	//index [0] is leftmost house of player 1
	public int[] housesAndKalahs;
	public final int numHouses;
	
	public int kalah1(){return numHouses;}
	public int kalah2(){return housesAndKalahs.length-1;}
	
	public Board(int numHouses, int numSeeds){
		this.numHouses = numHouses;
		housesAndKalahs = new int[numHouses*2+2];
		
		for(int i=0; i<numHouses; ++i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[numHouses] = 0;//kalah1
		
		int kalah2 = housesAndKalahs.length-1;
		for(int i=kalah2-1; i>numHouses; --i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[kalah2] = 0;//kalah2
	}
	
	public Board(int numHouses, int[] squares){
		this.numHouses = numHouses;
		housesAndKalahs = squares;
	}
	
	public int moveSeeds(int from){
		int numSeeds = housesAndKalahs[from];
		housesAndKalahs[from] = 0;
		
		boolean player1 = from < numHouses;
		
		int i = from;
		while(numSeeds > 0){
			if(++i == housesAndKalahs.length) i = 0;
			
			if(player1){
				if(i == housesAndKalahs.length-1) continue;
			}
			else{
				if(i == numHouses) continue;
			}
			--numSeeds;
			++housesAndKalahs[i];
		}
		
		//capture pieces on the opposite square
		if(housesAndKalahs[i] == 1 && (
				(player1 && i < numHouses) ||
				(!player1 && i > numHouses && i < housesAndKalahs.length-1)
		)){
			int capture = i + (numHouses - i) * 2;
			if(housesAndKalahs[capture] == 0/* && !doEmptyCapture*/) return i;
			
			int seeds = housesAndKalahs[i] + housesAndKalahs[capture];
			
			if(player1) housesAndKalahs[numHouses] += seeds;
			else housesAndKalahs[numHouses*2+1] += seeds;
			
			housesAndKalahs[i] = 0;
			housesAndKalahs[capture] = 0;
		}
		return i;
	}
	
	public boolean willHitKalah(int from){
		return housesAndKalahs[from] % (housesAndKalahs.length-1)
				== (from < numHouses ? numHouses-from : housesAndKalahs.length-1-from);
	}
	
	public void collectLeftoverSeeds(){
		for(int i=0; i<numHouses; ++i){
			housesAndKalahs[numHouses] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
		for(int i=housesAndKalahs.length-2; i>numHouses; --i){
			housesAndKalahs[housesAndKalahs.length-1] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
	}
	
	public int getScoreDifference(){
		return housesAndKalahs[numHouses] - housesAndKalahs[housesAndKalahs.length-1];
	}
	
	public boolean validMove(int i){
		return i < housesAndKalahs.length && i != kalah1() && i != kalah2() && housesAndKalahs[i] != 0;
	}
	
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
	
	public void pieRule(){
		//swap all houses & the two Kalahs
		for(int i=0; i<=numHouses; ++i){
			int temp = housesAndKalahs[i];
			housesAndKalahs[i] = housesAndKalahs[i+numHouses+1];
			housesAndKalahs[i+numHouses+1] = temp;
		}
	}
	
	public Board getCopy(){
		int[] squares = new int[housesAndKalahs.length];
		for(int i=0; i<housesAndKalahs.length; ++i){
			squares[i] = housesAndKalahs[i];
		}
		return new Board(numHouses, squares);
	}
	
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
	
	public int getSeedDifference(){
		int mySeeds=0, urSeeds=0;
		for(int i=0; i<numHouses; ++i) mySeeds += housesAndKalahs[i];
		for(int i=housesAndKalahs.length-2; i>numHouses; --i) urSeeds += housesAndKalahs[i];
		return mySeeds - urSeeds;
	}
}