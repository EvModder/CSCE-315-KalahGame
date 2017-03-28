package AIs;
import java.util.ArrayList;
import java.util.List;

public class DumbAI extends AI{
	@Override
	public List<Integer> getMove(int[] board, int timelimit){
		int numHouses = board.length/2-1;
		
		List<Integer> moves = new ArrayList<Integer>();
		
		boolean moveAgain = true;
		while(moveAgain){
			moveAgain = false;
			for(int i=numHouses-1; i>=0; --i){
				if(board[i] == numHouses-i){
					moves.add(i);
					simulateMove(board, i);
					moveAgain = true;
					break;
				}
			}
		}
		for(int i=numHouses-1; i>=0; --i){
			if(board[i] != 0){
				moves.add(i);
				break;
			}
		}
		return moves;
	}
	
	@Override
	public boolean doPieRule(int[] board, int timelimit){
		return true;
	}
}