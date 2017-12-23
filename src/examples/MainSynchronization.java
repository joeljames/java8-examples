package examples;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

import utils.ConcurrentUtils;

public class MainSynchronization {

    static int count = 0;
    
    static void incerement() {
        count = count + 1;
    }
    
    public static void main(String[] args) {
        //in multi-threaded code we have to be careful when accessing shared mutable variables concurrently 
        //eg: increment integer which is accessible simultaneously from multiple threads
        
        System.out.println("Accessing shared resource with multiple threads simultaniously");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable runnable = () -> {
            count = count + 1;
        };
        IntStream.range(0, 10000)
            .forEach(i -> executor.submit(runnable));
        ConcurrentUtils.stop(executor);
        //we won't see an constant result of 10000
        // Three steps has been performed 
        //1) read the value of count 
        //2) increment the value of count by 1
        //3) write the result to the count variable 
        //if step1 is performed in parallel(2 threads  reading the same value) 
        //then its possible that the result is lost in writes, so the actual result is lower  
        //Use the synchronized key word to fix the above issue 
        //synchronized void incerement() {
        //    count = count + 1;
        //}
        //IntStream.range(0, 10000)
        //.forEach(i -> executor.submit(this::increment));
        //synchronization statement is available as a block statement
        //void increment() {
        //    synchronized(this) {
        //        count = count + 1; 
        //    }
        //}
        System.out.println("Count: " + count);
        
        
        System.out.println("ReentrantLock (mutial exclusion lock)");
        ReentrantLock lock = new ReentrantLock();
        
        int count1 = 0;
        lock.lock();
        //It is important to wrap your code in try/finally to ensure unlocking in case of exception
        //if other thread has already acquired the lock 
        //then subsequent calls to lock() will pause the current thread until the lock been unlocked
        try {
            count++;
        }
        finally {
            lock.unlock();
        }

        System.out.println("Different methods which ReentrantLock locks support: ");
        ExecutorService executor3 = Executors.newFixedThreadPool(2);
        ReentrantLock lock3 = new ReentrantLock();
        
        executor3.submit(() -> {
            lock.lock();
            try {
                ConcurrentUtils.sleep(1);
            } 
            finally {
                lock.unlock();
            }
        });
        
        //the above task holds the lock for 1 second the below task will
        //obtain different information about the locks
        executor3.submit(() -> {
           System.out.println("Locked: " + lock.isLocked()); 
           System.out.println("Held by me: " + lock.isHeldByCurrentThread()); 
           System.out.println("Lock acquired: " + lock.tryLock()); 
        });
        ConcurrentUtils.stop(executor3);

        System.out.println("ReentrantReadWriteLock: ");
        //the idea behind read write locks are that its usually safe to read 
        // the variable as long as no one is writing to it
        //so the read locks can be held by multiple threads as long as no one is writing to it
        //This can improve the throughput in case reads are more frequent than writes
        
        ExecutorService executer4 = Executors.newFixedThreadPool(2);
        Map<String, String> map = new HashMap<>();
        ReadWriteLock lock4 = new ReentrantReadWriteLock();
        executer4.submit(() -> {
            lock4.writeLock();
            try {
                ConcurrentUtils.sleep(1);
                map.put("foo", "bar");
            }
            finally {
                lock4.writeLock().unlock();
            }
        });
        
        //before the above task has finished two other tasks are being submitted
        // to read the entry from the map 
        
        Runnable readTask = () -> {
           lock4.readLock();
           try {
               System.out.println(map.get("foo"));
               ConcurrentUtils.sleep(1);
           } 
           finally {
               lock4.readLock().unlock();
           }
        };
        //the read task has to wait for the write lock to be released
        //after the write lock has been released both the read tasks are executed in parallel
        //read lock can safely be acquired concurrently as long as there is no write locks
        executer4.submit(readTask);
        executer4.submit(readTask);
        ConcurrentUtils.stop(executer4);

        System.out.println("StampedLock: ");
        //similar to read write lock, but returns a stamped long value
        //can use this time stamp to release or check if the lock is still valid
        //also supports optimistic locking
        //stamped locks does not implement reentrant characteristic, be careful not to run into deadlocks
        
        ExecutorService executor5 = Executors.newFixedThreadPool(2);
        Map<String, String> map5 = new HashMap<>();
        StampedLock lock5 = new StampedLock();
        
        executor5.submit(() -> {
           long stamp = lock5.writeLock();
           try {
               ConcurrentUtils.sleep(1);
               map5.put("foo", "bar");
           }
           finally {
               lock5.unlockWrite(stamp);
           }
        });
        
        Runnable readTask5 = () -> {
           long stamp = lock5.readLock();
           try {
               System.out.println(map5.get("foo"));
               ConcurrentUtils.sleep(1);
           }
           finally {
               //stamp created above is used for unlocking
               lock5.unlockRead(stamp);
           }
        };
        
        executor5.submit(readTask5);
        executor5.submit(readTask5);
        ConcurrentUtils.stop(executor5);

        System.out.println("StampedLock with optimistic locking: ");
        //In contrast to normal read locks an optimistic read locks dosen't 
        //prevent other threads to obtain a write lock.
        // After sending the first thread to sleep for one second the second thread 
        //obtains a write lock without waiting for the optimistic read lock to be released. 
        //From this point the optimistic read lock is no longer valid. 
        //Even when the write lock is released the optimistic read locks stays invalid
        ExecutorService executor6 = Executors.newFixedThreadPool(2);
        StampedLock lock6 = new StampedLock();
        
        executor6.submit(() -> {
            long stamp = lock6.tryOptimisticRead();
            try {
                //check if a stamp is valid
                System.out.println("Optimistic lock valid: " + lock6.validate(stamp));
                ConcurrentUtils.sleep(1);
                System.out.println("Optimistic lock valid: " + lock6.validate(stamp));
                ConcurrentUtils.sleep(2);
                System.out.println("Optimistic lock valid: " + lock6.validate(stamp));
            }
            finally {
                lock6.unlock(stamp);
            }
        });
        
        executor6.submit(() -> {
            long stamp = lock6.writeLock();
            try {
                System.out.println("Write lock acquired");
                ConcurrentUtils.sleep(1);
            }
            finally {
                lock6.unlock(stamp);
                System.out.println("Write done");
            }
        });
        ConcurrentUtils.stop(executor6);

        System.out.println("StampedLock convert read lock to write lock (tryConvertToWriteLock): ");
        //because of the above issue sometimes it is useful to convert read lock into 
        //write lock without unlocking and locking again. Use tryConvertToWriteLock() for that purpose
        ExecutorService executor7 = Executors.newFixedThreadPool(2);
        StampedLock lock7 = new StampedLock();

        executor7.submit(() -> {
           int count7 = 0;
           long stamp = lock7.readLock();
           try {
               if (count7 == 0) {
                   //have to convert the read lock into a write lock to not break potential concurrent access by other threads
                   //tryConvertToWriteLock doesn't block but may return a zero stamp 
                   //indicating that no write lock is currently available
                   //In that case we call writeLock() to block the current thread until a write lock is available
                   stamp = lock7.tryConvertToWriteLock(stamp);
                   if (stamp == 0L) {
                       System.out.println("Could not convert to write lock");
                       stamp = lock7.writeLock();
                   }    
                   count7 = 23;
               }
               System.out.println(count7);
           }
           finally {
               lock7.unlock(stamp);
           }
        });
        ConcurrentUtils.stop(executor7);

        System.out.println("Semaphores: ");
        //locks grant access to resources or variables; 
        //whereas semaphores are capable of maintaining whole sets of permits
        //this is useful where you have to limit the amount of concurrent access
        //to certain part of the application.
        ExecutorService executor8 = Executors.newFixedThreadPool(10);
        Semaphore semaphore = new Semaphore(5);
        //executor can run 10 concurrent tasks
        //but semaphores is size 5. limiting the concurrency to 5 
        
        
        Runnable longRunningTask = () -> {
          boolean permit = false;  
          try {
              permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
              
              if (permit) {
                  System.out.println("Semaphore acquired");
                  ConcurrentUtils.sleep(5);
              } else {
                  System.out.println("Could not acquire semaphone"); 
              }
          } catch (InterruptedException e) {
              throw new IllegalStateException(e);
          } finally {
              if (permit) {
                  semaphore.release();
              }
          }
        };

        IntStream.range(0, 10)
            .forEach(i -> executor.submit(longRunningTask));
        ConcurrentUtils.stop(executor8);
    }

}
