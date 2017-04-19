package art2;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.function.DoubleBinaryOperator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * An example of worker verticle
 */
public class WorkerVerticle extends AbstractVerticle {
	
	
	//private static double sum1 =0.0 ,sum2= 0.0,sum3  = 0.0;
	private static int id= 1;
	private static double theta0 = 0.1;
	private static double theta1 = 0.1;
	private ArrayList<Point2D> bank;
	//private static List<ArrayList<Point2D>> queueCopy = new ArrayBlockingQueue<ArrayList<Point2D>>(10);
	public  double temp1;
	public  double temp2;
	private boolean FINISH;
	
	
	public WorkerVerticle(ArrayList<Point2D> bank) {
		this.bank =  new ArrayList<Point2D>(bank.size());
		this.bank.addAll(bank);
		temp1 =0.0;
		temp2 = 0.0;
	}
	public WorkerVerticle() {
		
	}
	
	public static void setTheta1(double val) {
		theta1 = val;
	}

	public static void setTheta0(double val) {
		theta0 = val;
	}
	
	
  public void calculate() {
	  temp2 = 0.0;
	  temp1 = 0.0;
	  //
	  //System.out.println("Theta0: " + theta0 + "Theta1: " + theta1);
	  //System.out.println("[Worker] Starting in " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());	  		    	  
	  //System.out.println(bank);
	  //System.out.println(temp(bank,theta0,theta1));
	  vertx.eventBus().send("temp1",  temp(bank, theta0,theta1), reply -> {
      if (reply.succeeded()) {
      	theta0 = (double) reply.result().body();
      } else {
        System.out.println("No reply1");
      }
    });
//	
	  vertx.eventBus().send("temp2", temp2(bank, theta0,theta1), reply -> {
      if (reply.succeeded()) {
       theta1 = (double) reply.result().body();
      // System.out.println(theta0 + " " + theta1);
       vertx.eventBus().send("NEXT", "");
      } else {
        System.out.println("No reply2");
      }
    });
	    
  }
  
  
  
  public double sigma(List<Point2D> data, DoubleBinaryOperator inner) {
  	
  	//System.out.println(data);
  	double temp = data.stream()
              .mapToDouble(theta -> {
                  double x = theta.getX(), y = theta.getY();
                  return inner.applyAsDouble(x, y);
              })
              .sum();
  	//System.out.println(temp);
  	return temp; 
  }
 
  

  public double temp(ArrayList<Point2D> bank,double theta0, double theta1) {
  	double hyp = 0.0, tmp = 0.0,tmp2=0.0,sum=0.0;
  //	System.out.println("BANK to processing: " + bank);
  	for(int i=0; i<bank.size(); i++) {
  		hyp = hypothesis(theta0, theta1, bank.get(i).getX());
  		//System.out.println("Hypotesa: " + hyp);
  		tmp = hyp - bank.get(i).getY();
  		
  		//tmp2 = tmp * bank.get(i).getX();
  		//resultTheta2 = resultTheta2 +  tmp2;
  		
  		sum = sum +  tmp;
  		
  				
  	}
  //	System.out.println("Processing sum________________ "+ sum);
 return sum; 	

  }
  
  public double temp2(ArrayList<Point2D> bank,double theta0, double theta1) {
  	//System.out.println(bank);
	  double hyp = 0.0, tmp = 0.0,tmp2=0.0,sum=0.0;
  	for(int i=0; i<bank.size(); i++) {
  		hyp = hypothesis(theta0, theta1, bank.get(i).getX());
  		//System.out.println("Hypotesa: " + hyp);
  		tmp = new BigDecimal(hyp - bank.get(i).getY()).setScale(5, RoundingMode.HALF_DOWN).doubleValue();
  		
  		tmp2 = new BigDecimal(tmp * bank.get(i).getX()).setScale(5, RoundingMode.HALF_DOWN).doubleValue();
  		//resultTheta2 = resultTheta2 +  tmp2;
  		sum = new BigDecimal(sum  +  tmp2).setScale(5, RoundingMode.HALF_DOWN).doubleValue();
  				
  	}
 return sum; 	

  }  
  
  
  private double hypothesis(double theta0, double theta1, double x) {
      return theta0 + (theta1 * x);
  }
  
  
  @Override
  public void start() throws Exception {
	 // System.out.println(bank);
	  calculate();
	  //fut.complete();
	  //MainVerticle.FINISH.arriveAndAwaitAdvance();
	  	  
	  
  }
  
  
  @Override
  public void stop() throws Exception {
	  //results.add(1,resultTemp);
	  //results.add(2,resultTemp2);
	  //System.out.println(results);
	  
  }
  public void setResults() {
	  temp1 = 0.0;
	  temp2 = 0.0;
  }
  
  
}
