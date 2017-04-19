package art2;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class SimpleGradientDescent {

    private static final double epsilon = Double.MIN_VALUE;
    private ArrayList<Point2D> data;
    private double result = 0.0;
    private double resultTheta2 = 0.0;
   // private static  WorkerVerticle worker = new WorkerVerticle();
//    public static void main(String[] args) {
//        new SimpleGradientDescent().run();
//    }

    public void run() {
        data = loadData();
        double alpha = 0.01;
        int maxIterations = 10_000;
        Point2D finalTheta = singleVarGradientDescent(data, 0.1, 0.1, alpha, maxIterations);
        System.out.printf("theta0 = %f, theta1 = %f", finalTheta.getX(), finalTheta.getY());
        System.out.println("");
    }
    
    public ArrayList<Point2D> getData() {
    	return data;
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
        }
        return new Point2D.Double(theta0, theta1);
    }

    private boolean hasConverged(double old, double current) {
        return (current - old) < epsilon;
    }

    private double gradientofThetaN(double theta0, double theta1, List<Point2D> data, DoubleUnaryOperator factor) {
        double m = data.size();
        //System.out.println(sigma(data, (x, y) ->  (hypothesis(theta0, theta1, x) - y) * factor.applyAsDouble(x)));
        return (1.0 / m) * sigma(data, (x, y) ->  (hypothesis(theta0, theta1, x) - y) * factor.applyAsDouble(x));
    }

    //sum (hypothesis - y) * x
    //data, 0.1, 0.1, alpha, maxIterations
    //List<Point2D> data, double initialTheta0, double initialTheta1, double alpha, int maxIterations
    public void SectionForWorkers(ArrayList<Point2D> bank, double theta0, double theta1  ) {
    	
    	
    		//result =  sigma(data, (x, y) ->  (hypothesis(theta0, theta1, x) - y) * factor.applyAsDouble(x));
    		//resultTheta2 =  sigma(data, (x, y) ->  (hypothesis(theta0, theta1, x) - y) * factor.applyAsDouble(x));
    	//result = 0.0;
    	//resultTheta2 = 0.0;
    	
    	result =   temp(bank, theta0, theta1);
    	resultTheta2 =  temp2(bank,theta0, theta1);
    	
//    	worker.setTempResult2(resultTheta2);
//    	worker.setTempResult1(result);
    	//result = sigma(data, (x, y) ->  (hypothesis(theta0, theta1, x) - y) * factor.applyAsDouble(x));
    	
    	
//    	for(int i=0; i<10; i++) {
//    		result += bank.get(i).getX();
//    	}
    	//System.out.println("From Gradient: " + result);
    	
    }
//    
   
    
    public void setResult() {
    	result = 0.0;
    }
    
    public void setResult2() {
    	resultTheta2 = 0.0;
    }
    
    public double getResult2() {
    	return resultTheta2;
    }
    
    public double getResult() {
    	return result;
    }
    
    
    public double temp(ArrayList<Point2D> bank,double theta0, double theta1) {
    	double hyp = 0.0, tmp = 0.0,tmp2=0.0,sum=0.0;
    	for(int i=0; i<10; i++) {
    		hyp = hypothesis(theta0, theta1, bank.get(i).getX());
    		//System.out.println("Hypotesa: " + hyp);
    		tmp = hyp - bank.get(i).getY();
    		
    		tmp2 = tmp * bank.get(i).getX();
    		//resultTheta2 = resultTheta2 +  tmp2;
    		sum = sum +  tmp;
    				
    	}
   return sum; 	

    }
    
    public double temp2(ArrayList<Point2D> bank,double theta0, double theta1) {
    	double hyp = 0.0, tmp = 0.0,tmp2=0.0,sum=0.0;
    	for(int i=0; i<10; i++) {
    		hyp = hypothesis(theta0, theta1, bank.get(i).getX());
    		//System.out.println("Hypotesa: " + hyp);
    		tmp = hyp - bank.get(i).getY();
    		
    		tmp2 = tmp * bank.get(i).getX();
    		//resultTheta2 = resultTheta2 +  tmp2;
    		sum = sum  +  tmp2;
    				
    	}
   return sum; 	

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
    	
    	

    public  ArrayList<Point2D> loadData() {
    	 ArrayList<Point2D> data = new ArrayList<>();
         Random rand = new Random();
         for(int i=0; i<100; i++) {
         	data.add(new Point2D.Double(new BigDecimal(rand.nextDouble()).setScale(2, RoundingMode.UP).doubleValue(), new BigDecimal(rand.nextDouble()).setScale(2, RoundingMode.UP).doubleValue()));
          	//data.add(new Point2D.Double(rand.nextInt(10), rand.nextInt(10)));

         }
        
        
        return data;
    }

}