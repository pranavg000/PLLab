import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PartC implements Runnable {
    private static int n = 1000; // Dimension of matrices
    private static int[][] A, B, C;
    private int start, count; // Start row no. and no. of rows for each thread
    private boolean flag; // Flag for first initializing, then multiplying

    public PartC(int start, int count, boolean flag) { // Setting values for start, count, flag
        this.start = start;
        this.count = count;
        this.flag = flag;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid no. of arguments. See README.txt for steps to run");
            return;
        }
        int noOfThreads = Integer.parseInt(args[0]); // Taking noOfThreads as argument
        int showOutput = Integer.parseInt(args[1]);

        A = new int[n][n]; // Creating A,B,C of size n x n
        B = new int[n][n];
        C = new int[n][n];

        ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads); // Make a thread pool of size
                                                                                     // noOfThreads

        int rem = n % noOfThreads;
        int div = n / noOfThreads;
        int it = 0;
        int cn;

        for (int i = 0; i < noOfThreads; i++) {
            if (i < rem)
                cn = div + 1;
            else
                cn = div;
            PartC app = new PartC(it, cn, true); // Creating runnable for initializing the matrices
            it += cn;
            executorService.submit(app); // Submitting runnables to executor service for initializations
        }

        it = 0;
        for (int i = 0; i < noOfThreads; i++) {
            if (i < rem)
                cn = div + 1;
            else
                cn = div;
            PartC app = new PartC(it, cn, false); // Creating runnable for multiplying
            it += cn;
            executorService.submit(app); // Submitting runnables to executor service for matrix multiplication
        }

        executorService.shutdown(); // Shutting down thread pool
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Matrix multiplication completed successfully!");

        if(showOutput == 1){
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    System.out.printf("%d ", A[i][j]);
                }
                System.out.printf("\n");
            }
            System.out.printf("\n");
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    System.out.printf("%d ", B[i][j]);
                }
                System.out.printf("\n");
            }
            System.out.printf("\n");
    
            
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    System.out.printf("%d ", C[i][j]);
                }
                System.out.printf("\n");
            }
        }

        
    }

    @Override
    public void run() {
        if(flag){   // Initialize rows if flag=true (Rows are divided among threads)
            Random rand = new Random();
            for(int i=start;i<start+count;i++){ 
                for(int j=0;j<n;j++){
                    A[i][j]=rand.nextInt(11);       // Random integer between [0,10]
                    B[i][j]=rand.nextInt(11);       // Random integer between [0,10]
                }
            }
        }
        else{   // Multiply matrices if flag=false (Rows are divided among threads)
            for(int i=start;i<start+count;i++){
                for(int j=0;j<n;j++){
                    C[i][j] = 0;
                    for(int k=0;k<n;k++){
                        C[i][j]+=A[i][k]*B[k][j];
                    }
                }
            }
        }
    }
    
}

