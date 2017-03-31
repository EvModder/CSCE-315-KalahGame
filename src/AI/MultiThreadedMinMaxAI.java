package AI;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadedMinMaxAI extends MinMaxAI{
	boolean weHaveTime;
	List<Integer> moves = new ArrayList<Integer>();
	
	//Override tree generation to use multiple threads (per possible move)
}