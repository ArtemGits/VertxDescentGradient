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
	private static int workers = 10;
	private static int NUM = 1_000;
	private static Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(10));
	public static long startTime1;	
	public static long endTime1;
	public static long startTime2;	
	public static long endTime2;
	  // Convenience method so you can run it in your IDE
	  public static void main(String[] args) {
		  
		ArrayList<Point2D> data = loadData();
		startTime1 = System.currentTimeMillis();  
		GradientVerticle simpleGradient = new GradientVerticle(data);
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
	    MainVerticle parallelImplementation = new MainVerticle(data,workers,NUM);
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
	         for(int i=0; i<NUM; i++) {
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
		  
			GradientVerticle simpleGradient = new GradientVerticle(data);
			vertx.deployVerticle(simpleGradient, res-> {
				
//				if(res.succeeded()) {
//					vertx.undeploy(res.result(), v -> {
//						System.out.println("undeploy GradientVerticle");
//					});
//				} else {
//					
//				}
			});
			  
		    MainVerticle parallelImplementation = new MainVerticle(data,workers,NUM);
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
