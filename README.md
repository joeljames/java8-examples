java8-examples
==============

Introduction to java8 features. This module provides short and simple examples of lambda expressions, method references,
functional interface, new collections API, new maps API, and streams API.

Developer Setup
-----------------

We use sdkman to manage java versions. Install sdkman by following the instructions [here](https://sdkman.io/install).
You could also enable automatically switching to the correct version of java when you cd into this directory.
This can be configured by updating the config in `~/.sdkman/etc/config` to `sdkman_auto_env=true`
Run the command below to install the configured version of java:

    sdk env install


Table of Contents
-----------------

  * [Lambda Expressions](src/java/examples/SimpleLambda.java)
    * Creating a Comparator
    * Creating a Runnable
  * [Method References](src/java/examples/MethodReferencesLambda.java)
    * Function
    * BinaryOperator
    * Consumer
    * Supplier
    * Optional
  * [New Collections API](src/java/examples/NewAPICollections.java)
    * forEach
    * removeIf
    * replaceAll
    * sort
  * [New Maps API](src/java/examples/NewAPIMap.java)
    * getOrDefault
    * putIfAbsent
    * computeIfAbsent
    * sortMap
    * merge
  * [Stream Building API](src/java/examples/MainStreamBuilding.java)
    * Collections to Stream
    * Stream.generate
    * Stream.iterate
    * filter and map
    * findFirst
    * forEach
    * IntStream
        * average, max
        * mapToInt
        * range
    * anyMatch
    * Stream why order matters
    * Reusing stream object
    * Collectors
        * toList
        * groupingBy
        * averagingDouble
        * summarizingDouble
        * joining
        * toMap
        * Create custom collector (Collector.of)
    * flatMap
    * reduce
        * excepts BinaryOperator(accumulator)
        * excepts identity and BinaryOperator(accumulator)
        * excepts identity, BiFunction and accumulator
    * parallelStream
  * [Avoiding Null check](src/java/examples/MainNullCheck.java)
  * [String](src/java/examples/MainStrings.java)
    * join
    * chars
    * regex
    * regex as predicate
  * [File IO](src/java/examples/MainFileIO.java)
      * listing files in a directory
      * listing files of directory and sub directory
      * Files.readAllLines to read files
      * Files.lines to read files as stream
      * Files.newBufferedReader for buffered reading
      * Files.newBufferedWriter for buffered writing
  * [Concurrency and Executors](src/java/examples/MainConcurrencyThreadsAndExecutors.java)
      * CompletableFuture example
      * creating runnable tasks
      * using executors
      * safe stopping of an executor
      * using callables
      * executors InvokeAll
      * executors InvokeAny
      * ScheduleExecutors for running tasks periodically
      * scheduleAtFixedRate
      * scheduleWithFixedDelay
  * [Synchronization](src/java/examples/MainSynchronization.java)
      * Problems accessing shared resources with multiple threads simultaneously
      * ReentrantLock
      * Different methods which ReentrantLock locks support
      * ReentrantReadWriteLock
      * StampedLock
      * StampedLock with optimistic locking
      * StampedLock convert read lock to write lock (tryConvertToWriteLock)
      * Semaphores
  * [Functional Interface Predicate Example](src/java/examples/MainPredicate.java)
  * [Functional Interface Comparator Example](src/java/examples/MainComparator.java)
  * [Bytes to String](src/java/examples/BytesToString.java)
  * [By Reference](src/java/examples/ByReference.java)

Thanks for reading!
