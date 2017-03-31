package EndlessServer;

import java.util.Random;
import Main.Settings;

class BoardNoGUI {
	//index [0] is leftmost house of player 1
	int[] housesAndKalahs;
	final int numHouses;
	final boolean doEmptyCapture;
	
	BoardNoGUI(int numHouses, int numSeeds){
		this.numHouses = numHouses;
		housesAndKalahs = new int[numHouses*2+2];
		doEmptyCapture = Boolean.parseBoolean(Settings.getSetting("empty-capture"));
		
		for(int i=0; i<numHouses; ++i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[numHouses] = 0;//kalah1
		
		int kalah2 = housesAndKalahs.length-1;
		for(int i=kalah2-1; i>numHouses; --i) housesAndKalahs[i] = numSeeds;
		housesAndKalahs[kalah2] = 0;//kalah2
	}
	
	int moveSeeds(int from){
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
			if(housesAndKalahs[capture] == 0 && !doEmptyCapture) return i;
			
			int seeds = housesAndKalahs[i] + housesAndKalahs[capture];
			
			if(player1) housesAndKalahs[numHouses] += seeds;
			else housesAndKalahs[numHouses*2+1] += seeds;
			
			housesAndKalahs[i] = 0;
			housesAndKalahs[capture] = 0;
		}
		return i;
	}
	
	void collectLeftoverSeeds(){
		for(int i=0; i<numHouses; ++i){
			housesAndKalahs[numHouses] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
		for(int i=numHouses+1; i<housesAndKalahs.length-1; ++i){
			housesAndKalahs[housesAndKalahs.length-1] += housesAndKalahs[i];
			housesAndKalahs[i] = 0;
		}
	}
	
	int getScoreDifference(){
		return housesAndKalahs[numHouses] - housesAndKalahs[housesAndKalahs.length-1];
	}
	
	boolean validMove(int i){
		return i < housesAndKalahs.length && i != numHouses && i != numHouses*2+1
				&& housesAndKalahs[i] != 0;
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
	
	void pieRule(){
		//swap all houses
		for(int i=0; i<numHouses; ++i){
			int temp = housesAndKalahs[i];
			housesAndKalahs[i] = housesAndKalahs[i+numHouses+1];
			housesAndKalahs[i+numHouses+1] = temp;
		}
	}
	
	int[] getCopy(){
		int[] squares = new int[housesAndKalahs.length];
		for(int i=0; i<housesAndKalahs.length; ++i){
			squares[i] = housesAndKalahs[i];
		}
		return squares;
	}
	
	boolean gameNotOver(){
		boolean noSeeds = true;
		for(int i=0; i<numHouses; ++i) if(housesAndKalahs[i] != 0){
			noSeeds = false;
			break;
		}
		if(noSeeds) return false;
		noSeeds = true;
		for(int i=numHouses+1; i<housesAndKalahs.length-1; ++i) if(housesAndKalahs[i] != 0){
			noSeeds = false;
			break;
		}
		return !noSeeds;
	}
}