package examples;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

public class MainReduction {

    public static void main(String[] args) {
        List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        BinaryOperator<Integer> op = (i1, i2) -> i1 + i2;
        
        int reduction = reduce(ints, 0, op);
        System.out.println("Reduction: " + reduction);
        
        //Simulating parallel execution
        System.out.println("Simulating parallel computation");
        List<Integer> ints1 = Arrays.asList(0, 1, 2, 3, 4);
        List<Integer> ints2 = Arrays.asList(5, 6, 7, 8, 9);

        int reduction1 = reduce(ints1, 0, op);
        int reduction2 = reduce(ints2, 0, op);
        int reductionParallel = reduce(Arrays.asList(reduction1, reduction2), 0, op);
        System.out.println("Reduction: " + reductionParallel);

    }

    private static int reduce(List<Integer> values, int valueIfEmpty, BinaryOperator<Integer> reduction) {
        int result = valueIfEmpty;

        for (int value : values) {
            result = reduction.apply(result, value);
        }

        return result;
    }

}
