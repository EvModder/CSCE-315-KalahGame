package AI;
import java.util.List;
import Main.Board;

public abstract class AI {
	int timelimit;
	Board board;
	public AI(Board board, int time){
		this.board = board;
		timelimit = time;
	}
	
	abstract public List<Integer> getMove();
	abstract public void applyMove(int move);//0 means pie rule
}
