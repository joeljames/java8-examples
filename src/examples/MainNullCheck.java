package examples;

import java.util.Optional;
import java.util.function.Supplier;

public class MainNullCheck {

    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        }
        catch (NullPointerException e) {
            return Optional.empty();
        }
    }
    
    public static void main(String[] args) {
        Outer outer = new Outer();
        Outer obj = new Outer();
        Nested nested = new Nested();
        Inner inner = new Inner();
        nested.setInner(inner);
        outer.setNested(nested);
        
        //traditional way
        System.out.println("In the traditional check");
        if (outer != null && outer.getNested() != null && outer.getNested().getInner() != null) {
            System.out.println(outer.getNested().getInner().getFoo());
        } 

        //Using java8 Optional type
        // map accepts a lambda expression of type Function and wraps each function result into an Optional
        // Null checks are automatically handled under the hood
        System.out.println("In the Optional");
        Optional.of(outer)
            .map(Outer::getNested)
            .map(Nested::getInner)
            .map(Inner::getFoo)
            .ifPresent(System.out::println);
        
        //Alternative way using Supplier function
        System.out.println("Using Supplier function");
        resolve(() -> outer.getNested().getInner().getFoo())
            .ifPresent(System.out::println);
        resolve(() -> obj.getNested().getInner().getFoo())
            .ifPresent(System.out::println);
    }

}
