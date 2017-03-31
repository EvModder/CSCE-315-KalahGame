package old;
import java.util.Scanner;

public class Utils {
	private static String inputStr = "";
	private volatile static boolean waiting;
	private static Thread inputThread;
	private static Scanner scan = new Scanner(System.in);
	
	//limit represents the number of milliseconds they have to enter something
	public static String getTimeLimitedInput(int limit){
		long start = System.currentTimeMillis();
		inputStr = "";
		waiting = true;
		if(inputThread == null || !inputThread.isAlive()){
//			System.out.println("starting wait thread");
			inputThread = new Thread(){
				@Override public void run(){
					String input = scan.nextLine();
					if(waiting){
						inputStr = input;
						waiting = false;
					}
				}
			};
			inputThread.start();
		}
		while(waiting && (System.currentTimeMillis()-start < limit));
		waiting = false;
//		inputThread.stop();
		return inputStr;
	}
}
