package Main;

public class MoveTimer {
	private boolean alive;
	private Thread thread;
	
	public interface TimerListener{
		void timerEnded();
		void timeElapsed(long time);
	}
	
	public void startTimer(TimerListener listener, long timelimit){
		while(thread != null && thread.isAlive()) Thread.yield();
		
		long end = System.currentTimeMillis()+timelimit;
		alive = true;
		thread = new Thread(){
			@Override public void run(){
				long current;
				while(alive && (current = System.currentTimeMillis()) < end){
					listener.timeElapsed(end-current);
					yield();
				}
				//Time is up!
				listener.timeElapsed(0);
				if(alive){
					//If this timer was not cancelled (it ended naturally)
					listener.timerEnded();
					alive = false;
				}
			}
		};
		thread.start();
	}
	
	public void cancelTimer(){
		alive = false;
	}
}
