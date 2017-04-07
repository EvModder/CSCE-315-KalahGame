package AI;
import java.util.ArrayList;
import java.util.List;
import Main.Board;

public class BasicMinMaxAI extends KalahPlayer{
	StrategicAI utility;
	boolean working, makingTree, waitingForOpp=true;
	Node root;
	long turn, timeLimit, timeBuffer;
	
	public BasicMinMaxAI(Board board){
		super(board);
		utility = new StrategicAI(null);
	}
	
	@Override public List<Integer> getMove(){
		if(working) return null;//TODO: remove?
		List<Integer> moves = new ArrayList<Integer>();
		if(++turn == 1)/* first move */;
		else if(turn == 2){/* pie rule? */
			moves.add(-1);
			return moves;
		}
		
		working = true;
		
		if(!makingTree) new Thread(){@Override public void run(){
			root = new Node(board, true, getUtilityValue(board, true));
			//TODO: generate tree, add moves
			root.makeKids();
		}}.start();
		
		while(working) Thread.yield();
		
		int move;
		do{
			move = root.getSourceChild();
			root = root.children[move];
			moves.add(move);
		}while(board.willHitKalah(move));
		waitingForOpp = true;
		return moves;
	}

	@Override public void applyOpponentMove(int move){
		if(waitingForOpp){++turn; waitingForOpp = false;}
		board.moveSeeds(move);
		root = root.children[move-board.kalah1()-1];
	}
	
	@Override public void updateTimer(long time){
		if(time > timeLimit){
			timeLimit = time;
			timeBuffer = Math.min(timeLimit/5 + 10, 750);
		}
		else if(time < timeBuffer) working = false;
	}
	
	int getUtilityValue(Board board, boolean myTurn){
		utility.board = board.getCopy();
		if(!myTurn) utility.board.pieRule();//swap
		utility.getMove();
		return myTurn ? utility.getUtilityValue() : -utility.getUtilityValue();
	}

	class Node{
		Node(Board board, boolean max, int val){state=board; isMax=max; value=val;}
		int value;
		Board state;
		Node[] children = new Node[board.numHouses];
		boolean isMax, hasKids;
		
		void updateValue(){
			if(isMax){
				int maxV = Integer.MIN_VALUE;
				for(Node n : children) if(n != null && n.value > maxV) maxV = n.value;
			}
			else{
				int minV = Integer.MAX_VALUE;
				for(Node n : children) if(n != null && n.value > minV) minV = n.value;
			}
		}
		
		int getSourceChild(){
			int idx=-1;
			if(isMax){
				int maxV = Integer.MIN_VALUE;
				for(int i=0; i<children.length; ++i){
					if(children[i] != null && children[i].value > maxV){
						maxV = children[i].value;
						idx = i;
					}
				}
			}
			else{
				int minV = Integer.MAX_VALUE;
				for(int i=0; i<children.length; ++i){
					if(children[i] != null && children[i].value > minV){
						minV = children[i].value;
						idx = i;
					}
				}
			}
			return idx;
		}

		void makeKids() {
			int start, end;
			if(isMax) { start=0; end=state.kalah1(); }
			else { start=state.kalah1()+1; end = state.kalah2(); }
			boolean max = !isMax;
			
			//TODO: separate thread to update parent
//			int extrema = max ? Integer.MAX_VALUE : Integer.MIN_VALUE;
			for(int i=start, idx=0; i<end; ++i, ++idx){
				if(state.validMove(i)){
					Board board = state.getCopy();
					board.moveSeeds(i);
					int v = getUtilityValue(board, max);
					children[idx] = new Node(board, max, v);
//					if((max && v < extrema) || !max && (v > extrema)) extrema = v;
				}
			}
//			value = extrema;
			hasKids = true;
		}
	}
}