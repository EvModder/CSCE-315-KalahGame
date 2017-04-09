package AI;
import java.util.ArrayList;
import java.util.List;

import Main.Board;

public class StrategicAI extends KalahPlayer{
	public StrategicAI(Board board){super(board);}
	static class Pair{int i,v; Pair(int a, int b){i=a;v=b;}}
	int moveValue = Integer.MIN_VALUE, lastValue;
	
	@Override public void applyOpponentMove(int move){board.moveSeeds(move);}
	@Override public List<Integer> getMove(){
		boolean outterMost = (moveValue == Integer.MIN_VALUE);
		List<Integer> moves = new ArrayList<Integer>();
		
		Pair capture = getBestCapture(board);
		int captureValue=-1;
		Board boardIfCapture = board.getCopy();
		if(capture.i != -1){
			boardIfCapture.moveSeeds(capture.i);
			captureValue = calculateUtilityValue(boardIfCapture);
		}
		
		boolean noMove = true;
		for(int i=board.numHouses-1; i>=0; --i){
			if(board.validMove(i) && board.willHitKalah(i)){
				board.moveSeeds(i);
				List<Integer> futureMoves = getMove();
				if(capture.i == -1 || moveValue >= captureValue){
					moves.add(i);
					moves.addAll(futureMoves);
				}
				else{
					board = boardIfCapture;//reset board
					moves.add(capture.i);
					board.moveSeeds(capture.i);
				}
				noMove = false;
				break;
			}
		}
		if(noMove){
			if(capture.i != -1){
				moves.add(capture.i);
				board.moveSeeds(capture.i);
			}
			else{
				int loop = getBestLoop(board);
				if(loop != -1){
					moves.add(loop);
					board.moveSeeds(loop);
				}
			}
		}
		if(outterMost){
			lastValue = moveValue;
			moveValue = Integer.MIN_VALUE;
		}
		else moveValue = Math.max(calculateUtilityValue(board), moveValue);
		return moves;
	}
	
	private int calculateUtilityValue(Board board){
		if(!board.gameNotOver()) board.collectLeftoverSeeds();
		return board.getScoreDifference() + board.getSeedDifference()/board.kalah2;//kalah2 = length-1
	}
	int getUtilityValue(){
		return lastValue;
	}
	
	static Pair getBestCapture(Board board){
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
		return new Pair(maxI, max);
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