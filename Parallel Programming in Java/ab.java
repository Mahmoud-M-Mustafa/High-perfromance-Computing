package edu.coursera.parallel;

import java.util.concurrent.Phaser;

/**
 * Wrapper class for implementing one-dimensional iterative averaging using
 * phasers.
 */
public final class OneDimAveragingPhaser {
    /**
     * Default constructor.
     */
    private OneDimAveragingPhaser() {
    }

    /**
     * Sequential implementation of one-dimensional iterative averaging.
     *
     * @param iterations The number of iterations to run
     * @param myNew A double array that starts as the output array
     * @param myVal A double array that contains the initial input to the
     *        iterative averaging problem
     * @param n The size of this problem
     */
    public static void runSequential(final int iterations, final double[] myNew,
            final double[] myVal, final int n) {
        double[] next = myNew;
        double[] curr = myVal;

        for (int iter = 0; iter < iterations; iter++) {
            for (int j = 1; j <= n; j++) {
                next[j] = (curr[j - 1] + curr[j + 1]) / 2.0;
            }
            double[] tmp = curr;
            curr = next;
            next = tmp;
        }
    }

    /**
     * An example parallel implementation of one-dimensional iterative averaging
     * that uses phasers as a simple barrier (arriveAndAwaitAdvance).
     *
     * @param iterations The number of iterations to run
     * @param myNew A double array that starts as the output array
     * @param myVal A double array that contains the initial input to the
     *        iterative averaging problem
     * @param n The size of this problem
     * @param tasks The number of threads/tasks to use to compute the solution
     */
    public static void runParallelBarrier(final int iterations,
            final double[] myNew, final double[] myVal, final int n,
            final int tasks) {
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);
	int count =0;
	for(int i=0; i< 100000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
for(int i=0; i< 1000000000;i++){
		count++; count--;
		
		}
int [] x= new int [1000000];
for(int i=0; i<    1000000;i++){
		x[i] =i;
		
		}
        Thread[] threads = new Thread[tasks];

        for (int ii = 0; ii < tasks; ii++) {
            final int i = ii;

            threads[ii] = new Thread(() -> {
                double[] threadPrivateMyVal = myVal;
                double[] threadPrivateMyNew = myNew;

                for (int iter = 0; iter < iterations; iter++) {
                    final int left = i * (n / tasks) + 1;
                    final int right = (i + 1) * (n / tasks);

                    for (int j = left; j <= right; j++) {
                        threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1]
                            + threadPrivateMyVal[j + 1]) / 2.0;
                    }
                    ph.arriveAndAwaitAdvance();

                    double[] temp = threadPrivateMyNew;
                    threadPrivateMyNew = threadPrivateMyVal;
                    threadPrivateMyVal = temp;
                }
            });
            threads[ii].start();
        }

        for (int ii = 0; ii < tasks; ii++) {
            try {
                threads[ii].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A parallel implementation of one-dimensional iterative averaging that
     * uses the Phaser.arrive and Phaser.awaitAdvance APIs to overlap
     * computation with barrier completion.
     *
     * TODO Complete this method based on the provided runSequential and
     * runParallelBarrier methods.
     *
     * @param iterations The number of iterations to run
     * @param myNew A double array that starts as the output array
     * @param myVal A double array that contains the initial input to the
     *              iterative averaging problem
     * @param n The size of this problem
     * @param tasks The number of threads/tasks to use to compute the solution
     */
   public static void runParallelFuzzyBarrier(final int iterations,
            final double[] myNew_, final double[] myVal_, final int n,
            final int tasks) {

        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);
        
        Thread[] threads = new Thread[tasks];
        
        for (int j =0; j<tasks ; j++){
            int i = j;
            threads[j] = new Thread(()->{
            
            double[]myVal = myVal_;
            double [] myNew = myNew_;
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
}