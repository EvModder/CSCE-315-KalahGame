import java.util.*;

public class AI{
	public double getMove(Board board){
		Board game = new Board(board);
		
		Node move = minimax(game, false, 10);
		if(move.getMoveVal() == Double.NEGATIVE_INFINITY || move.getMoveVal() == Double.POSITIVE_INFINITY){
			System.out.println("No node");
			int i = 1; 
			while(true){
				if(board.getNumSeeds(i+game.getIndexPlayer1()) != 0)
					return i+game.getIndexPlayer1();
				else{
					i++;
				}
			}
		}
		System.out.println("Move is " + move.getMoveVal()); 
		return move.getMoveVal();
		
	}
	
	public Node minimax(Board b, boolean player, int depth){
		if(b.checkGameOver() || depth == 0){
			if(b.getPlayerTurn() == true){
				return new Node(b.getNumSeeds(b.getIndexPlayer2()) - b.getNumSeeds(b.getIndexPlayer1()));
			}
			else{
				return new Node(b.getNumSeeds(b.getIndexPlayer1()) - b.getNumSeeds(b.getIndexPlayer2()));
			}
		}
		
		ArrayList<Node> moves = new ArrayList<Node>();
		
		for(int i = 0; i < b.getIndexPlayer1(); i++){
			int j = 0;
			
			if(player == false){
				j = i + b.getIndexPlayer1() + 1;
			}
			else{
				j = i;
			}
			if(i == 0){
				b.changeTurn();
			}
	
			if(b.validMove(j)){
				Node n = new Node();
				
				n.setMoveVal(j);
				
				Board original = new Board(b);

				b.distributeSeeds(j);
				
				if(player == true){
					n.setUtilVal(minimax(b, false, depth-1).getUtilVal());
				}
				else{
					n.setUtilVal(minimax(b, true, depth-1).getUtilVal());
				}

				//System.out.println("The value of n is " + n.getUtilVal());
				moves.add(n);
				b = original;
			}
			b.changeTurn();
		}
		
		Node bestMove = new Node();
		
		if(player == false){
			double score = Double.NEGATIVE_INFINITY;
			
			for(Node n : moves){
				if(n.getUtilVal() > score){
					bestMove = n;
					score = n.getUtilVal();
				}
			}
		}
		else{
			double score = Double.POSITIVE_INFINITY;
			
			for(Node n : moves){
				if(n.getUtilVal() < score){
					bestMove = n;
					score = n.getUtilVal();
				}
			}
		}
		return bestMove;
	}
}