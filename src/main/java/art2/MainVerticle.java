package art2;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;



/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class MainVerticle extends AbstractVerticle {

	private static ArrayList<Point2D> dataSet; 
	private static Bank[] banks; 
	private static ArrayList<String> ID = new ArrayList<String>(10);
	private static CountDownLatch cdl = new CountDownLatch(10);
	private static SimpleGradientDescent grad = new SimpleGradientDescent();
	public static final Phaser FINISH = new Phaser(10);
    private static final double epsilon = 0.0001;
 
	private static double temp1;
	
	private static double sigma1 = 0.0;
	private static double sigma2 = 0.0;
	private int idw = 0;
	private int idw2 = 0;
	private static int workers1 = 10;
	private static int workers2 = 10;
	private int iterations = 0;
	
	private  int m;
	
	private static double temp2;
	private static double theta0 = 0.1;
	private static double theta1 = 0.1;
	private static double oldTheta0 = 0.0;
	private static double oldTheta1 = 0.0;
	private double alpha = 0.01;
	private static double sum1 = 0.0, sum2 = 0.0;
	
	private int workers;

//	static Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(10));
//	
//	
//	  // Convenience method so you can run it in your IDE
//	  public static void main(String[] args) {
//	    MainVerticle main = new MainVerticle();
//		tempFunc();
//		vertx.deployVerticle("art2.MainVerticle");
//		//Runner.runExample(MainVerticle.class);
//	  }
//	  
	  
	  public MainVerticle(ArrayList<Point2D> data, int workers,int val) {
		  m = val;
		  this.workers = workers;
		  dataSet = data;
		  banks = new Bank[workers];
		  prepare();
	  }
	  
	  public  void prepare() {
		//create workers banks of data
		  int bankSize =  dataSet.size() / workers;
		  int ostatok = dataSet.size() % workers;
		  int iterator = 0;
		  System.out.println(bankSize + " " + ostatok);
			for(int i=0; i<workers; i++) {
				banks[i] = new Bank();
				
				if(i == (workers-1 ) ) {
					
					for(int j=0; j<dataSet.size(); j++) {
						if(j == (bankSize+ostatok)) {
							break;
						}
						banks[i].setBank(j, dataSet.get(j+iterator));
						
					}
					iterator+=bankSize+ostatok;
					
				} else {
					for(int j=0; j<dataSet.size(); j++) {
						if(j == (bankSize)) {
							break;
						}
						banks[i].setBank(j, dataSet.get(j+iterator));
						
					}
					iterator+=bankSize;
				}
				
				
			}
			
//			for(int i=0; i<workers; i++) {
//				System.out.println(banks[i].getBank());
//			}
			
	  }

	  
	  
	  @Override
	  public void start() throws Exception {
		  //long startTime = System.currentTimeMillis();	
		  calc();
		  //long endTime = System.currentTimeMillis();
		 // System.out.println("Time: " + (endTime - startTime));
		//-------------------------------------------
	  }
	  public void calc() {
		 // double initialTheta0  =0.1;
		//	double initialTheta1 = 0.1;
			
		//	theta0 = initialTheta0;
		//	theta1 = initialTheta1;
		//	double oldTheta0 = 0, oldTheta1 = 0;
			
			//worker.setThetas(theta0, theta1);
			
			
		//System.out.println("[Main] Running in " + Thread.currentThread().getName());
		
		//System.out.println(banks[0].getBank());
		
		
		for(int i = 0; i<workers; i++) {
			 vertx.deployVerticle(new WorkerVerticle(banks[i].getBank()),new DeploymentOptions().setWorker(true), res-> {
				 if(res.succeeded()) {
					 vertx.undeploy(res.result());
				 }
			 }); 
		}

		
		//for(int j = 0; j<2; j++) {
			
			
			//have async result
			vertx.eventBus().consumer("temp1", message -> {
				  sigma1 = sigma1 + (double)message.body();
				  
				  idw++;
					 //System.out.println("1 "+idw);
					 if(idw == workers) {
						 //System.out.println("Sigma: " + sigma1);
						 theta0 = theta0 - (alpha * (1.0 / m) * sigma1 );
						 //System.out.println("Theta0: " + theta0);
						 sigma1 = 0.0;
						 
						 idw = 0;
						 //WorkerVerticle.setTheta0(theta0);
						 message.reply(theta0);
					 }	  
				//message.reply(0.0);	      
			 });
			 vertx.eventBus().consumer("temp2", message -> {
				  sigma2 = sigma2 + (double)message.body();
				  
					 idw2++;
						// System.out.println("2 "+ idw2);
						 if(idw2 == workers) {
							 //System.out.println("Sigma2: " + sigma2);
							 theta1 = theta1 - (alpha * (1.0 / m) * sigma2 );
							 //System.out.println("Theta1: " + theta1);
							 sigma2 = 0.0;
							 idw2 = 0;
							 //WorkerVerticle.setTheta1(theta1);
							 message.reply(theta1);
						 }

				 
			 });
			 
			 vertx.eventBus().consumer("NEXT", v->{
				
				 	//System.out.println("new Hope");
					//System.out.println("Theta0: " + theta0 + "Theta1: " + theta1);
					//System.out.println("");
				 	
		            
				 	iterations++;
					//System.out.println("Iterations: "+ iterations + " oldThetha0: " + oldTheta0 + " oldTheta1: " + oldTheta1 );
					if ( (iterations < 10_000) && ( !(hasConverged(oldTheta0, theta0) && hasConverged(oldTheta1, theta1)) ) )  {
						try {
							 
						//	start();

							
							
							oldTheta1 = theta1;
							oldTheta0 = theta0;
							for(int i = 0; i<workers; i++) {
								 vertx.deployVerticle(new WorkerVerticle(banks[i].getBank()),new DeploymentOptions().setWorker(true), res-> {
									 if(res.succeeded()) {
										 vertx.undeploy(res.result());
									 }
								 }); 
							}

					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					} else {
						Main.endTime2 = System.currentTimeMillis();    
					 	System.out.println("new Hope");
						System.out.println("Theta0: " + theta0 + " Theta1: " + theta1);
						System.out.println("Time: " + (Main.endTime2 - Main.startTime2));
					}
			 });
			 
			// FINISH.awaitAdvance(0);
			
			 
			 
		//}
		
		
	  }
				  
		    
public void setTheta0(double val) {
	theta0 = val;
}
public void setTheta1(double val) {
	theta1 = val;
}

//public get
	  
	  
	  
	  
	  
		public  boolean hasConverged(double old, double current) {
			 
			// System.out.println((current - old) < epsilon);
			// System.out.println(epsilon);
		        return ( current - old) < epsilon;
		    }
	  
	}
