package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Main.Board;

public class MinMaxAIFinal extends KalahPlayer{
	int MAX_DEPTH, FUNCTION_SPEED;
	int pieTurn=1;
	long lastTime, timeLimit, timerStart, timerTime;

	int floorLogBaseX(int x, long val){
		int count=0;
		long n=1;
		while(n*x <= val){
			n *= x;
			++count;
		}
		return count;
	}

	public MinMaxAIFinal(Board board){
		super(board);
		new Thread(){@Override public void run(){
			MAX_DEPTH = floorLogBaseX(board.numHouses, 1000000);//instantaneous?
					
			timerStart = System.currentTimeMillis();
			pickBestOption(board, true, 1);
			timerTime = System.currentTimeMillis() - timerStart;
			
			FUNCTION_SPEED = 5*(int)(Math.pow(board.numHouses, MAX_DEPTH)/timerTime);
//			System.out.println("FUNCTION_SPEED="+FUNCTION_SPEED);
		}}.start();
	}
	
	@Override public List<Integer> getMove(){
		while(FUNCTION_SPEED == 0)Thread.yield();
		MAX_DEPTH = 0;
		while(MAX_DEPTH == 0) Thread.yield();
		List<Integer> moves = new ArrayList<Integer>();
		
		int move, land = board.kalah1();
		while(land == board.kalah1() && board.gameNotOver()){
//			System.out.println("Attempting Depth: "+MAX_DEPTH);
			timerStart = System.currentTimeMillis();
			move = pickBestOption(board, true, pieTurn);
			timerTime = System.currentTimeMillis() - timerStart;
//			System.out.println("Actual Depth: "+MAX_DEPTH);
//			System.out.println("New speed: "+(Math.pow(board.numHouses, MAX_DEPTH)/timerTime));
			FUNCTION_SPEED = (int)(.93*FUNCTION_SPEED +
							 	   .07*Math.pow(board.numHouses, MAX_DEPTH)/timerTime);
//			System.out.println("FUNCTION_SPEED="+FUNCTION_SPEED);
			
			land = board.moveSeeds(move);
			moves.add(move);
			if(pieTurn == 2) pieTurn = 3;
		}
		if(pieTurn == 1) pieTurn = 2;
//		if(4*(timeLimit-lastTime) > timeLimit){
//			FUNCTION_SPEED *= 0.75*timeLimit/lastTime;
//			System.out.println("Multiplier: "+0.75*timeLimit/lastTime);
//			System.out.println("Finished early, new speed="+FUNCTION_SPEED);
//		}
		return moves;
	}

	@Override public void applyOpponentMove(int move){
		if(board.moveSeeds(move) != board.kalah2 && pieTurn != 3){
			++pieTurn;
		}
	}
	
	@Override public void updateTimer(long timeLeft){
		lastTime = timeLeft;
		if(timeLeft > timeLimit) timeLimit = timeLeft;

//		int newDepth = floorLogBaseX(board.numHouses, FUNCTION_SPEED*timeLeft);
//		if(newDepth < MAX_DEPTH) newDepth = MAX_DEPTH-1;
//		MAX_DEPTH = Math.max(1, newDepth);
		
		//Out of time, act rashly!
//		if(timeLeft < 500)
//			MAX_DEPTH = floorLogBaseX(board.numHouses, 500000);
		else if(FUNCTION_SPEED != 0)
			MAX_DEPTH = floorLogBaseX(board.numHouses, FUNCTION_SPEED*(timeLeft-100))+1;//Note -100
	}
	
	int pickBestOption(Board state, boolean myTurn, int pieTurn){
		int bestValue = myTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int bestMove = -100;
		
		List<Integer> moves = state.getPossibleMovesOrdered(myTurn, pieTurn);
		ExecutorService pool = Executors.newFixedThreadPool(moves.size());
		int[] values = new int[moves.size()];
		
		int idx = -1;
		for(int move : moves){
			Board newState = state.getCopy();
			int land = newState.moveSeeds(move);
			boolean newMyTurn = myTurn ? (land == state.kalah1()) : (land != state.kalah2);
			int newPieTurn = (newMyTurn != myTurn || pieTurn == 2) ? pieTurn+1 : pieTurn;
			int index = ++idx;
			
			pool.execute(new Runnable(){@Override public void run(){
				values[index] = pickBestValue(newState, 1, newMyTurn, newPieTurn);
			}});
		}
		pool.shutdown();
		while(!pool.isTerminated()) Thread.yield();
		for(int i=0; i<values.length; ++i){
			if(myTurn){
				if(values[i] > bestValue){
					bestValue = values[i];
					bestMove = moves.get(i);
				}
			}
			else{
				if(values[i] < bestValue){
					bestValue = values[i];
					bestMove = moves.get(i);
				}
			}
		}
		return bestMove;
	}
	
	int getUtility(Board state, boolean myTurn){
		int mySeeds=0,myVal=0, urSeeds=0,urVal=0;
		for(int i=0; i<board.numHouses; ++i){
			mySeeds += board.housesAndKalahs[i];
			myVal += Math.min(board.housesAndKalahs[i], board.numHouses-i);
		}
		for(int i=board.kalah2-1; i>board.numHouses; --i){
			urSeeds += board.housesAndKalahs[i];
			urVal += Math.min(board.housesAndKalahs[i], board.numHouses-i);
		}
		return state.getScoreDifference() + (myVal - urVal) +
				(mySeeds - urSeeds)/board.kalah2;
	}
	
	int pickBestValue(Board state, int depth, boolean myTurn, int pieTurn){
		if(!state.gameNotOver()){
			state.collectLeftoverSeeds();
			return state.getScoreDifference();
		}
		if(depth >= MAX_DEPTH){
			return getUtility(state, myTurn);
		}

		int bestValue = myTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		++depth;
		int land;
		boolean newMyTurn;
		Board newState;
		
		for(int move : state.getPossibleMovesOrdered(myTurn, pieTurn)){
			newState = state.getCopy();
			land = newState.moveSeeds(move);
			newMyTurn = myTurn ? (land == board.kalah1()) : (land != board.kalah2);
			int newPieTurn = (newMyTurn != myTurn || pieTurn == 2) ? pieTurn+1 : pieTurn;
			
			int value = pickBestValue(newState, depth, newMyTurn, newPieTurn);
			if(myTurn){
				if(value > bestValue) bestValue = value;
			}
			else{
				if(value < bestValue) bestValue = value;
			}
		}
		return bestValue;
	}
}