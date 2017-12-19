package examples;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainConcurrencyThreadsAndExecutors {

    static Callable<String> callable(String result, long sleepSconds) {
        return () -> {
          TimeUnit.SECONDS.sleep(sleepSconds);
          return result;
        };
    }
    
    public static void main(String[] args) {
        System.out.println("Creating runnable task: ");
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };
        //run the task in the main thread
        task.run();
        
        // run the task by spanning up a new thread 
        Thread thread = new Thread(task);
        thread.start();
        System.out.println("Done!");

        System.out.println("Threads can be put to sleep: ");
        // Simulate long running tasks
        
        Runnable task2 = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Foo " + threadName);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Alternatively you can achieve the same by calling Thread.sleep(1000)
            System.out.println("Bar " + threadName);

        };
        Thread thread2 = new Thread(task2);
        thread2.start();
        
        System.out.println("Threads example using executor: ");
        //ExecutorService is a higher level replacement for working with threads directly
        //Executors are capable of running asynchronous tasks and typically manage a pool of threads.
        //so we don't have to create new threads manually
        //Let create a executor with a thread pool of size one
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);            
        });

        System.out.println("Example on how to stop an executor: ");
        //An ExecutorService provides two methods to stop: 
        //1)shutdown() waits for currently running tasks to finish 
        //2)shutdownNow() interrupts all running tasks and shut the executor down immediately

        //shot down softly by waiting a certain amount of time
        //after 5 seconds fore shut down all the treads
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } 
        catch (InterruptedException e) {
            System.out.println("task interrupted");
            
        }
        finally {
            if (!executor.isTerminated()) {
                System.out.println("force terminnating non finished tasks.");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
        
        System.out.println("Example callables: ");
        //Similar to Runnable's but instead of being void they return a value
        //Callables can be submitted to executor services just like runnables
        // submit() doesn't wait until the task completes, 
        // the executor service cannot return the result of the callable directly
        // executor returns a special result of type Future which can be used to retrieve the actual result 
        // at a later point in time
        Callable<Integer> task3 = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted exception", e);
                
            }
        };
        //newFixedThreadPool(1) is equivalent to newSingleThreadExecutor()
        ExecutorService executor1 = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor1.submit(task3);
        System.out.println("future done? " + future.isDone());
        Integer result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        System.out.println("future done? " + future.isDone());
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.print("result: " + result);
        
        System.out.println("Example timeouts: ");
        ExecutorService executor2 = Executors.newFixedThreadPool(1);
        Future<Integer> feature = executor2.submit(task3);
        try {
            feature.get(1, TimeUnit.SECONDS);
            //If the result cannot be returned in 1 second then TimeoutException will be raised
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("Example InvokeAll: ");
        //Batch submitting of multiple callables
        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");
        ExecutorService executor3 = Executors.newWorkStealingPool();
        try {
            executor3.invokeAll(callables)
                .stream()
                .map(f -> {
                    try {
                        return f.get();
                    } 
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Example InvokeAny: ");
        //returns the result of the fastest callable
        List<Callable<String>> callables1 = Arrays.asList(
                callable("task1", 2),
                callable("task2", 1),
                callable("task3", 3));
        ExecutorService executor4 = Executors.newWorkStealingPool();
        String result1 = null;
        try {
            result1 = executor4.invokeAny(callables1);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(result1);
        
        System.out.println("Scheduled Executors for running tasks periodically: ");
        ScheduledExecutorService executor5 = Executors.newScheduledThreadPool(1);
        
        Runnable task5 = () -> System.out.println("Scheduling: " + System.nanoTime());
        ScheduledFuture<?> future5 = executor5.schedule(task5, 3, TimeUnit.SECONDS);
        
        try {
            TimeUnit.MICROSECONDS.sleep(1337);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long remaningDealy = future5.getDelay(TimeUnit.MICROSECONDS);
        System.out.println("Get Dealy: " + remaningDealy);
        
        System.out.println("Execute task periodically (scheduleAtFixedRate): ");
        //capable of executing tasks with a fixed time rate
        int initialDelay = 0; //wait before the task will be executed
        int period = 1; 
        //triggers this task every sec
        //the drawback of this method is that it doesn't take into account the actual duration of the task
        //So if you specify a period of one second but the task needs 2 seconds to be executed 
        //then the thread pool will working to capacity very soon
        executor5.scheduleAtFixedRate(task5, initialDelay, period, TimeUnit.SECONDS);

        System.out.println("Execute task periodically (scheduleWithFixedDelay): ");
        Runnable task6 = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            }
            catch (InterruptedException e) {
                System.out.println("Task interrupted");
            }
        };
        //This method works just like the counterpart described above. 
        //The difference is that the wait time period applies between the end of a task 
        //and the start of the next task
        executor5.scheduleWithFixedDelay(task6, 1, 1, TimeUnit.SECONDS);

    }
}
