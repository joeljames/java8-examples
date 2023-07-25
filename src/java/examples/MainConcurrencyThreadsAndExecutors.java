package examples;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import examples.utils.ConcurrentUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Slf4j
public class MainConcurrencyThreadsAndExecutors {

    static Callable<String> callable(String result, long sleepSconds) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSconds);
            return result;
        };
    }

    static int squareWithHeavyComputation(int num) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return num * num;
    }

    static String toString(int num) {
        return String.valueOf(num);
    }

    public static void main(String[] args) {
        //================================================================
        System.out.println("Example CompletableFuture: ");
        //================================================================
        //CompletableFuture is very similar to JS Promise async calls followed by `.then`
        //Note: each num in the map will be executed in parallel (separate threads)
        List<CompletableFuture<String>> results = Stream.of(1, 5, 20).map(num -> {
                    //No need to manage thread pool eg: Executors.newFixedThreadPool(2). Internally it use a ForkJoin pool
                    return CompletableFuture.supplyAsync(() -> squareWithHeavyComputation(num))
                            .thenApply(squareNum -> toString(squareNum)) // thenApply is similar to .map in stream. Performs transformation
                            .thenApply(stringNum -> String.format("Prefix %s", stringNum))
                            //similar to a catch block in-case any failure in the above steps
                            .exceptionally(e -> {
                                e.printStackTrace();
                                return "";
                            })
                            .thenApply(prefixedNum -> String.format("%s Suffix", prefixedNum));
//                            .thenAccept(data -> commitToDb(data)) // thenAccept is similar to .forEach in stream
//                            .thenRun(() -> System.out.println("Go grab a drink, everything worked"));

                }
        ).toList();

        // Get data from the future collection above.
        List<String> joinedResults = results
                .stream()
                .map(CompletableFuture::join)
                .toList();

        System.out.println("CompletableFuture Result is: " + joinedResults);


        System.out.println("Creating runnable task: ");
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };
        //run the task in the main thread
        task.run();

        //run the task by spanning up a new thread
        Thread thread = new Thread(task);
        thread.start();
        System.out.println("Done!");

        //================================================================
        System.out.println("Threads can be put to sleep: ");
        //================================================================
        //Simulate long running tasks
        Runnable task2 = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Foo " + threadName);
            try {
                // Alternatively you can achieve the same by calling Thread.sleep(1000)
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Bar " + threadName);
        };
        Thread thread2 = new Thread(task2);
        thread2.start();

        //================================================================
        System.out.println("Custom executorService with large thread pool: ");
        //================================================================
        int maxQueryThreads = 1024;
        int workQueueSize = 5120;
        //Use large thread pool if you have blocking I/O. Small thread pools will get exhausted easily and you won't achieve parallelism.
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                maxQueryThreads,
                maxQueryThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue(workQueueSize)
        );

        //================================================================
        System.out.println("Threads example using executor: ");
        //================================================================
        //ExecutorService is a higher level replacement for working with threads directly
        //Executors are capable of running asynchronous tasks and typically manage a pool of threads.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        });

        System.out.println("Example on how to stop an executor: ");
        //The stop method is defined in ConcurrentUtils.stop module
        ConcurrentUtils.stop(executor);

        //================================================================
        System.out.println("Example callables: ");
        //================================================================
        //Similar to Runnable's but instead of being void they return a value
        //Callables can be submitted to executor services just like runnables
        //submit() doesn't wait until the task completes,
        //the executor service cannot return the result of the callable directly
        //executor returns a special result of type Future which can be used to retrieve the actual result
        // at a later point in time
        Callable<Integer> task3 = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted exception", e);

            }
        };
        //newFixedThreadPool(1) is equivalent to newSingleThreadExecutor()
        //Give meaningful name to executor thread
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("meaningful-name-%d")
                .setPriority(Thread.NORM_PRIORITY + 1)
                .setDaemon(true)
                .build();

        ExecutorService executor1 = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(), //Get thread count base on number of processors
                threadFactory);
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
        ConcurrentUtils.stop(executor1);

        System.out.println("Example timeouts: ");
        ExecutorService executor2 = Executors.newFixedThreadPool(1);
        Future<Integer> feature = executor2.submit(task3);
        try {
            feature.get(1, TimeUnit.SECONDS);
            //If the result cannot be returned in 1 second then TimeoutException will be raised
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        ConcurrentUtils.stop(executor2);

        //================================================================
        System.out.println("Example InvokeAll: ");
        //================================================================
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
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConcurrentUtils.stop(executor3);

        //================================================================
        System.out.println("Example InvokeAny: ");
        //================================================================
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
        ConcurrentUtils.stop(executor4);

        //================================================================
        System.out.println("Scheduled Executors for running tasks periodically: ");
        //================================================================
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

        //================================================================
        System.out.println("Execute task periodically (scheduleAtFixedRate): ");
        //================================================================
        //capable of executing tasks with a fixed time rate
        int initialDelay = 0; //wait before the task will be executed
        int period = 1; //triggers this task every second
        //the drawback of this method is that it doesn't take into account the actual duration of the task
        //So if you specify a period of one second but the task needs 2 seconds to be executed
        //then the thread pool will be working to capacity very soon
        executor5.scheduleAtFixedRate(task5, initialDelay, period, TimeUnit.SECONDS);
        ConcurrentUtils.stop(executor5);

        //================================================================
        System.out.println("Execute task periodically (scheduleWithFixedDelay): ");
        //================================================================
        ScheduledExecutorService executor6 = Executors.newScheduledThreadPool(1);
        Runnable task6 = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            } catch (InterruptedException e) {
                System.out.println("Task interrupted");
            }
        };
        //This method works just like the counterpart described above.
        //The difference is that the wait time period applies between the end of a task
        //and the start of the next task
        executor6.scheduleWithFixedDelay(task6, 1, 1, TimeUnit.SECONDS);
        ConcurrentUtils.stop(executor6);
    }
}
