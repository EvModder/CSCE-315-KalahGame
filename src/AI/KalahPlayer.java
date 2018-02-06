package AI;
import java.util.List;
import Main.Board;

/*
 * An abstract class that represents a KalahPlayer.
 * Can be either an AI or an actual Human playing through an interface
 * Provides basic functions like getMove() which can be used by the KalahGame
 */
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
