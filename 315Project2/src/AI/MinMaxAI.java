package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Main.Board;

/*
 * A working, multi-threaded, basic-ordering(pruning) MIN-MAX AI.
 * Uses recursive function calls instead of a tree of nodes to avoid
 * the slow down from the Java garbage collector.
 */
public class MinMaxAI extends KalahPlayer{
	long FUNCTION_SPEED, lastTime, timeLimit, timerStart, timerTime;
	int MAX_DEPTH, pieTurn=1;

	//Calculate floor(log_x(val))
	int floorLogBaseX(int x, long val){
		int count=-1;
		long n=1;
		while(n*x <= val){
			n *= x;
			++count;
		}
		return count;
	}
	
	//Calculate b^p
	long pow(long b, int p){
		long n = 1;
		for(int i=0; i<p; ++i) n *= b;
		return n;
	}

	public MinMaxAI(Board board){
		super(board);
		
		//Calculate approximate number of operations this computer can do in a millisecond
		new Thread(){@Override public void run(){
			MAX_DEPTH = floorLogBaseX(board.numHouses, 1000000);
					
			timerStart = System.currentTimeMillis();
			pickBestOption(board, 1);
			timerTime = System.currentTimeMillis() - timerStart;
			
			FUNCTION_SPEED = 5*pow(board.numHouses, MAX_DEPTH)/timerTime;
		}}.start();
	}
	
	@Override public List<Integer> getMove(){
		//Wait for variables to be defined (prevent race-condition)
		while(FUNCTION_SPEED == 0)Thread.yield();
		MAX_DEPTH = 0;
		while(MAX_DEPTH == 0) Thread.yield();
		
		//List of moves we are going to return
		List<Integer> moves = new ArrayList<Integer>();
		
		//Keep moving for as long as we hit our Kalah
		int move, land = board.kalah1();
		while(land == board.kalah1() && board.gameNotOver()){
			
			//Time how long it takes to get a move using the MinMax
			timerStart = System.currentTimeMillis();
			move = pickBestOption(board, pieTurn);
			timerTime = System.currentTimeMillis() - timerStart + 1;
			
			//Re-calculate running average of number of functions per millisecond
			long newSpeed = pow(board.numHouses, MAX_DEPTH)/timerTime;
			FUNCTION_SPEED = (long)(.93*FUNCTION_SPEED + .07*newSpeed);
			
			land = board.moveSeeds(move);
			moves.add(move);
			if(pieTurn == 2) pieTurn = 3;
		}
		if(pieTurn == 1) pieTurn = 2;
		return moves;
	}

	@Override public void applyOpponentMove(int move){
		if(board.moveSeeds(move) != board.kalah2 && pieTurn != 3){
			++pieTurn;
		}
	}
	
	//Update from the MoveTimer class to let us know how much time we have lefts
	@Override public void updateTimer(long timeLeft){
		lastTime = timeLeft;
		if(timeLeft > timeLimit) timeLimit = timeLeft;

		if(FUNCTION_SPEED != 0){
			timeLeft -= 200;//200 buffer (so this returns 200 milliseconds earlier than needed)
			if(timeLeft < 20) timeLeft = 20;
			
			//Model of n*sqrt(n) to go deeper for longer time limits
			else timeLeft *= timeLeft>>4;
			
			MAX_DEPTH = floorLogBaseX(board.numHouses, FUNCTION_SPEED*timeLeft)+1;
		}
	}
	
	//Find the best move given the current board state
	int pickBestOption(Board state, int pieTurn){
		int bestValue = Integer.MIN_VALUE;
		int bestMove = -100;
		
		List<Integer> moves = state.getPossibleMovesOrdered(true, pieTurn);
		ExecutorService pool = Executors.newFixedThreadPool(Math.min(moves.size(), 100));
		int[] values = new int[moves.size()];
		
		int idx = -1;
		for(int move : moves){
			Board newState = state.getCopy();
			int land = newState.moveSeeds(move);
			boolean newMyTurn = (land == state.kalah1());
			int newPieTurn = (!newMyTurn || pieTurn == 2) ? pieTurn+1 : pieTurn;
			int index = ++idx;
			
			//Run recursive calls in separate threads (multithreading)
			pool.execute(new Runnable(){@Override public void run(){
				if(newMyTurn) values[index] = pickHighestValue(newState, 1, newPieTurn);
				else values[index] = pickLowestValue(newState, 1, newPieTurn);
			}});
		}
		pool.shutdown();
		while(!pool.isTerminated()) Thread.yield();
		for(int i=0; i<values.length; ++i){
			if(values[i] > bestValue){
				bestValue = values[i];
				bestMove = moves.get(i);
			}
		}
		return bestMove;
	}
	
	//Evaluate the utility of a board
	int getUtility(Board state){
		int mySeeds=0,myVal=0, urSeeds=0,urVal=0;
		for(int i=0; i<board.numHouses; ++i){
			mySeeds += board.housesAndKalahs[i];
			myVal += Math.min(board.housesAndKalahs[i], board.numHouses-i);
		}
		for(int i=board.kalah2-1; i>board.numHouses; --i){
			urSeeds += board.housesAndKalahs[i];
			urVal += Math.min(board.housesAndKalahs[i], board.numHouses-i);
		}
		return state.getScoreDifference() + (myVal - urVal)>>1 +
				(mySeeds - urSeeds)/board.kalah2;
	}
	
	int pickHighestValue(Board state, int depth, int pieTurn){
		if(!state.gameNotOver()){
			state.collectLeftoverSeeds();
			return state.getScoreDifference();
		}
		if(depth >= MAX_DEPTH){
			return getUtility(state);
		}

		int bestValue = Integer.MIN_VALUE;

		++depth;
		Board newState;
		
		for(int move : state.getPossibleMovesOrdered(true, pieTurn)){
			newState = state.getCopy();
			int value;
			if(newState.moveSeeds(move) == board.numHouses){
				value = pickHighestValue(newState, depth, (pieTurn == 2) ? 3 : pieTurn);
			}
			else{
				value = pickLowestValue(newState, depth, (pieTurn == 3) ? 3 : pieTurn+1);
			}
			if(value > bestValue) bestValue = value;
		}
		return bestValue;
	}
	
	int pickLowestValue(Board state, int depth, int pieTurn){
		if(!state.gameNotOver()){
			state.collectLeftoverSeeds();
			return state.getScoreDifference();
		}
		if(depth >= MAX_DEPTH){
			return getUtility(state);
		}

		int bestValue =Integer.MAX_VALUE;

		++depth;
		Board newState;
		
		for(int move : state.getPossibleMovesOrdered(false, pieTurn)){
			newState = state.getCopy();
			int value;
			if(newState.moveSeeds(move) == board.kalah2){
				value = pickLowestValue(newState, depth, (pieTurn == 2) ? 3 : pieTurn);
			}
			else{
				value = pickHighestValue(newState, depth, (pieTurn == 3) ? 3 : pieTurn+1);
			}
			if(value < bestValue) bestValue = value;
		}
		return bestValue;
	}
}