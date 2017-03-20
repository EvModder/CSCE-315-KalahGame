import java.util.ArrayList;

public class Board {
	private ArrayList<House> allHouses = new ArrayList<House>();
	public Boolean keepPlaying = true;
	private int initHouses;
	private int initSeeds;
	
	public Board(int numHouses, int numSeeds) {
		this.initHouses = numHouses;
		this.initHeeds = numSeeds;
		// create all houses and 2 more for the kalahs
		for (int i=0; i<(numHouses*2)+2; i++){
			this.allHouses.add(new House(numSeeds));
		}
	}
	
	public void replayOrQuit(String option){
		if (option == "Quit"){
			keepPlaying = false;
		}
		else if (option == "Replay"){
			ArrayList<House> restart = new ArrayList<House>();
			for (int i=0; i<(initHouses*2)+2; i++){
				restart.add(new House(initSeeds));
			}
			this.allHouses = restart;
		}
		else{
			System.out.println("Not a valid option. Please enter Quit or Replay.");
		}
	}
	
	public void displayOutcome(){
		if (allHouses.get(allHouses.size()/2-1).getNumSeeds() > 
			allHouses.get(allHouses.size()-1).getNumSeeds()) {
			System.out.println("Player 1 is the winner.");
		}
		else if(allHouses.get(allHouses.size()/2-1).getNumSeeds() < 
				allHouses.get(allHouses.size()-1).getNumSeeds()) {
			System.out.println("Player 2 is the winner.");
		}
		else {
			System.out.println("There is a tie.");
		}
	}
	
	public int getHouseSeed(int houseNumber){
		return allHouses.get(houseNumber).getNumSeeds();
	}
	
	public void setHouseSeed(int houseNumber, int numSeeds){
		this.allHouses.get(houseNumber).setNumSeeds(numSeeds);
	}
	
	public ArrayList<House> getAllHouses() {
		return allHouses;
	}
}
