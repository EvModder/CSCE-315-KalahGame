package AI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Main.Board;
import Main.MoveTimer;
import Main.MoveTimer.TimerListener;;

public class MinMaxAI extends AI{
	public static final int max = Integer.MAX_VALUE;
	public static final int min = Integer.MIN_VALUE;
	
	MoveTimer timer = new MoveTimer();
	boolean turn;
	public Node root;
	
	public MinMaxAI(Board board, int time, boolean whoseTurn){
		super(board, time);
		turn = whoseTurn;
	}
	
	class Node{
		private int alpha;
		private int beta;
		private int move;
		public boolean playerTurn;
		private Board currState;
		List<Node> children;
		
		public Node(Board b) {
			this.setAlpha(min);
		    this.setBeta(max);
		    this.setCurrState(b);
		    this.children = new LinkedList<Node>();
		}

	    public void addChild(Node node) {
	        this.children.add(node);
	    }
	    
	    public boolean isLeaf(){
	    	return children.isEmpty();
	    }
	    
		public int getAlpha() {
			return alpha;
		}

		public void setAlpha(int alpha) {
			this.alpha = alpha;
		}

		public int getBeta() {
			return beta;
		}

		public void setBeta(int beta) {
			this.beta = beta;
		}

		public int getMove() {
			return move;
		}

		public void setMove(int move) {
			this.move = move;
		}

		public Board getCurrState() {
			return currState;
		}

		public void setCurrState(Board currState) {
			this.currState = currState;
		}
	}
	
	//TODO: modify this as you see fit!
	public int utilityFunction(Node n){
		Board temp = n.getCurrState().getCopy();
		return temp.getScoreDifference();
	}
	
	public void constructTree(Node node, int depth){
		if (depth == 0){
			return;
		}
		// player 1
		if (node.playerTurn){
			for (int i = 0; i<board.numHouses; i++){
				Board temp = node.getCurrState().getCopy();
				if (temp.validMove(i)){
					boolean newTurn = false;
					if (temp.willHitKalah(i)){
						newTurn = true;
					}
					else if (!temp.willHitKalah(i)){
						newTurn = false;
					}
					temp.moveSeeds(i);
					if (!temp.gameNotOver()){
						temp.collectLeftoverSeeds();
					}
					Node newMove = new Node(temp);
					newMove.setMove(i);
					newMove.playerTurn = newTurn;
					node.addChild(newMove);
					constructTree(newMove, depth-1);
				}
			}
		}
		// player 2
		else if (!node.playerTurn){
			for (int i=board.kalah1()+1; i<board.kalah2(); i++){
				Board temp = node.getCurrState().getCopy();
				if (temp.validMove(i)){
					boolean newTurn = false;
					if (temp.willHitKalah(i)){
						newTurn = false;
					}
					else if (!temp.willHitKalah(i)){
						newTurn = true;
					}
					temp.moveSeeds(i);
					if (!temp.gameNotOver()){
						temp.collectLeftoverSeeds();
					}
					Node newMove = new Node(temp);
					newMove.setMove(i - board.numHouses - 1);
					newMove.playerTurn = newTurn;
					node.addChild(newMove);
					constructTree(newMove, depth-1);
				}
			}
		}
	}
	
	public int minimax(Node n, int depth){
		if (n.isLeaf() || depth == 0){
			return utilityFunction(n);
		}

		if (n.playerTurn){
			int temp = min;
			for (Node e:n.children){
				e.setAlpha(n.getAlpha());
				e.setBeta(n.getBeta());
				int child = minimax(e, depth-1);
				if (child > temp){
					temp = child;
					if (temp > n.getAlpha()){
						n.setAlpha(temp);
					}
				}
				if (temp >= n.getBeta()){
					return temp;
				}
			}
			return temp;
		}
		else if (!n.playerTurn){
			int temp = max;
			for (Node e:n.children){
				e.setAlpha(n.getAlpha());
				e.setBeta(n.getBeta());
				int child = minimax(e, depth-1);
				if (child < temp){
					temp = child;
					if (temp < n.getBeta()){
						n.setBeta(temp);
					}
				}
				if (temp <= n.getAlpha()){
					return temp;
				}
			}
			return temp;
		}
		return -1;
	}
	
	public void updateBoard(Board b){
		board = b;
	}
	
	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		
		new Thread(){
			@Override public void run(){
				//generate tree, add moves
				root = new Node(board);
				root.playerTurn = turn;
				constructTree(root, 9);
				minimax(root, 9);
				boolean keepGoing = true;
				while (keepGoing){
					int index = -1;
					int alpha = min;
					int beta = max;
					if (root.children.size() == 1){
						moves.add(root.children.get(0).getMove());
					}
					else{
						for (Node e:root.children){
							//player 1
							if (root.playerTurn){
								if (e.getAlpha() > alpha){
									alpha = e.getAlpha();
									index = root.children.indexOf(e);
								}
							}
							//player 2
							else if (!root.playerTurn){
								if (e.getBeta() < beta){
									beta = e.getBeta();
									index = root.children.indexOf(e);
								}
							}
						}
						moves.add(root.children.get(index).getMove());
						if (root.children.get(index).playerTurn == root.playerTurn){
							root = root.children.get(index);
						}
						else{
							keepGoing = false;
							//weHaveTime = false;
						}
					}
				}
			}
		}.start();
		return moves;
	}
}