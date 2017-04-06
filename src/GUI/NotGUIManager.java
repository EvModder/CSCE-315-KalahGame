package GUI;


public class NotGUIManager extends GUIManager{
	@Override public void openMenuWindow(){}
	@Override public void closeMenuWindow(){}
	@Override public void openWaitingWindow(){}
	@Override public void closeWaitingWindow(){}
	@Override public void connectionErrorWindow(){}
	@Override public void openGameOverWindow(GameResult ending){}
	@Override public void openGameErrorWindow(String message){}
}