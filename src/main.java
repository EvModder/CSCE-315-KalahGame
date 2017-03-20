// to test methods
public class main {
	public static void main(String[] args) {
		Board myBoard = new Board(6,4); 
		while (myBoard.keepPlaying){
			myBoard.setHouseSeed(6, 40);
			myBoard.setHouseSeed(13, 41);
			myBoard.displayOutcome();
			for (int i=0; i<14; i++){
				System.out.println(myBoard.getHouseSeed(i));
			}
			
			myBoard.replayOrQuit("Replay");
			for (int i=0; i<14; i++){
				System.out.println(myBoard.getHouseSeed(i));
			}
			myBoard.displayOutcome();
			
			myBoard.replayOrQuit("Quit");
		}
		System.out.println("done");
	}
}
