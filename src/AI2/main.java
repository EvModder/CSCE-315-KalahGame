package AI2;

import java.util.*;

public class main{
	public static void main(String[] args){
		Board myBoard = new Board(6,4); 
		Kalah myKalah = new Kalah(myBoard);
		myKalah.displayIntro();
		String userInput;
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter Easy, Medium, or Hard");
		userInput = scan.nextLine();
		if (userInput.equals("Easy")){
			myKalah.play(1);
		}
		else if (userInput.equals("Medium")){
			myKalah.play(4);
		}
		else if (userInput.equals("Hard")){
			myKalah.play(9);
		}
	}
}