package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Main.Board;

public class UNUSED_MinMaxAI4 extends KalahPlayer{
	static int INF = Integer.MAX_VALUE, NINF = -Integer.MAX_VALUE;
	int MAX_DEPTH, OPS_PER_MILLI;//determined at runtime
	boolean waitingForOpp=true, knownTime;
	long turn;

	int floorLogBaseX(int x, long val){
		int count=0;
		long n=1;
		while(n*x <= val){
			n *= x;
			++count;
		}
		return count;
	}

	public UNUSED_MinMaxAI4(Board board){
		super(board);
		OPS_PER_MILLI = 5000;
	}

	@Override public List<Integer> getMove(){
		while(!knownTime || OPS_PER_MILLI == 0) Thread.yield();
		List<Integer> moves = new ArrayList<Integer>();
		++turn;
		
		int land, move;
		do{
			moves.add(move = pickBestOption(board, true, turn));
			land = board.moveSeeds(move);
		}while(land == board.kalah1() && board.gameNotOver());

		waitingForOpp = true;
		return moves;
	}

	@Override public void applyOpponentMove(int move){
		int land = board.moveSeeds(move);
		if(waitingForOpp && land != board.kalah2){
			++turn; waitingForOpp = false;
		}
	}
	
	@Override public void updateTimer(long timeLeft){
//		long operations = OPS_PER_MILLI*timeLeft;
		long operations = Math.min(OPS_PER_MILLI*timeLeft, OPS_PER_MILLI*10000);
		MAX_DEPTH = Math.max(1, floorLogBaseX(board.numHouses, operations));
		if(timeLeft % 500 == 0) System.out.println("Timer Update, MAX_DEPTH="+MAX_DEPTH);
		knownTime = true;
	}
	
	int pickBestOption(Board state, boolean myTurn, long pieTurn){
		int bestValue = myTurn ? NINF : INF;
		int bestMove = -100;
		
		List<Integer> moves = state.getPossibleMoves(myTurn, pieTurn);
		ExecutorService pool = Executors.newFixedThreadPool(moves.size());
		int[] values = new int[moves.size()];
		
		int idx = -1;
		for(int move : moves){
			Board newState = state.getCopy();
			int land = newState.moveSeeds(move);
			boolean newMyTurn = myTurn ? (land == state.kalah1()) : (land != state.kalah2);
			long newPieTurn = (newMyTurn != myTurn || pieTurn == 2) ? pieTurn+1 : pieTurn;
			int index = ++idx;
			
			pool.execute(new Runnable(){@Override public void run(){
				values[index] = pickBestValue(newState, 1, newMyTurn, newPieTurn, NINF, INF);
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
	
	int pickBestValue(Board state, int depth, boolean myTurn, long pieTurn, int alpha, int beta){
		if(depth == MAX_DEPTH){
			return state.getScoreDifference();//+board.getSeedDifference()/board.kalah2;
		}
		if(!state.gameNotOver()){
			state.collectLeftoverSeeds();
			return state.getScoreDifference();
		}

		++depth;
		int land;
		boolean newMyTurn;
		Board newState;
		
		for(int move : state.getPossibleMoves(myTurn, pieTurn)){
			newState = state.getCopy();
			land = newState.moveSeeds(move);
			newMyTurn = myTurn ? (land == board.kalah1()) : (land != board.kalah2);
			long newPieTurn = (newMyTurn != myTurn || pieTurn == 2) ? pieTurn+1 : pieTurn;
			
			int value = pickBestValue(newState, depth, newMyTurn, newPieTurn, alpha, beta);
			if(myTurn){
				if(value > alpha) alpha = value;
			}
			else{
				if(value < beta) beta = value;
			}
		}
		return myTurn ? alpha : beta;
	}
}