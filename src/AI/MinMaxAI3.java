package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Main.Board;

public class MinMaxAI3 extends KalahPlayer{
	boolean waitingForOpp=true, hasTime=true;
	long turn, timeLimit, timeBuffer;
	int MAX_DEPTH;
	Node root;

	int floorLogBaseX(int x, long val){
		int count=0;
		long n=1;
		while(n*x <= val){
			n *= x;
			++count;
		}
		return count;
	}

	public MinMaxAI3(Board board){
		super(board);
		root = new Node(null, -100);
		MAX_DEPTH = floorLogBaseX(board.numHouses, 10000);//instantaneous
	}

	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		++turn;
//		if(root.parent == null)
			root.expand(board, true, turn, 0);
			System.out.println("expansion complete");
		
		while(hasTime) Thread.yield();
		hasTime = true;
		
		int land, move;
		do{
			moves.add(move = root.bestMove);
			root = root.bestChild;
			land = board.moveSeeds(move);
		}while(land == board.kalah1() && board.gameNotOver());

		waitingForOpp = true;
		return moves;
	}
	
	@Override public void closeGame(){
		pool.shutdownNow();
	}

	@Override public void applyOpponentMove(int move){
		int land = board.moveSeeds(move);
		if(waitingForOpp && land != board.kalah2){
			++turn;
			waitingForOpp = false;
		}
	}
	
	@Override public void updateTimer(long timeLeft){
//		if(timeLeft % 500 == 0) System.out.println("Time Left: "+timeLeft);
		if(timeLeft > timeLimit){
			timeLimit = timeLeft;
			timeBuffer = Math.min(timeLimit/5 + 11, 800);
		}
		else if(timeLeft < timeBuffer) hasTime = false;
	}

	
	ExecutorService pool = Executors.newFixedThreadPool(10);
	public class Node{
		boolean myTurn/*, isLeaf*/, expanded;
		int bestValue, moveToGetHere, bestMove = -100;
		Node parent, bestChild;
		Node[] children;
		
		Node(Node p, int move){parent=p; moveToGetHere = move;}
		
		public void expand(Board state, boolean myTurn, long pieTurn, int depth){
//			if(isLeaf) return; hopefully expand() won't be called on leaves
			if(depth == MAX_DEPTH){
				bestValue = state.getScoreDifference()+state.getSeedDifference()/state.kalah2;
			}
			else if(!state.gameNotOver()){
//				isLeaf = true;
				state.collectLeftoverSeeds();
				bestValue = state.getScoreDifference();
			}
			else{
//				//Approximation
//				bestValue = state.getScoreDifference()+state.getSeedDifference()/state.kalah2;
				
				List<Integer> moves = state.getPossibleMoves(myTurn, pieTurn);
				children = new Node[moves.size()]; int idx=-1;
//				int priority = Thread.currentThread().getPriority()-1;
				
//				ExecutorService executor = Executors.newFixedThreadPool(state.numHouses); 
				for(int move : state.getPossibleMoves(myTurn, pieTurn)){
					Board newState = state.getCopy();
					int land = newState.moveSeeds(move);
					boolean newMyTurn = myTurn ? (land == state.kalah1()) : (land != state.kalah2);
					int index = ++idx;
					
//					executor.execute(
//					pool.execute(new Thread(){@Override public void run(){
						
//						setPriority(priority);
						children[index] = new Node(Node.this, move);
						children[index].expand(newState, newMyTurn, pieTurn+1, depth+1);
//					}});
				}
			}
//			expanded = true;
			if(parent != null) parent.reconsiderBestChild(this);
		}
		
		private void pickBestChild(){
			if(myTurn){
				bestValue = Integer.MIN_VALUE;
				for(Node child : children){
					if(child.expanded && child.bestValue > bestValue){
						bestValue = child.bestValue;
						bestMove = child.moveToGetHere;
						bestChild = child;
					}
				}
			}
			else{
				bestValue = Integer.MAX_VALUE;
				for(Node child : children){
					if(child.expanded && child.bestValue < bestValue){
						bestValue = child.bestValue;
						bestMove = child.moveToGetHere;
						bestChild = child;
					}
				}
			}
			if(parent != null) parent.reconsiderBestChild(this);
		}
		
		synchronized void reconsiderBestChild(Node child){
			if(bestChild == null){
				bestValue = child.bestValue;
				bestMove = child.moveToGetHere;
				if(parent != null) parent.reconsiderBestChild(this);
			}
			else if(bestChild == child){
				pickBestChild();
			}
			else if(myTurn){
				if(child.bestValue > bestValue){
					bestValue = child.bestValue;
					bestMove = child.moveToGetHere;
					if(parent != null) parent.reconsiderBestChild(this);
				}
			}
			else{
				if(child.bestValue < bestValue){
					bestValue = child.bestValue;
					bestMove = child.moveToGetHere;
					if(parent != null) parent.reconsiderBestChild(this);
				}
			}
		}
	}
}