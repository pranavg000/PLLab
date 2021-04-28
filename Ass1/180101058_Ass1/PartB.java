import java.util.ArrayList;

public class PartB implements Runnable {
    private static double sum;          // Variable to hold the total sum
    private static int n = 10000000;    // No. of points 10^7
    private static double delta;        // Delta between consecutive points  
    public static double lowerLimit, upperLimit;    // Lower & Upper limit of integral
    private int start;                  // Helper variable for each thread
    private static int count;           // No. of points each thread evaluates at

    public PartB(int start){              // Initializing value of start
        this.start = start;
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify the no. of threads as argument. See README.txt for steps to run");
            return;
        }
        int noOfThreads = Integer.parseInt(args[0]);    // Get noOfThreads as argument
        ArrayList<Thread> threads = new ArrayList<>();  // List for temporarily storing threads

        n = (n+noOfThreads-1)/noOfThreads*noOfThreads;  // Increasing n so that it is multiple of noOfThreads
        lowerLimit = -1;                                // Initializing values
        upperLimit = 1;
        sum = 0;
        delta = (upperLimit-lowerLimit)/n;
        count = n/noOfThreads;
        for (int i = 0; i < noOfThreads; i++) {         
            PartB app = new PartB(i*count);                 // Creating runnables
            Thread thread = new Thread(app);
            threads.add(thread);                        // Storing threads in the list
            thread.start();                             // Start new threads for doing work
        }

        for (Thread thread : threads) {                 // Join threads
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double estimatedIntegral = sum*delta/3;         // After all threads have completed, calculate the estimated integral
        System.out.println(estimatedIntegral);
    }

    @Override
    public void run() {
        double localSum = 0;                    // Using local sum to increase performance
        double val = lowerLimit + delta*start;  // Initializing val
        for(int i=start;i<start+count;i++){     // Calculating func value for each point and adding to localsum
            localSum += (func(val)*multi(i));
            val+=delta;
        }
        addToSum(localSum);                     // Add localsum to global sum
    }

    private static synchronized void addToSum(double localSum){     // Synchronized method to add local sum in global sum
        sum += localSum;
    }

    private int multi(int x){           // Helper fn for calculted the scalar factor for each value at each point
        if(x==0 || x==n-1) return 1;
        if(x%2==0) return 4;
        return 2; 
    }

    private double func(double x){      // Calculates the value of the fn inside the integral at the given x
        return Math.exp(-x*x/2)/Math.sqrt(2*Math.PI);
    }
}

