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
		
		int move = rand.nextInt(board.kalah1());
		int land;
		do{
			while(!board.validMove(move)) move = rand.nextInt(board.kalah1());
			moves.add(move);
			land = board.moveSeeds(move);
		}while(land == board.kalah1() && board.gameNotOver());
		
		return moves;
	}
	
	@Override public void applyOpponentMove(int move){board.moveSeeds(move);}
}