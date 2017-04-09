package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Main.Board;

public class MinMaxAI2 extends KalahPlayer{
	static int INF = Integer.MAX_VALUE, NINF = -Integer.MAX_VALUE;
	int MAX_DEPTH, OPS_PER_MILLI;//determined at runtime
	boolean waitingForOpp=true/*, hasTime=true*/, knownTime;
	long pieTurn, opCount/*, timeLimit, timeBuffer*/;

	int floorLogBaseX(int x, long val){
		int count=0;
		long n=1;
		while(n*x <= val){
			n *= x;
			++count;
		}
		return count;
	}

	public MinMaxAI2(Board board){
		super(board);
		OPS_PER_MILLI = 3000;
/*		new Thread(){@Override public void run(){
			MAX_DEPTH = floorLogBaseX(board.numHouses, 1000000);//instantaneous?
//			System.out.println("test-depth: "+MAX_DEPTH);
//			System.out.println("Op Count="+Math.pow(board.numHouses, MAX_DEPTH));
			
			long time, start = System.currentTimeMillis();
			pickBestOption(board, true, turn);
			time = System.currentTimeMillis() - start;
			
//			System.out.println("Took time: "+time);
//			OPS_PER_MILLI = (int)(Math.pow(board.numHouses, MAX_DEPTH)/time);
			
//			System.out.println("MinMax Depth = "+MAX_DEPTH);
			System.out.println("Ops/Millisecond: "+OPS_PER_MILLI);
		}}.start();*/
	}
//	int land, move;
//	@SuppressWarnings("deprecation")
	@Override public List<Integer> getMove(){
		while(!knownTime || OPS_PER_MILLI == 0) Thread.yield();
		List<Integer> moves = new ArrayList<Integer>();
		++pieTurn;
		opCount=0;
		
		int move, land = board.kalah1();
		System.out.println("getting move");
//		Thread t = new Thread(){@Override public void run(){
			while(land == board.kalah1() && board.gameNotOver())
			{
				System.out.println("MAX_DEPTH="+MAX_DEPTH);
				move = pickBestOption(board, true, pieTurn);
				land = board.moveSeeds(move);
				moves.add(move);
				if(pieTurn == 2) ++pieTurn;
			}
//			System.out.println("got moves");
//		}}; t.start();
//		while(hasTime && t.isAlive()){
//			Thread.yield();
//			System.out.println("has time");
//		}
//		hasTime = true;
//		t.stop();
//		
//		if(land == board.kalah1()){
//			System.out.println("From MinMax: "+moves.toString());
//			if(moves.isEmpty()) OPS_PER_MILLI -= 500;
//			
//			utility.board = board;//pass by reference, will update our board as well
//			moves.addAll(utility.getMove());
//		}
		
		waitingForOpp = true;
		return moves;
	}

	@Override public void applyOpponentMove(int move){
		if(board.moveSeeds(move) != board.kalah2){
			waitingForOpp = false;
			if(pieTurn != 3) ++pieTurn;
		}
	}
	
	@Override public void updateTimer(long timeLeft){
//		long operations = OPS_PER_MILLI*timeLeft;
		long operations = Math.min(OPS_PER_MILLI*timeLeft, OPS_PER_MILLI*10000);
		MAX_DEPTH = Math.max(1, floorLogBaseX(board.numHouses, operations));
		
//		if(timeLeft % 500 == 0) System.out.println("Timer Update, MAX_DEPTH="+MAX_DEPTH);
//		System.out.println("ops="+opCount);
		
//		if(timeLeft > timeLimit){
//			timeLimit = timeLeft;
//			timeBuffer = Math.min(timeLimit/5 + 11, 800);
			knownTime = true;
//		}
//		else if(timeLeft < timeBuffer) hasTime = false;
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
	
	StrategicAI utility = new StrategicAI(null);
	int getStrategicUtility(Board state, boolean myTurn){
		utility.board = state;
		if(myTurn){
			utility.getMove();
			return state.getScoreDifference();//+state.getSeedDifference()/state.kalah2;
		}
		else{
			utility.board.pieRule();
			utility.getMove();
			return -state.getScoreDifference();//-state.getSeedDifference()/state.kalah2;
		}
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
	
	int pickBestValue(Board state, int depth, boolean myTurn, long pieTurn){
		++opCount;
		if(!state.gameNotOver()){
			state.collectLeftoverSeeds();
			return state.getScoreDifference();
		}
		if(depth >= MAX_DEPTH){
			return getUtility(state, myTurn);
		}

		int bestValue = myTurn ? NINF : INF;

		++depth;
		int land;
		boolean newMyTurn;
		Board newState;
		
		for(int move : state.getPossibleMoves(myTurn, pieTurn)){
			newState = state.getCopy();
			land = newState.moveSeeds(move);
			newMyTurn = myTurn ? (land == board.kalah1()) : (land != board.kalah2);
			long newPieTurn = (newMyTurn != myTurn || pieTurn == 2) ? pieTurn+1 : pieTurn;
			
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