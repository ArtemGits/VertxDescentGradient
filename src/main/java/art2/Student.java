package art2;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class Student  {
	private int id;
	private ArrayList<Point2D> LocalArr;
	private CountDownLatch countDown;
	private int sum = 0;
	private double resultTemp;
	private double resultTemp2;
	private double theta0;
	private double theta1;
	private SimpleGradientDescent temp = new SimpleGradientDescent();
	public Student(int id, ArrayList<Point2D> arr, CountDownLatch cdl) {
		this.countDown = cdl;
		this.LocalArr = arr;
		this.id = id;
	}
	
	public void setThetas(double val1, double val2) {
		theta0 = val1;
		theta1 = val2;
	}
	
	public void printBanks() {
		for(int i=0; i<LocalArr.size(); i++) {
			System.out.println(LocalArr.get(i));
		}
	}
	
	public double getTempResult2() {
		return resultTemp2;
	}
	
	public void run() {
		//System.out.println("Thread: " + id);
		
			//sum += id;
		   // System.out.print(" Id: " + id);
			 	temp.setResult();
			 	temp.setResult2();
			  temp.SectionForWorkers(LocalArr, theta0, theta1); //1.0
			  resultTemp =  temp.getResult();
			  //temp.SectionForWorkers2(LocalArr, 0.1, 0.1, x-> x);
			  resultTemp2 = temp.getResult2();
		
		//System.out.println("Result: " + resultTemp);
		
		countDown.countDown();
		
//			try {
//				countDown.await();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		
		//System.out.println("Поток завершил работу");
	}
	public int getBankSize() {
		return LocalArr.size();
	}
	
	public double getX(int i) {
		return LocalArr.get(i).getX();
	}
	
	public double getTempResult() {
		return resultTemp;
	}
	
	public int getSum() {
		return sum;
	}
}