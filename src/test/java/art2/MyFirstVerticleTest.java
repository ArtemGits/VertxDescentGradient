package art2;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class MyFirstVerticleTest {

  private Vertx vertx;
  private static  Async async;
  private static int workers = 10;
  private static int NUM = 1_000;
  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    
    
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testMyApplication(TestContext context) {
//    
//	  ArrayList<Point2D> data = loadData();
//	  GradientVerticle simpleGradient = new GradientVerticle(data);
//	  vertx.deployVerticle(simpleGradient,context.asyncAssertSuccess());
//  	  
////	System.out.println("-------------------------------------------------Test?-----------------------------------");
////	  
////	//async  = context.async();
//////		Main.startTime2 = System.currentTimeMillis();
////	ArrayList<Point2D> data = loadData();  
////    MainVerticle parallelImplementation = new MainVerticle(data,workers,NUM);
////	vertx.deployVerticle(parallelImplementation,context.asyncAssertSuccess());
////   
////	System.out.println("-------------------------------------------------End?-----------------------------------"); 
      
  }
  
  
  public static ArrayList<Point2D> loadData() {
 	 ArrayList<Point2D> data = new ArrayList<>();
      Random rand = new Random();
      for(int i=0; i<NUM; i++) {
      	data.add(new Point2D.Double(new BigDecimal(rand.nextDouble()).setScale(2, RoundingMode.UP).doubleValue(), new BigDecimal(rand.nextDouble()).setScale(2, RoundingMode.UP).doubleValue()));

      }
//      System.out.println("------------------------------");
//      System.out.println(data);
//      System.out.println("------------------------------");
     return data;
 }
}