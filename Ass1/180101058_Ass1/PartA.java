import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PartA implements Runnable {
    private static AtomicInteger globalCount = new AtomicInteger();     // Atomic integer for counting no. of points inside unit circle
    private static int noOfThreads;     
    private static int n = 1000000;     // No. of points
    private int count;          // Count of points to be calculated in each thread
    public PartA(int count){
        this.count = count;
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify the no. of threads as argument. See README.txt for steps to run");
            return;
        }
        noOfThreads = Integer.parseInt(args[0]);
        ArrayList<Thread> threads = new ArrayList<>();      // List for temporarily storing threads

        int div = n/noOfThreads;                            
        int rem = n%noOfThreads;
        for (int i = 0; i < noOfThreads; i++) {            
            int cn = div;
            if(i<rem) cn = div+1;                           // Calculating no. of point to be calculated in each thread
            PartA app = new PartA(cn);                          // Creating runnables
            Thread thread = new Thread(app);
            threads.add(thread);                            // Storing threads in the list
            thread.start();                                 // Starting new threads for doing work
        }   

        for (Thread thread : threads) {                     // Join threads
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double estimatedValueOfPi = ((double)globalCount.get() * 4) / n;  // After all threads have completed, calculate the estimated value of PI 
        System.out.println(estimatedValueOfPi);    
    }

    @Override
    public void run() {
        Random rand = new Random(); 
        int localCount=0;
        for(int i=0;i<count;i++){
            double x = rand.nextDouble();           // Choosing x,y as random double from 0 to 1
            double y = rand.nextDouble();  
            if(checkInside(x,y)){
                localCount++;                       // Increment local count if point inside circle 
            }
        }
        globalCount.addAndGet(localCount);          // Add the local count to the global count
    }

    private boolean checkInside(double x, double y){
        return (x*x+y*y <= 1);
    }

}

