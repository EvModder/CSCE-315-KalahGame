package AI;
import java.util.List;

import com.sun.scenario.Settings;

public abstract class AI {
	abstract public List<Integer> getMove(int[] board, int timelimit);
	abstract public boolean doPieRule(int[] board, int timelimit);
	
	boolean doEmptyCapture;
	public AI(){
		doEmptyCapture = Boolean.parseBoolean(Settings.get("empty-capture"));
	}
	
	int simulateMove(int[] board, int from){
		int numHouses = board.length/2-1;
		int numSeeds = board[from];
		boolean player1 = from < numHouses;
		
		board[from] = 0;
		
		int i = from;
		while(numSeeds > 0){
			if(++i == board.length) i = 0;
			
			if(player1){
				if(i == board.length-1) continue;
			}
			else{
				if(i == numHouses) continue;
			}
			--numSeeds;
			++board[i];
		}
		
		//capture pieces on the opposite square
		if(board[i] == 1 && (
				(player1 && i < numHouses) ||
				(!player1 && i > numHouses && i < board.length-1)
		)){
			int capture = i + (numHouses - i) * 2;
			if(board[capture] == 0 && !doEmptyCapture) return i;
			
			int seeds = board[i] + board[capture];
			
			if(player1) board[numHouses] += seeds;
			else board[numHouses*2+1] += seeds;
			
			board[i] = 0;
			board[capture] = 0;
		}
		return i;
	}
	
	boolean isValid(int[] board, int move){
		return move < board.length/2-1 && board[move] != 0;
	}
}
