import java.util.ArrayList;

public class Board {
	private ArrayList<House> all_houses = new ArrayList<House>();
	public Boolean keep_playing = true;
	private int init_houses;
	private int init_seeds;
	
	public Board(int num_houses, int num_seeds) {
		this.init_houses = num_houses;
		this.init_seeds = num_seeds;
		// create all houses and 2 more for the kalahs
		for (int i=0; i<(num_houses*2)+2; i++){
			this.all_houses.add(new House(num_seeds));
		}
	}
	
	public void replay_or_quit(String option){
		if (option == "Quit"){
			keep_playing = false;
		}
		else if (option == "Replay"){
			ArrayList<House> restart = new ArrayList<House>();
			for (int i=0; i<(init_houses*2)+2; i++){
				restart.add(new House(init_seeds));
			}
			this.all_houses = restart;
		}
		else{
			System.out.println("Not a valid option. Please enter Quit or Replay.");
		}
	}
	
	public void display_outcome(){
		if (all_houses.get(all_houses.size()/2-1).get_num_seeds() > 
			all_houses.get(all_houses.size()-1).get_num_seeds()) {
			System.out.println("Player 1 is the winner.");
		}
		else if(all_houses.get(all_houses.size()/2-1).get_num_seeds() < 
				all_houses.get(all_houses.size()-1).get_num_seeds()) {
			System.out.println("Player 2 is the winner.");
		}
		else {
			System.out.println("There is a tie.");
		}
	}
	
	public int get_house_seed(int house_number){
		return all_houses.get(house_number).get_num_seeds();
	}
	
	public void set_house_seed(int house_number, int num_seeds){
		this.all_houses.get(house_number).set_num_seeds(num_seeds);
	}
	
	public ArrayList<House> get_all_houses() {
		return all_houses;
	}
}