
package pointtopointsyncphasers;

import java.util.concurrent.Phaser;

/**
 *
 * @author Mahmoud Mustafa
 */
public class PointToPointSyncPhasers {

    
    public static void dowork(int n){
        int [] count= new int[n*100000];
        for (int i = 0; i < 100000*n; i++) {
            count[i] =i;
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        for( int i = 0; i < 2; i++){
            System.out.printf("Run %d \n ",i);
            final Phaser ph0 = new Phaser(1);
            final Phaser ph1 = new Phaser(1);
            final Phaser ph2 = new Phaser(1);
            
            Thread t0 = new Thread(() -> {
                // Phase 0
                dowork(100);
                ph0.arrive();
                ph1.awaitAdvance(0);
                // Phase 1
                dowork(300);
            });
            
            
            Thread t1 = new Thread(() -> {
                // Phase 0
                dowork(200); // A(2)
                ph1.arrive();
                ph0.awaitAdvance(0);
                ph2.awaitAdvance(0);

                // Phase 1
                dowork(200); //B(2)
            });
            
            Thread t2 = new Thread(() -> {
                // Phase 0
                dowork(300); // A(3)
                ph2.arrive();
                ph1.awaitAdvance(0);

                // Phase 1
                dowork(100); //B(3)
            });
        
            
            // run Sequential version
            
            long startTime = System.nanoTime();
            
            dowork(100); // t0 - phase 0
            dowork(300); // t0 - phase 1
            
            dowork(200); // t1 - phase 0
            dowork(200); // t1 - phase 1
            
            dowork(300); // t2 - phase 0
            dowork(100); // t2 - phase 1
            
            long timeInNanos = System.nanoTime() - startTime;
            System.out.printf(" Sequential version completed in %8.3f seconds \n", timeInNanos/1e9);
            
            // Run PARALLEL Version
            
            startTime = System.nanoTime();
            
            t0.start();
            t1.start();
            t2.start();
            
            try{
                t0.join();
                t1.join();
                t2.join();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            
            timeInNanos = System.nanoTime() - startTime;
            System.out.printf(" Parallel version completed in %8.3f seconds \n", timeInNanos/1e9);
            
        }

        
        
    }
    
}
