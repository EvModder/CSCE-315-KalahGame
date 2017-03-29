package AIs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AI{
	@Override
	public List<Integer> getMove(int[] board, int timelimit){
		int numHouses = board.length/2-1;
		Random rand = new Random();
		
		List<Integer> moves = new ArrayList<Integer>();
		
		//pick random moves
		int i;
		do{
			i = rand.nextInt(numHouses);
			moves.add(i);
			simulateMove(board, i);
			
		}while(board[i] == numHouses-i);
		
		return moves;
	}
	
	@Override
	public boolean doPieRule(int[] board, int timelimit){
		return true;
	}
}