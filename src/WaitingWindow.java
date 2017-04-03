package GUI;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class WaitingWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private Timer waitingTimer;

	WaitingWindow(GUIManager guiHandler){
		setTitle("Waiting to start");
		setIconImage(guiHandler.icon);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel waitingLabel = new JLabel("Waiting for opponent", SwingConstants.CENTER);
		setSize(300, 100);
		add(waitingLabel);
		setLocationRelativeTo(null);
		setVisible(true);
		
		waitingTimer = new Timer();
		waitingTimer.schedule(new TimerTask(){@Override public void run() {
			waitingLabel.setText((" "+waitingLabel.getText()+'.').replace("....", "").replace("    ", ""));
			repaint();
		}}, 500, 300);
	}
	
	@Override public void dispose(){
		waitingTimer.cancel();
		super.dispose();
	}
}
