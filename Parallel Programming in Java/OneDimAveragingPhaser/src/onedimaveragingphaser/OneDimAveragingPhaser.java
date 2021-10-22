

package onedimaveragingphaser;

import java.util.concurrent.Phaser;

/**
 *
 * @author Mahmoud Mustafa
 */
public class OneDimAveragingPhaser {
    final int n ;
    double [] myNew ;
    double [] myVal;

    public OneDimAveragingPhaser(double[] myNew, double[] myVal,final int n) {
        this.myNew = myNew;
        this.myVal = myVal;
        this.n=n;
    }
    
    public void runSequential (int iterations){
        
        for(int itr =0 ; itr <= iterations ;itr++){
            
            for (int i = 1; i <= n; i++) {
                 myNew[i] = (myVal[i-1]+myVal[i+1])/2.0;
            }
            
            double[] temp = myNew;
            myNew=myVal;
            myVal=temp;
        }
    }
    
    
    public void runForallBarrier(final int iterations , final int tasks) {
        
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);
        
        Thread[] threads = new Thread[tasks];
        
        for (int j =0; j<tasks ; j++){
            int i = j;
            threads[j] = new Thread(()->{
            
            double[]myVal = this.myVal;
            double [] myNew = this.myNew;
            for(int itr =0 ; itr < iterations ;itr ++){
                
                int left = i *(n/tasks) +1;
                int right = (i+1)* (n/tasks);
                
                for ( int k = left ; k<=right;k++)
                    myNew[k]=(myVal [k-1] + myVal[k+1])/2.0;
                
                    ph.arriveAndAwaitAdvance();
                
               double[] temp = myNew;
            myNew=myVal;
            myVal=temp;
                
            }
            
            });
            
            threads[j].start();
        }
        
        for( int j =0; j < tasks ; j++)
        {
            try{
                threads[j].join();
            }
            catch( InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    
    public void runForallFuzzyBarrier( final int iterations , final int tasks){
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);
        
        Thread[] threads = new Thread[tasks];
        
        for (int j =0; j<tasks ; j++){
            int i = j;
            threads[j] = new Thread(()->{
            
            double[]myVal = this.myVal;
            double [] myNew = this.myNew;
            for(int itr =0 ; itr < iterations ;itr ++){
                
                int left = i *(n/tasks) +1;
                myNew[left] = (myVal [left-1] + myVal[left+1])/2.0;
                
                int right = (i+1)* (n/tasks);
                myNew[right] = (myVal [right-1] + myVal[right+1])/2.0;
                
                
                int currentPhase = ph.arrive();
                
                
                for ( int k = left+1 ; k<=right-1;k++)
                    myNew[k]=(myVal [k-1] + myVal[k+1])/2.0;
                
                ph.awaitAdvance(currentPhase);
                
                double[] temp = myNew;
                myNew=myVal;
                myVal=temp;
                
            }
            
            });
            
            threads[j].start();
        }
        
        for( int j =0; j < tasks ; j++)
        {
            try{
                threads[j].join();
            }
            catch( InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
    private static void printResults(String name, long timeInNanos){
        System.out.printf("  %s completed in %8.3f milliseconds \n",name,timeInNanos/ 1e6);
        
    }
    
    public static double[] createArray(final int N) {
        final double[] input = new double[N + 2];
        input[N + 1] = 1.0;
        return input;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        final int iterations = 10000;
        final int N = 100000;
        final int tasks = 4;
        double[] myNew = createArray(N);
        double[] myVal = createArray(N);
        
        OneDimAveragingPhaser phaser = new OneDimAveragingPhaser(myNew, myVal, N);
       
        long startTime = System.nanoTime();
        phaser.runSequential(iterations);
        long TimeInNanos = System.nanoTime() - startTime;
        printResults("runSequebtial", TimeInNanos);
        
         startTime = System.nanoTime();
        phaser.runForallBarrier(iterations, tasks);
         TimeInNanos = System.nanoTime() - startTime;
        printResults("runForallBarrier", TimeInNanos);
        
        startTime = System.nanoTime();
        phaser.runForallFuzzyBarrier(iterations,tasks);
         TimeInNanos = System.nanoTime() - startTime;
        printResults("runForallFuzzyBarrier", TimeInNanos);
        
    }
    
}
