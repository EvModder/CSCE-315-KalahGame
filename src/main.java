// to test methods
public class main {
	public static void main(String[] args) {
		Board my_board = new Board(6,4); 
		while (my_board.keep_playing){
			my_board.set_house_seed(6, 40);
			my_board.set_house_seed(13, 41);
			my_board.display_outcome();
			for (int i=0; i<14; i++){
				System.out.println(my_board.get_house_seed(i));
			}
			
			my_board.replay_or_quit("Replay");
			for (int i=0; i<14; i++){
				System.out.println(my_board.get_house_seed(i));
			}
			my_board.display_outcome();
			
			my_board.replay_or_quit("Quit");
		}
		System.out.println("done");
	}
}
