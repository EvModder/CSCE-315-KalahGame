package AI;
import java.util.ArrayList;
import java.util.List;
import Main.Board;

public class DumbAI extends KalahPlayer{
	public DumbAI(Board board){super(board);}
	
	@Override public void applyOpponentMove(int move){board.moveSeeds(move);}
	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		
		boolean hitKalah = true;
		while(hitKalah){
			hitKalah = false;
			for(int i=board.numHouses-1; i>=0; --i){
				if(board.validMove(i) && board.willHitKalah(i)){
					moves.add(i);
					board.moveSeeds(i);
					hitKalah = true;
					break;
				}
			}
		}
		int capture = getBestCapture(board.housesAndKalahs);
		int loop = getBestLoop(board.housesAndKalahs);
		if(capture != -1){
			moves.add(capture);
			board.moveSeeds(capture);
		}
		else if(loop != -1){
			moves.add(loop);
			board.moveSeeds(loop);
		}
		return moves;
	}
	
	static int getBestCapture(int[] board){
		int numHouses = board.length/2-1;
		
		int max=0,maxI=-1;
		for(int i=0; i<numHouses; ++i){
			if(board[i] > board.length-1 || board[i] == 0) continue;
			
			if(board[i] == board.length-1 && board[board.length-2-i]+2 > max){
				maxI = i;
				max = board[board.length-2-i]+2;//land=i
				continue;
			}
			
			int land = i + board[i];
			int inKalah = 0;
			
			if(land >= board.length-1){
				land -= (board.length-1);
				++inKalah;
			}
			if(land < numHouses && board[land] == 0){
				inKalah += board[board.length-2-land];	
				if(inKalah > max){
					maxI = i;
					max = inKalah;
				}
			}
		}
		return maxI;
	}
	
	static int getBestLoop(int[] board){
		int numHouses = board.length/2-1;
		
		int max=-1,maxI=-1;
		for(int i=0; i<numHouses; ++i){
			if(board[i] == 0) continue;
			int inKalah = (board[i]-1) / (board.length-1);
			if(i+board[i] >= numHouses) ++inKalah;
			
			if(inKalah > max){
				max = inKalah;
				maxI = i;
			}
		}
		return maxI;
	}
}