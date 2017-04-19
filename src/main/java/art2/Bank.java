package art2;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Bank {
//	private String content;
//	private String answer;
//	private int mark;
	
	private ArrayList<Point2D> bank = new ArrayList<>();
	private Bank[] workerBanks = new Bank[10];
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
	
	
	public void printBank() {
		for(int i =0; i<10; i++) {
			System.out.println(bank.get(i));
		}
	}
//	public String getContent() {
//		return content;
//	}
//	public void setContent(String content) {
//		this.content = content;
//	}
//	public String getAnswer() {
//		return answer;
//	}
//	public void setAnswer(String answer) {
//		this.answer = answer;
//	}
//	public int getMark() {
//		return mark;
//	}
//	public void setMark(int mark) {
//		this.mark = mark;
//	}
}