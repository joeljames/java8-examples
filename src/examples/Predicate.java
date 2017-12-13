package examples;

@FunctionalInterface
public interface Predicate<T> {
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
    
    public boolean test(T t);

    public default Predicate<T> and(Predicate<T> other) {
        return t -> test(t) && other.test(t);
    }

    public default Predicate<T> or(Predicate<T> other) {
        return t -> test(t) || other.test(t);
    }

//    public static Predicate<String> isEqualsTo(String string) {
//        return s -> s.equals(string);
//    }
//    
    //parameterize the above static method 
    public static <U> Predicate<U> isEqualsTo(U u) {
        return s -> s.equals(u);
    }
}
