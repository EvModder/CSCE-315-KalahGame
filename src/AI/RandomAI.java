package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Main.Board;

/*
 * An AI that randomly selects a possible move from the current board
 * If the random moves lands in the Kalah, it goes again, but overall
 * this AI follows absolutely no strategy.
 */
public class RandomAI extends KalahPlayer{
	public RandomAI(Board board){super(board);}
	Random rand = new Random();
	
	@Override
	public List<Integer> getMove() {
		List<Integer> moves = new ArrayList<Integer>();
		
		int move, land;
		do{
			//Get all possible moves ('true' = it is my turn, 3 = neglect pie rule)
			List<Integer> possibleMoves = board.getPossibleMoves(true, 3);
			
			//Select a random move
			move = possibleMoves.get(rand.nextInt(possibleMoves.size()));
			
			moves.add(move);
			land = board.moveSeeds(move);
		}
		while(land == board.kalah1() && board.gameNotOver());
		
		return moves;
	}
}