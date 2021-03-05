package examples;

import java.util.function.Function;

@FunctionalInterface
public interface Comparator<T> {
    // A functional interface is a interface which satisfy the following requirements
    // 1) Only one abstract method 
    // 2) Default methods do not count 
    // 3) Static methods do not count 
    // 4) Methods from Object class do not count
    // Four Categories in Java 8 are:
    // 1) Consumers 
    // 2) Supplier
    // 3) Functions
    // 4) Predicates
    

    public int compare(T t1, T t2);

//    public static Comparator<Person> comparing(Function<Person, Comparable> f) {
//        return (p1, p2) -> f.apply(p1).compareTo(f.apply(p2));
//    }
    
    // Same as above
    // New in java 8
    public static <U> Comparator<U> comparing(Function<U, Comparable> f) {
        return (p1, p2) -> f.apply(p1).compareTo(f.apply(p2));
    }

    // New in java 8
    public default Comparator<T> thenComparing(Comparator<T> cmp){
        return (p1, p2) -> compare(p1, p2) == 0 ? cmp.compare(p1, p2) : compare(p1, p2); 
    };
    
    public default Comparator<T> thenComparing(Function<T, Comparable> f){
        return thenComparing(comparing(f)); 
    };
}
