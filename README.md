java8-examples
==============

Introduction to java8 features. This module provides short and simple examples of lambda expressions, method references,
functional interface, new collections API, new maps API, and streams API.

Table of Contents
-----------------

  * [Lambda Expressions](src/examples/SimpleLambda.java)
    * Creating a Comparator
    * Creating a Runnable
  * [Method References](src/examples/MethodReferencesLambda.java)
    * Function
    * BinaryOperator
    * Consumer
    * Supplier
  * [New Collections API](src/examples/NewAPICollections.java)
    * forEach
    * removeIf
    * replaceAll
    * sort
  * [New Maps API](src/examples/NewAPIMap.java)
    * getOrDefault
    * putIfAbsent
    * computeIfAbsent
    * merge
  * [Stream Building API](src/examples/MainStreamBuilding.java)
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
  * [Functional Interface](src/examples/MainPredicate.java)
    * Predicate Example

