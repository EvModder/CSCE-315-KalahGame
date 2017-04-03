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
	abstract public void applyOpponentMove(int move);//Apply opponent move. -1 means pie rule
	public void updateBoard(Board b){board=b;}
}
