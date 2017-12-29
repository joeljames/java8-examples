package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ConcurrentUtils {
    
    public static void stop(ExecutorService executor) {
        //An ExecutorService provides two methods to stop: 
        //1)shutdown() waits for currently running tasks to finish 
        //2)shutdownNow() interrupts all running tasks and shut the executor down immediately

        //shutdown softly by waiting a certain amount of time
        //after 5 seconds fore shut down all the treads
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        } 
    } 
    
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
