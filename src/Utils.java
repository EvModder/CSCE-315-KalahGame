import java.util.Scanner;

public class Utils {
	private static String inputStr = "";
	private static boolean waiting;
	private static Thread inputThread;
	
	//limit represents the number of milliseconds they have to enter something
	public static String getTimeLimitedInput(int limit){
		long start = System.currentTimeMillis();
		inputStr = "";
		waiting = true;
		if(inputThread == null || !inputThread.isAlive()){
			inputThread = new Thread(){
				@Override public void run(){
					Scanner scan = new Scanner(System.in);
					String input = scan.nextLine();
					if(waiting){
						inputStr = input;
						waiting = false;
					}
					scan.close();
				}
			};
		}
		while(waiting && System.currentTimeMillis()-start < limit);
		waiting = false;
//		inputThread.stop();
		return inputStr;
	}
}
