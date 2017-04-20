package art2;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main extends AbstractVerticle {
	private static final int workers = 8;
	private static final int POINTS = 1_000_000;
	private static final double epsilon = 0.0001;
	private static final double alpha = 0.01; //step
	private static final int maxIterations = 10_00;
	private static final double theta0 = 0.1; //start point
	private static final double theta1 = 0.1;
	private static Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(workers));
	public static long startTime1;	
	public static long endTime1;
	public static long startTime2;	
	public static long endTime2;

	public static void main(String[] args) {
		  
		ArrayList<Point2D> data = loadData();
		startTime1 = System.currentTimeMillis();  
		GradientVerticle simpleGradient = new GradientVerticle(data,alpha,maxIterations,epsilon,theta0,theta1);
		vertx.deployVerticle(simpleGradient, res-> {
			
//			if(res.succeeded()) {
//				vertx.undeploy(res.result(), v -> {
//					System.out.println("undeploy GradientVerticle");
//				});
//			} else {
//				
//			}
		});
		startTime2 = System.currentTimeMillis();    
	    MainVerticle parallelImplementation = new MainVerticle(data,workers,POINTS,alpha,maxIterations,epsilon,theta0,theta1);
		vertx.deployVerticle(parallelImplementation, res -> {
//			if(res.succeeded()) {
//				vertx.undeploy(res.result(), v -> {
//					System.out.println("undeploy MainVerticle");
//				});
//			} else {
//				
//			}
		});
	  }
	  
	  public static ArrayList<Point2D> loadData() {
	    	 ArrayList<Point2D> data = new ArrayList<>();
	         Random rand = new Random();
	         for(int i=0; i<POINTS; i++) {
	         	data.add(new Point2D.Double(new BigDecimal(rand.nextDouble()).setScale(2, RoundingMode.UP).doubleValue(), new BigDecimal(rand.nextDouble()).setScale(2, RoundingMode.UP).doubleValue()));

	         }
//	         System.out.println("------------------------------");
//	         System.out.println(data);
//	         System.out.println("------------------------------");
	        return data;
	    }
	  
	  
	  
	  @Override
	  public void start() {
	 
		  ArrayList<Point2D> data = loadData();
			startTime1 = System.currentTimeMillis();  
			GradientVerticle simpleGradient = new GradientVerticle(data,alpha,maxIterations,epsilon,theta0,theta1);
			vertx.deployVerticle(simpleGradient, res-> {
				
//				if(res.succeeded()) {
//					vertx.undeploy(res.result(), v -> {
//						System.out.println("undeploy GradientVerticle");
//					});
//				} else {
//					
//				}
			});
			startTime2 = System.currentTimeMillis();    
		    MainVerticle parallelImplementation = new MainVerticle(data,workers,POINTS,alpha,maxIterations,epsilon,theta0,theta1);
			vertx.deployVerticle(parallelImplementation, res -> {
//				if(res.succeeded()) {
//					vertx.undeploy(res.result(), v -> {
//						System.out.println("undeploy MainVerticle");
//					});
//				} else {
//					
//				}
			});
		  
	  }

	

}
