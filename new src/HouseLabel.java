
import javax.swing.JLabel;

public class HouseLabel extends JLabel implements KalahSquare{
	private static final long serialVersionUID = 1L;
	
	HouseLabel(int i){
		super("0");
	}

	@Override
	public void setSeeds(int i) {
		setText(""+i);
	}

	@Override
	public int getSeeds() {
		return Integer.parseInt(getText());
	}
	
	@Override
	public void addSeeds(int i) {
		setSeeds(i+getSeeds());
	}
}
