package Main;

import java.util.Random;

public class Board {
	//index [0] is leftmost house of player 1
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
	
	public Board(int numHouses, int[] squares){
		this.numHouses = numHouses;
		housesAndKalahs = squares;
		kalah2 = housesAndKalahs.length-1;
	}
	
	public int moveSeedsFast(int from){
		boolean player1 = from < numHouses;
		int each = housesAndKalahs[from] / kalah2;
		int extra = housesAndKalahs[from] % kalah2;
		int land = from+extra;
		int i=from+1, end=i;
		while(true){
			if((player1 && i == kalah2) || (!player1 && i == numHouses)) continue;
			housesAndKalahs[i] += each;
			if(--extra != -1) ++housesAndKalahs[i];
			if(++i == end) break;
		}
		if(housesAndKalahs[land] == 1 &&
				((player1 && land < numHouses) || (!player1 && land > numHouses && land < kalah2)))
		{
			captureSeeds(land);
		}
		return land;
	}
	
	public int moveSeeds(int from){
		if(from == -1){
			pieRule();
			return -1;
		}
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
				((player1 && i < kalah1()) || (!player1 && i > kalah1() && i < kalah2())))
		{
			captureSeeds(i);
		}
		return i;
	}
	
	private void captureSeeds(int land){
		boolean player1 = land < numHouses;
		
		int capture = housesAndKalahs.length - 2 - land;
		if(housesAndKalahs[capture] == 0/* && !doEmptyCapture*/) return;
		
		int seeds = housesAndKalahs[land] + housesAndKalahs[capture];
		
		if(player1) housesAndKalahs[kalah1()] += seeds;
		else housesAndKalahs[kalah2()] += seeds;
		
		housesAndKalahs[land] = housesAndKalahs[capture] = 0;
	}
	
	public boolean willHitKalah(int from){
		return housesAndKalahs[from] % (housesAndKalahs.length-1)
				== (from < numHouses ? kalah1()-from : kalah2()-from);
	}
	
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