package AI;
import java.util.ArrayList;
import java.util.List;

public class DumbAI extends AI{
	@Override
	public List<Integer> getMove(int[] board, int timelimit){
		int numHouses = board.length/2-1;
		
		List<Integer> moves = new ArrayList<Integer>();
		
		boolean hitKalah = true;
		while(hitKalah){
			hitKalah = false;
			for(int i=numHouses-1; i>=0; --i){
				if(board[i] % (board.length-1) == numHouses-i){
					moves.add(i);
					simulateMove(board, i);
					hitKalah = true;
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