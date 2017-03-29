import java.util.*;

public class AI2 {
	public TreeNode root;
	
	public AI2(){
	}
	
	public String checkPie(Board input){
		if (input.getNumSwitches() == 1 && !input.getHasUsedPieRule()){
			if (input.getNumSeeds(input.getIndexPlayer1()) >= 2)
			{
				return "Y";
			}
		}
		return "N";
	}
	
	public int generateMove(Board input, int depth){
		root = new TreeNode(input);
		constructTree(root, depth);
		minimax(root, depth);//, TreeNode.min, TreeNode.max);
		int index = -1;
		int alpha = TreeNode.min;
		int beta = TreeNode.max;
		if (root.children.size() == 1){
			return root.children.get(0).getMove();
		}
		else{
			for (TreeNode e:root.children){
				if (input.getPlayerTurn()){
					if (e.getAlpha() > alpha){
						alpha = e.getAlpha();
						index = e.getMove();
					}
				}
				else if (!input.getPlayerTurn()){
					if (e.getBeta() < beta){
						beta = e.getBeta();
						index = e.getMove();
					}
				}
			}
			return index;
		}
	}
	
	public int utility(TreeNode node){
		Board temp = new Board(node.getCurrState());
		return temp.getNumSeeds(temp.getIndexPlayer1()) - temp.getNumSeeds(temp.getIndexPlayer2());
	}
	
	public void constructTree(TreeNode node, int depth){
		if (depth == 0){
			return;
		}
		int first, last;
		// player 1
		if (node.getCurrState().getPlayerTurn()){
			first = 0;
			last = node.getCurrState().getIndexPlayer1();
			
			for (int i = first; i<last; i++){
				Board temp = new Board(node.getCurrState());
				
				if (temp.validMove(i)){
					int hit = temp.distributeSeeds(i);
					if (temp.hitEmptyHouse(hit)){
						temp.captureOppositeSeeds(hit);
					}
					if (temp.checkGameOver()){
						temp.collectLeftoverSeeds();
					}
					if (!temp.hitKalah(hit)){
						temp.changeTurn();
					}
					
					TreeNode newMove = new TreeNode(temp);
					newMove.setMove(i);
					node.addChild(newMove);
					constructTree(newMove, depth-1);
				}
			}
		}
		// player 2
		else{
			first = node.getCurrState().getIndexPlayer1() + 1;
			last = node.getCurrState().getIndexPlayer2();
			
			for (int i = first; i<last; i++){
				Board temp = new Board(node.getCurrState());
				
				if (temp.validMove(i)){
					int hit = temp.distributeSeeds(i);
					if (temp.hitEmptyHouse(hit)){
						temp.captureOppositeSeeds(hit);
					}
					if (temp.checkGameOver()){
						temp.collectLeftoverSeeds();
					}
					if (!temp.hitKalah(hit)){
						temp.changeTurn();
					}
					
					TreeNode newMove = new TreeNode(temp);
					newMove.setMove(i);
					node.addChild(newMove);
					constructTree(newMove, depth-1);
				}
			}
		}
	}
	
	public int minimax(TreeNode n, int depth){
		if (n.isLeaf() || depth == 0){
			n.setAlpha(utility(n));
			n.setBeta(utility(n));
			return utility(n);
		}

		if (n.getCurrState().getPlayerTurn()){
			int temp = TreeNode.min;
			for (TreeNode e:n.children){
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
		else if (!n.getCurrState().getPlayerTurn()){
			int temp = TreeNode.max;
			for (TreeNode e:n.children){
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
	// old version
	/*public int minimax(TreeNode n, int depth, int min, int max){
		if (n.isLeaf() || depth == 0){
			return utility(n);
		}

		if (n.getCurrState().getPlayerTurn()){
			int temp = min;
			for (TreeNode e:n.children){
				e.setAlpha(n.getAlpha());
				e.setBeta(n.getBeta());
				int child = minimax(e, depth-1, temp, max);
				if (child > temp){
					temp = child;
					n.setAlpha(temp);
				}
				if (temp >= max){
					return max;
				}
			}
			return temp;
		}
		else if (!n.getCurrState().getPlayerTurn()){
			int temp = max;
			for (TreeNode e:n.children){
				e.setAlpha(n.getAlpha());
				e.setBeta(n.getBeta());
				int child = minimax(e, depth-1, min, temp);
				if (child < temp){
					temp = child;
					n.setBeta(temp);
				}
				if (temp <= min){
					return min;
				}
			}
			return temp;
		}
		return -1;
	}
*/
}
