package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Main.Board;

public class RandomAI extends AI{
	public RandomAI(Board board, int time){super(board, time);}
	Random rand = new Random();
	
	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		
		//pick random moves
		int move;
		do{
			move = rand.nextInt(board.numHouses);
			moves.add(move);
			board.moveSeeds(move);
			
		}while(board.willHitKalah(move));
		
		return moves;
	}
	
	@Override public void applyMove(int move) {board.moveSeeds(move);}
}