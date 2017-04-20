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


public class MainVerticle extends AbstractVerticle {

	private static ArrayList<Point2D> dataSet; 
	private static Bank[] banks; 
    private  double epsilon;
 
	
	private static double sigma1 = 0.0;
	private static double sigma2 = 0.0;
	private int idw = 0;
	private int idw2 = 0;
	private int iterations = 0;	
	private  int m;
	private  double theta0;
	private  double theta1;
	private  double oldTheta0 = 0.0;
	private  double oldTheta1 = 0.0;
	private  double alpha;
	private int maxIterations;
	private int workers;

	  
	  public MainVerticle(ArrayList<Point2D> data, int workers,int val, double alpha,int maxIterations,double epsilon,double theta0, double theta1) {
		  m = val;
		  this.alpha = alpha;
		  this.theta0=theta0;
		  this.theta1 = theta1;
		  this.epsilon = epsilon;
		  this.maxIterations = maxIterations; 
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
		  //bankSize.System.out.println(bankSize + " " + ostatok);
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
		  calc();
	  }

	  public void calc() {
		for(int i = 0; i<workers; i++) {
			 vertx.deployVerticle(new WorkerVerticle(banks[i].getBank()),new DeploymentOptions().setWorker(true), res-> {
				 if(res.succeeded()) {
					 vertx.undeploy(res.result());
				 }
			 }); 
		}
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
							 message.reply(theta1);
						 }

				 
			 });
			 
			 vertx.eventBus().consumer("NEXT", v->{
				 	iterations++;
					//System.out.println("Iterations: "+ iterations + " oldThetha0: " + oldTheta0 + " oldTheta1: " + oldTheta1 );
					if ( (iterations < maxIterations) && ( !(hasConverged(oldTheta0, theta0) && hasConverged(oldTheta1, theta1)) ) )  {
											oldTheta1 = theta1;
							oldTheta0 = theta0;
							for(int i = 0; i<workers; i++) {
								 vertx.deployVerticle(new WorkerVerticle(banks[i].getBank()),new DeploymentOptions().setWorker(true), res-> {
									 if(res.succeeded()) {
										 vertx.undeploy(res.result());
									 }
								 }); 
							}
					} else {
						Main.endTime2 = System.currentTimeMillis();    
					 	System.out.println("new Hope");
						System.out.println("Theta0: " + theta0 + " Theta1: " + theta1);
						System.out.println("Time: " + (Main.endTime2 - Main.startTime2));
					}
			 });
	  }
				  
	  
	public  boolean hasConverged(double old, double current) {
		        return ( current - old) < epsilon;
		    }
	  
	}
