package examples;

@FunctionalInterface
public interface Predicate<T> {

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
