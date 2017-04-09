package AI;
import java.util.ArrayList;
import java.util.List;
import Main.Board;

public class MinMaxAI extends KalahPlayer{
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

	public MinMaxAI(Board board){
		super(board);
		MAX_DEPTH = floorLogBaseX(board.numHouses, 1000000);//instantaneous
		
		new Thread(){@Override public void run(){
//			System.out.println("test-depth: "+MAX_DEPTH);
//			System.out.println("Op Count="+Math.pow(board.numHouses, MAX_DEPTH));
			
			long time, start = System.currentTimeMillis();
			pickBestOption(board, true, turn);
			time = System.currentTimeMillis() - start;
			
//			System.out.println("Took time: "+time);
			OPS_PER_MILLI = (int)(Math.pow(board.numHouses, MAX_DEPTH)/time);
			
//			System.out.println("MinMax Depth = "+MAX_DEPTH);
//			System.out.println("Ops/Millisecond: "+OPS_PER_MILLI);
		}}.start();
	}

	@Override public List<Integer> getMove(){
		while(!knownTime || OPS_PER_MILLI == 0) Thread.yield();
		List<Integer> moves = new ArrayList<Integer>();
		++turn;
		
		int land, move;
		do{
//			System.out.println("Max Depth: "+MAX_DEPTH);
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
		long operations = Math.min(OPS_PER_MILLI*timeLeft, OPS_PER_MILLI*5000);
		MAX_DEPTH = Math.max(1, floorLogBaseX(board.numHouses, operations));
//		System.out.println("Timer Update, MAX_DEPTH="+MAX_DEPTH);
		knownTime = true;
	}

	int pickBestOption(Board state, boolean myTurn, long turn){
		int bestValue = myTurn ? NINF : INF;
		int bestMove = -100;

		int land;
//		long newTurn;
		boolean newMyTurn;
		Board newState;
		
		for(int move : state.getPossibleMoves(myTurn, turn)){
			newState = state.getCopy();
			land = newState.moveSeeds(move);
			newMyTurn = myTurn ? (land == board.kalah1()) : (land != board.kalah2);
//			newTurn = (newMyTurn == myTurn) ? turn : turn+1;//Remember, pie rule
			
			int value = pickBestValue(newState, 1, newMyTurn, turn+1);
			if(myTurn){
				if(value > bestValue){
					bestValue = value;
					bestMove = move;
				}
			}
			else{
				if(value < bestValue){
					bestValue = value;
					bestMove = move;
				}
			}
		}
		return bestMove;
	}
	
	int pickBestValue(Board state, int depth, boolean myTurn, long turn){
		if(depth >= MAX_DEPTH){
			return state.getScoreDifference();//+board.getSeedDifference()/board.kalah2;
		}
		if(!state.gameNotOver()){
			state.collectLeftoverSeeds();
			return state.getScoreDifference();
		}

		int bestValue = myTurn ? NINF : INF;

		++depth;
		int land;
//		long newTurn;
		boolean newMyTurn;
		Board newState;
		
		for(int move : state.getPossibleMoves(myTurn, turn)){
			newState = state.getCopy();
			land = newState.moveSeeds(move);
			newMyTurn = myTurn ? (land == board.kalah1()) : (land != board.kalah2);
//			newTurn = (newMyTurn == myTurn) ? turn : turn+1;
			
			int value = pickBestValue(newState, depth, newMyTurn, turn+1);
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