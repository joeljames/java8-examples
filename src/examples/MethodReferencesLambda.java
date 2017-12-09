package examples;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferencesLambda {

    public static void main(String[] args) {
        // Method references is an another way of writing lambda expressions
        
        // ----------------Example 1 Function--------------  
        // Simple Lambda Function 
        // Functions accept one argument and produce a result.
        Function<Person, Integer> f1 = person -> person.getAge();
        // Method reference lambda (More readable) getAge need not be static method
        Function<Person, Integer> f2 = Person::getAge;
        Person p = new Person("Jack", 28);
        System.out.println(f1.apply(p));

        // ----------------Example 2 BinaryOperator--------------  
        // Simple Lambda BinaryOperator
        // Accept two argument and produce a result.
        BinaryOperator<Integer> s1 = (i1, i2) -> Integer.sum(i1, i2);
        // Method reference lambda
        BinaryOperator<Integer> s2 = Integer::sum;
        
        // ----------------Example 3 Consumer--------------  
        // Simple Lambda Consumer
        // Consumers represent operations to be performed on a single input argument.
        Consumer<String> printer1 = s -> System.out.println(s);
        // Method reference lambda
        Consumer<String> printer2 = System.out::println;

        // ----------------Example 4--------------  
        // Simple Lambda Supplier
        //Suppliers produce a result of a given generic type
        Supplier<Person> personSupplier1 = () -> new Person();
        // Method reference lambda
        Supplier<Person> personSupplier2 = Person::new;
        
        // ----------------Example 5 Optional--------------  
        // Optionals are not functional interfaces, but nifty utilities to prevent NullPointerException
        Optional<String> o = Optional.of("bam");
        System.out.println(o.isPresent()); //true
        System.out.println(o.get()); //"bam"
        System.out.println(o.orElse("fallback")); //"bam"

        


    }

}
