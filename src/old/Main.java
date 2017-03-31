package old;
public class Main{
	public static void main(String... args){
		Board myBoard = new Board(6,4); 
		Kalah myKalah = new Kalah(myBoard);
		myKalah.displayIntro();
		myKalah.play();
	}
}