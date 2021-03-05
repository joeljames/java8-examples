package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainStreamBuilding {

    public static void main(String[] args) {
        // Stream operation are either intermediate(returns stream) or terminal(returns void or non-stream results).
        // Intermediate operation returns a stream so we can chain multiple intermediate operations.
        // If there is no terminal operation at the end then the intermediate operation wont be executed
        // You can convert a collection into a stream by calling the .stream() method on the collection
        // See doc for full list of available operation https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
        System.out.println("Stream Example1: ");
        List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4);
        Stream<Integer> stream = ints.stream();
        stream.forEach(System.out::println);

        System.out.println("Stream generate method: ");
        Stream<String> streamOfStrings = Stream.generate(() -> "one");
        streamOfStrings.limit(5).forEach(System.out::println);
       
        System.out.println("Stream iterate method: ");
        Stream<String> streamOfStrings2 = Stream.iterate("+", s -> s + "+");
        streamOfStrings2.limit(5).forEach(System.out::println);

        System.out.println("Stream ThreadLocalRandom.current().ints method: ");
        IntStream streamOfInts = ThreadLocalRandom.current().ints();
        streamOfInts.limit(5).forEach(System.out::println);

        System.out.println("Stream with filter map : ");
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c1", "c2");
        myList.stream()
            .filter(s -> s.startsWith("c"))
            .map(String::toUpperCase)
            .sorted()
            .forEach(System.out::println);
        
        System.out.println("Stream with findFirst : ");
        myList.stream()
            .findFirst()
            .ifPresent(System.out::println);
        
        System.out.println("Stream of: ");
        //we don't have to create collections to work with streams
        Stream<Integer> stream1 = Stream.of(0, 1, 2);
        stream1.forEach(System.out::println);
        
        System.out.println("Stream for working with primitive datatypes (int, long, double): ");
         IntStream.range(1, 4)
             .forEach(System.out::println);
         //Primitive stream supports additional terminal aggregate operations (sum average) 
         IntStream.of(1, 2, 3)
             .map(n -> (n*2) + 1)
             .average()
             .ifPresent(System.out::println);
         
         System.out.println("Stream mapToInt: ");
         //Transform a regular object stream to a primitive stream or vice versa
         Stream.of("a1", "a2", "a3")
             .map(s -> s.substring(1))
             .mapToInt(Integer::parseInt)
             .max()
             .ifPresent(System.out::println);
         
         System.out.println("Stream mapToObj: ");
         IntStream.range(1, 4)
             .mapToObj(i -> "a" + i)
             .forEach(System.out::println);
         
         
         System.out.println("Combined mapToInt and mapToObj: ");
         Stream.of(1.0, 2.0, 3.0)
             .mapToInt(Double::intValue)
             .mapToObj(i -> "a" + i)
             .forEach(System.out::println);
         
         System.out.println("Stream anyMatch (Reduce the number of iteration in map): ");
         // d2 first goes into map and then into anyMatch followed by a2 and so on
         Stream.of("d2", "a2", "b1", "b3")
             .map(s -> {
                 System.out.println("map: " + s);
                 return s.toUpperCase();
             })
             .anyMatch(s -> {
                 System.out.println("anyMatch: " + s);
                 return s.startsWith("A");
             });
         
         System.out.println("Stream why order matters: ");
         //The map and filter are called five times for every string in the collection 
         // whereas forEach is only called once
         Stream.of("d2", "a2", "b1", "b3", "c")
             .map(s -> {
                 System.out.println("map: " + s);
                 return s.toUpperCase();
             })
             .filter(s -> {
                 System.out.println("filter: " + s);
                 return s.startsWith("A");
             })
             .forEach(s -> System.out.println("forEach: " + s));

         System.out.println("Extending above example to reduce the execution by changing the order: ");
         //Always recommended to have filter before map to reduce the number of iteration
         Stream.of("d2", "a2", "b1", "b3", "c")
         .filter(s -> {
             System.out.println("filter: " + s);
             return s.startsWith("a");
         })
         .map(s -> {
             System.out.println("map: " + s);
             return s.toUpperCase();
         })
         .forEach(s -> System.out.println("forEach: " + s));

         System.out.println("Extending above example by adding sorted: ");
         // Sorting is a special kind of intermediate operation (stateful operation). 
         // In sorting you need to maintain the previous state of the element to get a ordered collection.
         // Hence, sort operation is performed on the entire collection first
         Stream.of("d2", "a2", "b1", "b3", "c")
             .sorted((s1, s2) -> {
                 System.out.printf("Sort: %s, %s\n", s1, s2);
                 return s1.compareTo(s2);
             })
             .filter(s -> {
                 System.out.println("filter: " + s);
                 return s.startsWith("a");
             })
             .map(s -> {
                 System.out.println("map: " + s);
                 return s.toUpperCase();
             })
             .forEach(s -> {
                 System.out.println("forEach: " + s);
             });
         
         System.out.println("Extending reusing stream: ");
         // In java8 streams cannot be reused. 
         // As soon as you call the terminal operation the stream is closed. 
         Stream<String> s3 = Stream.of("d2", "a2", "b1", "b3", "c")
                 .filter(s -> s.startsWith("a"));
         
         s3.anyMatch(s -> true); //ok
         //s3.noneMatch(s -> true); // exception

         System.out.println("Overcome the limitation by creting new stream chain: ");
         // Create a supplier to create a new stream with intermediate operations already setup.
         Supplier<Stream<String>> streamSupplier = 
                 () -> Stream.of("d2", "a2", "b1", "b3", "c") 
                         .filter(s -> s.startsWith("a"));
         
          streamSupplier.get().anyMatch(s -> true); //ok
          streamSupplier.get().noneMatch(s -> true); //ok
                 
          
          System.out.println("Collector example to transform elements of stream to list: ");
          List<Person> persons = Arrays.asList(
                  new Person("Max", 18),
                  new Person("Peter", 23),
                  new Person("Pamela", 23),
                  new Person("David", 12)
          );
          
          List<Person> filtered = persons
                  .stream()
                  .filter(p -> p.getName().startsWith("P"))
                  .collect(Collectors.toList());
          System.out.println(filtered);
          
          System.out.println("Example collectors groupingBy: ");
          Map<Integer, List<Person>> personByAge = persons
                  .stream()
                  .collect(Collectors.groupingBy(p -> p.getAge()));
          personByAge.forEach((age, p) ->  System.out.printf("age: %s, person: %s", age, p));
          
          System.out.println("Example collectors aggregations: ");
          Double averageAge = persons
                  .stream()
                  .collect(Collectors.averagingDouble(p -> p.getAge()));
          System.out.println(averageAge);

          System.out.println("Example collectors summarizingInt: ");
           DoubleSummaryStatistics ageSummary = persons
                  .stream()
                  .collect(Collectors.summarizingDouble(p -> p.getAge()));
          System.out.println(ageSummary);
          
          System.out.println("Example collectors joining: ");
          String phrase = persons
                  .stream()
                  .filter(p -> p.getAge() >= 18)
                  .map(p -> p.getName())
                  .collect(Collectors.joining(" and ", "In USA ", " are of legal age."));
          System.out.println(phrase);

          System.out.println("Example collectors toMap: ");
          // Mapped keys must be unique otherwise IllegalStateException will be thrown
          Map<Integer, String> map = persons
                  .stream()
                  .collect(Collectors.toMap(
                          p -> p.getAge(),
                          p -> p.getName(), 
                          (name1, name2) -> name1 + "; " + name2));
          System.out.println(map);
          
          System.out.println("Example create your own collectors: ");
          Collector<Person, StringJoiner, String> personNameCollector = 
                  Collector.of(
                          () -> new StringJoiner(" | "), // supplier ( initially constructs such a StringJoiner with the appropriate delimiter)
                          (j, p) -> j.add(p.getName().toUpperCase()),//accumulator (add each persons upper-cased name to the StringJoiner), 
                          (j1, j2) -> j1.merge(j2), // combiner (knows how to merge two StringJoiners into one), 
                          StringJoiner::toString //characteristics ( constructs the desired String from the StringJoiner)
                   );
          String names = persons
                  .stream()
                  .collect(personNameCollector);
          System.out.println(names);
          
          System.out.println("Example flatMap: ");
          // Each object is transformed to zero or multiple other objects
          List<Foo> foos = new ArrayList<>();
          //create foos
          IntStream
              .range(1, 4)
              .forEach(i -> foos.add(new Foo("Foo" + i)));
          //create bars
          foos.forEach(f ->
                  IntStream
                  .range(1, 4)
                  .forEach(i -> f.bars.add(new Bar("Bar"+ i + " <- " + f.name)))
          );
          //flatMap accepts a function which has to return a stream of objects
          foos.stream()
              .flatMap(f -> f.bars.stream())
              .forEach(b -> System.out.println(b.name));
          
          System.out.println("Example reduce (excepts BinaryOperator(accumulator)): ");
          // Get max age
          persons.stream()
              .reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2)
              .ifPresent(System.out::println);

          System.out.println("Example reduce (excepts identity and BinaryOperator(accumulator)): ");
          Person result = persons.stream()
              .reduce(new Person("", 0), (p1, p2) -> {
                  int age = p1.getAge() + p2.getAge();
                  p1.setAge(age);
                  p1.name += p2.name;
                  return p1;
              });
          System.out.printf("name=%s; age=%s", result.name, result.getAge());

          System.out.println("Example reduce (excepts identity, BiFunction and accumulator): ");
          Integer ageSum = persons.stream()
                  .reduce(0, (sum, p) -> sum += p.getAge(), (sum1, sum2) -> sum1 + sum2);
          System.out.println(ageSum);
          
          System.out.println("Example Parallel Stream: ");
          // Stream can be executed in parallel to improve runtime performance
          // threads are created depending on the number of cores available
          // The values can be increased or decreased by setting the JVM parameter 
          // -Djava.util.concurrent.ForkJoinPool.common.parallelism=5
          ForkJoinPool commonPool = ForkJoinPool.commonPool();
          System.out.println(commonPool.getParallelism());
          
          System.out.println("Understand Parallel behaviour: ");
          Arrays.asList("a1", "a2", "b1", "c2", "c1")
              .parallelStream()
              .filter(s -> {
                  System.out.printf("filter: %s [%s]\n", s, Thread.currentThread().getName());
                  return true;
              })
              .map(s -> {
                  System.out.printf("map: %s [%s]\n", s, Thread.currentThread().getName());
                  return s.toUpperCase();
              })
              .forEach(s -> {
                  System.out.printf("forEach: %s [%s]\n", s, Thread.currentThread().getName());
              });
          
          System.out.println("Parallel execution with sort: ");
          // It will seems that sort is executed sequentially on the main thread only.
          // Actually, in java 8 sort uses Arrays.parallelSort() under the hood.
          // this method decides on the length of the array if sorting will be performed sequentially or in parallel
          // If the length of the specified array is less than the minimum granularity, then it is sorted using the appropriate Arrays.sort method.
          Arrays.asList("a1", "a2", "b1", "c2", "c1")
          .parallelStream()
          .filter(s -> {
              System.out.printf("filter: %s [%s]\n", s, Thread.currentThread().getName());
              return true;
          })
          .map(s -> {
              System.out.printf("map: %s [%s]\n", s, Thread.currentThread().getName());
              return s.toUpperCase();
          })
          .sorted((s1, s2) -> {
              System.out.printf("sort: %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
              return s1.compareTo(s2);
          })
          .forEach(s -> {
              System.out.printf("forEach: %s [%s]\n", s, Thread.currentThread().getName());
          });

          System.out.println("Parallel execution with reduce: ");
          Integer r1 = persons.parallelStream()
              .reduce(0, 
                      (sum, p) -> {
                          System.out.printf("accumulator: sum=%s; person=%s [%s]\n", sum, p, Thread.currentThread().getName());
                          return sum += p.getAge();
                      }, 
                      (sum1, sum2) -> {
                          System.out.printf("combiner: sum=%s; person=%s [%s]\n", sum1, sum2, Thread.currentThread().getName());
                          return sum1 + sum2;
                      });
          System.out.println(r1);
    }
}
