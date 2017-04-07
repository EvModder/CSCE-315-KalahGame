package Main;

public class MoveTimer {
	private boolean alive;
	private Thread thread;
	
	public interface TimerListener{
		void timerEnded();
		void timeElapsed(long time);
	}
	
	public void startTimer(TimerListener listener, long timelimit){
		if(thread != null && thread.isAlive()) return;//only supports 1 timer for now
		
		long end = System.currentTimeMillis()+timelimit;
		alive = true;
		thread = new Thread(){@Override public void run(){
			long current;
			while(alive && (current = System.currentTimeMillis()) < end){
				if(current % 100 == 0) listener.timeElapsed(end-current);
				yield();
			}
			if(alive){
				listener.timeElapsed(0);
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
