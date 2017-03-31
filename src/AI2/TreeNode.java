package AI2;

import java.util.*;

public class TreeNode{
	public static final int max = Integer.MAX_VALUE;
	public static final int min = Integer.MIN_VALUE;
	
	private int alpha;
	private int beta;
	private int move;
	
	private Board currState;
	
    List<TreeNode> children;

    // Constructor for node of the tree
    public TreeNode(Board b) {
        this.setAlpha(min);
        this.setBeta(max);
        
        Board temp = new Board(b);
    	this.setCurrState(temp);
    	
        this.children = new LinkedList<TreeNode>();
    }
    
    public TreeNode(Board b, int alpha, int beta) {
    	this.alpha = alpha;
    	this.beta = beta;
    	
    	Board temp = new Board(b);
    	this.setCurrState(temp);
    }

    public void addChild(TreeNode node) {
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
	
	public Board getCurrState() {
		return currState;
	}

	public void setCurrState(Board currState) {
		this.currState = currState;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}
	
}
