public class Node{
	private double utilityVal;
	private double moveVal;
	
	public Node(){
		utilityVal = Double.NEGATIVE_INFINITY;
		moveVal = Double.NEGATIVE_INFINITY;
	}
	
	public Node(double utilityVal){
		this.utilityVal = utilityVal;
	}
	
	public double getUtilVal(){
		return utilityVal;
	}
	
	public double getMoveVal(){
		return moveVal;
	}
	
	public void setUtilVal(double val){
		utilityVal = val;
	}
	
	public void setMoveVal(double val){
		moveVal = val;
	}
}