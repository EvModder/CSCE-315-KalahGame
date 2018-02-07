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
public class MinMaxAI_WeirdPruning extends KalahPlayer{
	long FUNCTION_SPEED, lastTime, timeLimit, timerStart, timerTime;
	int MAX_DEPTH, pieTurn=1, BUFFER =300;

	//Calculate floor(log_x(val))
	int floorLogBaseX(int x, long val){
		int count=0;
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

	public MinMaxAI_WeirdPruning(final Board board){
		super(board);
		
		//Calculate approximate number of operations this computer can do in a millisecond
		new Thread(){@Override public void run(){
			MAX_DEPTH = floorLogBaseX(board.numHouses, 100000);
					
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
//			System.out.println("Attempting: "+MAX_DEPTH);
			move = pow(board.numHouses, MAX_DEPTH) > 10000 ?
					pickBestOptionThreaded(board, pieTurn) : pickBestOption(board, pieTurn);
			timerTime = System.currentTimeMillis() - timerStart + 1;
//			System.out.println("Actual: "+MAX_DEPTH);
			
			
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
		if(timeLeft > timeLimit){
			timeLimit = timeLeft;
			BUFFER = (int)Math.min(timeLimit/4, BUFFER);
		}

		if(FUNCTION_SPEED != 0){
			//BUFFER so this returns some number of milliseconds earlier than needed
			timeLeft -= BUFFER;//(Helps account for network lag)
			
			if(timeLeft > 64){
				timeLeft *= (timeLeft>>5);
			}
			
			MAX_DEPTH = floorLogBaseX(board.numHouses, FUNCTION_SPEED*timeLeft);
//			System.out.println("New MAX_DEPTH: "+MAX_DEPTH);
		}
		if(MAX_DEPTH < 1) MAX_DEPTH = 1;
	}
	
	//Find the best move given the current board state
	int pickBestOption(Board state, int pieTurn){
		List<Integer> moves = state.getPossibleMovesOrdered(true, pieTurn);
		int bestValue = Integer.MIN_VALUE, bestMove = moves.get(0), value;
		Board newState;
		
		for(int move : moves){
			newState = state.getCopy();
			
			if(newState.moveSeeds(move) == state.kalah1()){
				value = pickHighestValue(newState, 1, (pieTurn == 2) ? 3 : pieTurn, WIN);
				if(value > bestValue){
					bestValue = value;
					bestMove = move;
				}
			}
			else{
				value = pickLowestValue(newState, 1, (pieTurn == 3) ? 3 : pieTurn+1, LOSE);
				if(value < bestValue){
					bestValue = value;
					bestMove = move;
				}
			}
		}
		return bestMove;
	}
	
	int pickBestOptionThreaded(Board state, final int pieTurn){
		List<Integer> moves = state.getPossibleMovesOrdered(true, pieTurn);
		int bestValue = Integer.MIN_VALUE, bestMove = moves.get(0);
		ExecutorService pool = Executors.newFixedThreadPool(Math.min(moves.size(), 100));
		final int[] values = new int[moves.size()];
		int i=-1;
		
		for(int move : moves){
			final Board newState = state.getCopy();
			final int index = ++i;
			
			if(newState.moveSeeds(move) == state.kalah1()){
				pool.execute(new Runnable(){@Override public void run(){
					values[index] = pickHighestValue(newState, 1, (pieTurn == 2) ? 3 : pieTurn, WIN);
				}});
			}
			else{
				pool.execute(new Runnable(){@Override public void run(){
					values[index] = pickLowestValue(newState, 1, (pieTurn == 3) ? 3 : pieTurn+1, LOSE);
				}});
			}
		}
		pool.shutdown();
		while(!pool.isTerminated()) Thread.yield();
		for(i=0; i<values.length; ++i){
			if(values[i] > bestValue){
				bestValue = values[i];
				bestMove = moves.get(i);
			}
		}
		return bestMove;
	}
	
	int getUtility(Board state){
		int mySeeds=0,myVal=0, urSeeds=0,urVal=0;
		for(int i=0; i<board.numHouses; ++i){
			mySeeds += board.housesAndKalahs[i];
			myVal += Math.min(board.housesAndKalahs[i], board.numHouses-i);
		}
		for(int i=board.house02; i<board.kalah2; ++i){
			urSeeds += board.housesAndKalahs[i];
			urVal += Math.min(board.housesAndKalahs[i], board.kalah2-i);
		}
//		return state.getScoreDifference() + ((myVal - urVal)>>1) + (mySeeds - urSeeds)/board.kalah2;
//		return state.getScoreDifference() + (((myVal - urVal)<<1) + (mySeeds - urSeeds))/board.numHouses;
		return state.getScoreDifference() + ((myVal - urVal)>>1) + (mySeeds - urSeeds) / (Math.min(mySeeds, urSeeds)>>1);
	}
	
	int WIN = Integer.MAX_VALUE-1, LOSE = Integer.MIN_VALUE+1, WILL_WIN = WIN/4, WILL_LOSE = LOSE/4;
//	int WILL_SCUNK = WILL_WIN*2;
	int pickHighestValue(Board state, int depth, int pieTurn, int beta){
		if(state.gameOver()){
			int score = state.getFinalScoreDifference();
//			return score > 0 ? WIN : (diff == 0 ? 0 : LOSE);
//			return state.getScoreDifference()<<4;
//			return score + (score > 0 ? (state.seedsToTie <= 1.5*score ? WILL_SCUNK : WILL_WIN) : WILL_LOSE);
			return score + (score > 0 ? WILL_WIN : WILL_LOSE);//Already wins maximally
		}
		if(depth >= MAX_DEPTH){
			return getUtility(state);
		}//numSeeds == 2*seedsToTie == seedsToTie-.5*score + seedsToTie+.5*score

		int bestValue = LOSE;

		++depth;
		Board newState;
		int pieHigh = (pieTurn == 2) ? 3 : pieTurn, pieLow = (pieTurn == 3) ? 3 : pieTurn+1;

		for(int move : state.getPossibleMovesOrdered(true, pieTurn)){
			newState = state.getCopy();
			int value;
			if(newState.moveSeeds(move) == board.numHouses){
				value = pickHighestValue(newState, depth, pieHigh, beta);
			}
			else{
				value = pickLowestValue(newState, depth, pieLow, bestValue/2);
			}
			if(value > bestValue){
				if(value > beta) return value;
				//Comment out the MAGIC_LINEs for a more glorious but less likely win
//				if(value > board.seedsToTie) return value;//MAGIC_LINE
				bestValue = value;
			}
		}
		return bestValue;
	}
	
	int pickLowestValue(Board state, int depth, int pieTurn, int alpha){
		if(state.gameOver()){
			int score = state.getFinalScoreDifference();
			return score + (score > 0 ? WILL_WIN : WILL_LOSE);
		}
		if(depth >= MAX_DEPTH){
			return getUtility(state);
		}

		int worstValue = WIN;

		++depth;
		Board newState;
		int pieLow = (pieTurn == 2) ? 3 : pieTurn, pieHigh = (pieTurn == 3) ? 3 : pieTurn+1;
		
		for(int move : state.getPossibleMovesOrdered(false, pieTurn)){
			newState = state.getCopy();
			int value;
			if(newState.moveSeeds(move) == board.kalah2){
				value = pickLowestValue(newState, depth, pieLow, alpha);
			}
			else{
				value = pickHighestValue(newState, depth, pieHigh, worstValue/2);
			}
			if(value < worstValue){
				if(value < alpha) return value;
				//Comment out the MAGIC_LINEs for a more glorious but less likely win
//				if(value < board.seedsToTie) return value;//MAGIC_LINE
				worstValue = value;
			}
		}
		return worstValue;
	}
}