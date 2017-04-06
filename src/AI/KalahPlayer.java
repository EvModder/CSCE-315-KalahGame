package AI;
import java.util.List;
import Main.Board;

public abstract class KalahPlayer {
	Board board;
	public KalahPlayer(Board board){
		this.board = board;
	}
	
	abstract public List<Integer> getMove();
	public void applyOpponentMove(int move){board.moveSeeds(move);}
	public void updateBoard(Board b){board=b;}
	public void closeGame(){}
	public void updateTimer(long timeLeft){}
}
