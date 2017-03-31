package AI;
import java.util.ArrayList;
import java.util.List;

import Main.Utils;
import Main.Utils.TimerListener;

public class MinMaxAI extends AI{
	boolean weHaveTime;
	List<Integer> moves = new ArrayList<Integer>();
	
	@Override
	public List<Integer> getMove(int[] board, int timelimit){
		moves.clear();
		
		weHaveTime = true;
		Utils.startTimer(new TimerListener(){
			@Override public void timerEnded() {
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
	
	@Override
	public boolean doPieRule(int[] board, int timelimit){
		return true;
	}
}