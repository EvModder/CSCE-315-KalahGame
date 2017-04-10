package AI;
import java.util.ArrayList;
import java.util.List;
import Main.Board;

/*
 * A simple AI that picks moves which land in its Kalah
 */
public class DumbAI extends KalahPlayer{
	public DumbAI(Board board){super(board);}
	
	@Override public void applyOpponentMove(int move){board.moveSeeds(move);}
	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		
		//Pick things that land in the Kalah
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
		//See if there is an available capture
		int capture = getBestCapture(board);
		if(capture != -1){
			moves.add(capture);
			board.moveSeeds(capture);
		}
		else{
			//See if a number loops around the board (more seeds)
			int loop = getBestLoop(board);
			if(loop != -1){
				moves.add(loop);
				board.moveSeeds(loop);
			}
		}
		return moves;
	}
	
	static int getBestCapture(Board board){
		int max=0,maxI=-1;
		for(int i=0; i<board.numHouses; ++i){
			if(board.housesAndKalahs[i] == 0 || board.housesAndKalahs[i] > board.kalah2) continue;
			
			int capture = board.housesAndKalahs.length-2-i;
			if(board.housesAndKalahs[i] == board.kalah2){
				if(board.housesAndKalahs[capture]+2 > max){
					maxI = i;//land = i
					max = board.housesAndKalahs[capture]+2;
				}
			}
			else{
				int land = i + board.housesAndKalahs[i];
				int inKalah = 0;
				
				if(land >= board.kalah2){
					land -= board.kalah2;
					++inKalah;
				}
				if(land < board.numHouses && board.housesAndKalahs[land] == 0){
					inKalah += board.housesAndKalahs[capture];	
					if(inKalah > max){
						maxI = i;
						max = inKalah;
					}
				}
			}
		}
		return maxI;
	}
	
	static int getBestLoop(Board board){
		int max=-1,maxI=-1;
		for(int i=0; i<board.numHouses; ++i){
			if(board.housesAndKalahs[i] == 0) continue;
			int inKalah = (board.housesAndKalahs[i]-1) / (board.kalah2);
			if(i+board.housesAndKalahs[i] >= board.numHouses) ++inKalah;
			
			if(inKalah > max){
				max = inKalah;
				maxI = i;
			}
		}
		return maxI;
	}
}