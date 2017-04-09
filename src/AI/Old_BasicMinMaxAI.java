package AI;
import java.util.ArrayList;
import java.util.List;
import Main.Board;

public class Old_BasicMinMaxAI extends KalahPlayer{
	static int INF = Integer.MAX_VALUE, NINF = -Integer.MAX_VALUE;
	int MAX_DEPTH;
	boolean waitingForOpp=true;
	long turn, timeLimit, timeBuffer, operationCount;

	int floorLogBaseX(int x, long val){
		int count=0;
		long n=1;
		while(n*x <= val){
			n *= x;
			++count;
		}
		return count;
	}

	public Old_BasicMinMaxAI(Board board){
		super(board);
		MAX_DEPTH = floorLogBaseX(board.numHouses, 10000000);
		System.out.println("MinMax Depth = "+MAX_DEPTH);
		System.out.println("Max Ops: "+Math.pow(board.numHouses, MAX_DEPTH));
	}

	@Override public List<Integer> getMove(){
		operationCount = 0;
		List<Integer> moves = new ArrayList<Integer>();
		++turn;

		int land, move;
		do{
			moves.add(move = pickBestOption(board, 0, true, turn));
			
			System.out.println("Op Count="+operationCount);
			operationCount = 0;
			
			land = board.moveSeeds(move);
		}while(land == board.kalah1() && board.gameNotOver());

		waitingForOpp = true;
		return moves;
	}

	@Override public void applyOpponentMove(int move){
		if(waitingForOpp){++turn; waitingForOpp = false;}
		board.moveSeeds(move);
	}

	int getUtilityValue(Board board, boolean over){
		if(over) board.collectLeftoverSeeds();
		return board.getScoreDifference()+board.getSeedDifference()/board.kalah2;
	}

	int pickBestOption(Board state, int depth, boolean myTurn, long turn){
		++operationCount;
		if(depth == MAX_DEPTH) return getUtilityValue(state, false);
		if(!state.gameNotOver()) return getUtilityValue(state, true);

		int bestValue = myTurn ? NINF : INF;
		int bestMove = -100;

		for(int move : state.getPossibleMoves(myTurn, turn)){
			Board newState = state.getCopy();
			int land = newState.moveSeeds(move);
			boolean newTurn;
			if(myTurn) newTurn = (land == board.kalah1());
			else newTurn = (land != board.kalah2);
			if(myTurn != newTurn) ++turn;
			int value = pickBestOption(newState, depth + 1, newTurn, turn);
			if(myTurn){
				// on a 'max' node of the minmax tree
				if(value > bestValue){
					bestValue = value;
					bestMove = move;
				}
			}
			else{
				// on a 'min' node of the minmax tree
				if(value < bestValue){
					bestValue = value;
					bestMove = move;
				}
			}
		}
		return depth == 0 ? bestMove : bestValue;
	}
}