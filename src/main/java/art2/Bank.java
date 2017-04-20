package art2;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Bank {
	
	private ArrayList<Point2D> bank = new ArrayList<>();
	public Bank() {
//		for(int i =0; i<10; i++) {
//			bank.add(new Point2D.Double(0.0,0.0));
//		}
	}
	
	public ArrayList<Point2D> getBank() {
		return bank;
	}
	
	public void setBank(int i, Point2D point) {
		bank.add(point); 
	}
	
	
	
}