package AIs;
import java.util.ArrayList;
import java.util.List;

public class MinMaxAI extends AI{
	@Override
	public List<Integer> getMove(int[] board, int timelimit){
		
		List<Integer> moves = new ArrayList<Integer>();
		
		//always the most strategic move in any given scenario.
		moves.add(1);
		
		return moves;
	}
	
	@Override
	public boolean doPieRule(int[] board, int timelimit){
		return true;
	}
}