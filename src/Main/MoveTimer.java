package Main;

public class MoveTimer {
	private boolean alive;
	private Thread thread;
	
	public interface TimerListener{
		void timerEnded();
	}
	
	public void startTimer(TimerListener listener, long timelimit){
		if(thread != null && thread.isAlive()) return;//only supports 1 timer for now
		
		long end = System.currentTimeMillis()+timelimit;
		alive = true;
		thread = new Thread(){@Override public void run(){
			while(alive && System.currentTimeMillis() < end) yield();
			if(alive){
				listener.timerEnded();
				alive = false;
			}
		}};
		thread.start();
	}
	public void cancelTimer(){
		alive = false;
	}
}
