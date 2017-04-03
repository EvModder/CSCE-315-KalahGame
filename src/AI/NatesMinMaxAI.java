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
		else if(turn == 2)/* pie rule? */;
		
		//1 second buffer to account for network lag
		timer.startTimer(new TimerListener(){@Override public void timerEnded(){working = false;}},
				Math.max(timelimit-1000, timelimit-10));
		
		if(!makingTree) new Thread(){@Override public void run(){
			//generate tree, add moves
		}}.start();
		
		while(working) Thread.yield();
		//get moves using tree
		return moves;
	}
	
	int getUtilityValue(Board board){
		utility.board = board.getCopy();
		utility.getMove();
		return utility.board.getScoreDifference() +
				utility.board.getSeedDifference()/board.kalah2();//kalah2 = length-1
	}

	@Override public void applyMove(int move){
		++turn;
		//TODO: update tree with move
	}
}