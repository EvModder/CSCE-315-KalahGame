package AI;
import java.util.ArrayList;
import java.util.List;

import Main.Board;
import Main.MoveTimer;
import Main.MoveTimer.TimerListener;;

public class MinMaxAI extends AI{
	public MinMaxAI(Board board, int time){super(board, time);}

	MoveTimer timer = new MoveTimer();
	boolean weHaveTime;
	
	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		
		weHaveTime = true;
		timer.startTimer(new TimerListener(){
			@Override public void timerEnded(){
				weHaveTime = false;
			}
		}, timelimit-1000);//1 second buffer to account for network lag
		
		new Thread(){
			@Override public void run(){
				//generate tree, add moves
			}
		}.start();
		
		while(weHaveTime) Thread.yield();
		return moves;
	}
	
	//TODO: modify this as you see fit!
	public int utilityFunction(Board board){
		return board.housesAndKalahs[board.kalah1()] - board.housesAndKalahs[board.kalah2()];
	}

	@Override public void applyMove(int move){board.moveSeeds(move);}
}