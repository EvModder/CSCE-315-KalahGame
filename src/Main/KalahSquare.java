package Main;

//A basic interface representing a square on a Kalah board
public interface KalahSquare{
	abstract void addSeeds(int i);
	abstract void setSeeds(int i);
	abstract int getSeeds();
}
