package art2;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class GradientVerticle extends AbstractVerticle {

    private static double epsilon;
    private ArrayList<Point2D> data;
    private static double alpha;
    private static int maxIterations;
    private static double theta0;
    private static double theta1;
  
    public GradientVerticle(ArrayList<Point2D> data, double alpha, int maxIterations, double epsilon, double theta0, double theta1) {
    	this.data = data;
    	this.alpha = alpha;
    	this.maxIterations = maxIterations;
    	this.epsilon = epsilon;
    	this.theta0 = theta0;
    	this.theta1 = theta1;
    	
    }
    
    @Override
    public void start() throws Exception {
    	//System.out.println("RUN");
    	run();
    }
    
    public void run() {
      vertx.executeBlocking(future -> {
        Point2D result  = singleVarGradientDescent(data, theta0, theta1, alpha, maxIterations);;
        future.complete(result);
      }, res -> {
    	Main.endTime1 = System.currentTimeMillis();    
        System.out.println("The result from GradientVerticle is: " + res.result() + " Time: " + (Main.endTime1 - Main.startTime1));
      });     
    }
    
    
    
    

    private Point2D singleVarGradientDescent(List<Point2D> data, double initialTheta0, double initialTheta1, double alpha, int maxIterations) {
        double theta0 = initialTheta0, theta1 = initialTheta1;
        double oldTheta0 = 0, oldTheta1 = 0;

        for (int i = 0 ; i < maxIterations; i++) {
            if (hasConverged(oldTheta0, theta0) && hasConverged(oldTheta1, theta1))
                break;

            oldTheta0 = theta0;
            oldTheta1 = theta1;

            theta0 = theta0 - (alpha * gradientofThetaN(theta0, theta1, data, x -> 1.0));
            theta1 = theta1 - (alpha * gradientofThetaN(theta0, theta1, data, x -> x));
           // System.out.println("Iterations from sequence method: " + i);
        }
        return new Point2D.Double(theta0, theta1);
    }

    private boolean hasConverged(double old, double current) {
        //System.out.println("TRue1");
    	return (current - old) < epsilon;
    }

    private double gradientofThetaN(double theta0, double theta1, List<Point2D> data, DoubleUnaryOperator factor) {
        double m = data.size();
        return (1.0 / m) * sigma(data, (x, y) ->  (hypothesis(theta0, theta1, x) - y) * factor.applyAsDouble(x));
    }

    
    
    private double hypothesis(double theta0, double theta1, double x) {
        return theta0 + (theta1 * x);
    }

    private double sigma(List<Point2D> data, DoubleBinaryOperator inner) {
        
    	double temp = data.stream()
                .mapToDouble(theta -> {
                    double x = theta.getX(), y = theta.getY();
                    return inner.applyAsDouble(x, y);
                })
                .sum();
    	//System.out.println(temp);
    	return temp; 
 } 
    	
    	

    
}