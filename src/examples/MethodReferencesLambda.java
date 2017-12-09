package examples;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferencesLambda {

    public static void main(String[] args) {
        // Method references is an another way of writing lambda expressions
        
        // ----------------Example 1--------------  
        // Simple Lambda
        Function<Person, Integer> f1 = person -> person.getAge();
        // Method reference lambda (More readable) getAge need not be static method
        Function<Person, Integer> f2 = Person::getAge;

        // ----------------Example 2--------------  
        // Simple Lambda
        BinaryOperator<Integer> s1 = (i1, i2) -> Integer.sum(i1, i2);
        // Method reference lambda
        BinaryOperator<Integer> s2 = Integer::sum;
        
        // ----------------Example 3--------------  
        // Simple Lambda
        Consumer<String> printer1 = s -> System.out.println(s);
        // Method reference lambda
        Consumer<String> printer2 = System.out::println;

        // ----------------Example 4--------------  
        // Simple Lambda
        Supplier<Person> personSupplier1 = () -> new Person();
        // Method reference lambda
        Supplier<Person> personSupplier2 = Person::new;

    }

}
