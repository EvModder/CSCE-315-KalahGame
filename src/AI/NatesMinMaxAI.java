package AI;
import java.util.ArrayList;
import java.util.List;

import Main.Board;
import Main.MoveTimer;
import Main.MoveTimer.TimerListener;;

public class NatesMinMaxAI extends AI{
	MoveTimer timer = new MoveTimer();
	DumbAI utility;
	boolean working, makingTree;
	int turn;
	
	public NatesMinMaxAI(Board board, int time){
		super(board, time);
		utility = new DumbAI(null, 0);
	}
	
	@Override
	public List<Integer> getMove(){
		if(working) return null;
		List<Integer> moves = new ArrayList<Integer>();
		if(++turn == 1)/* first move */;
		else if(turn == 2){/* pie rule? */
			moves.add(-1);
			return moves;
		}
		
		//1 second buffer to account for network lag
		timer.startTimer(
			new TimerListener(){
				@Override public void timerEnded(){working = false;}
				@Override public void timeElapsed(long time){}
			},
			Math.max(timelimit-1000, timelimit-10)
		);
		
		if(!makingTree) new Thread(){@Override public void run(){
			//TODO: generate tree, add moves
		}}.start();
		
		while(working) Thread.yield();
		//TODO: get moves using tree
		return moves;
	}
	
	int getUtilityValue(Board board){
		utility.board = board.getCopy();
		utility.getMove();
		if(!utility.board.gameNotOver()) utility.board.collectLeftoverSeeds();
		return utility.board.getScoreDifference() +
				utility.board.getSeedDifference()/board.kalah2();//kalah2 = length-1
	}

	@Override public void applyOpponentMove(int move){
		++turn;
		board.moveSeeds(move);
		//TODO: update tree with move
	}
}