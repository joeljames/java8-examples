package examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleLambda {

    public static void main(String[] args) {
        // ----------------Comparator example--------------  
        // Traditional way 
        Comparator<String> c1 = new Comparator<String>() {

            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
            
        };
        
        // Lambda way
        Comparator<String> c2 = (String s1, String s2) -> Integer.compare(s1.length(), s2.length());
        //call the comparator by c2.compare("strign1", "string2");
      
        // ----------------Collections sorting example--------------  
        List<String> names = Arrays.asList("John", "Smith", "Paul");
        // Traditional way
        Collections.sort(names, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
            
        });
        
        // Lambda way
        Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
        
        // ----------------Runnable example--------------  
        // Traditional way 
        Runnable r1 = new Runnable() {
            
            @Override
            public void run() {
                int i = 0; 
                while (i++ < 10) {
                    System.out.println("It works");
                }
            }
        };

        // Lambda way
        // If the lambda function has multiple lines use the {}
        // Use the return statement if the lambda function need to return
        // Following modifiers can be put on a lambda expression
        // 1) Final keyword 
        // 2) Annotations
        // It is not possible to specify the return type of a lambda expression
        // We can omit the parameters types in lambda (String s1, String s2) can be written as (s1, s2) 
        Runnable r2 = () -> {
            int i = 0;
            while (i++ < 10) {
                System.out.println("It works");
            }
        };
        //call the runnable by calling r2.run();
    }
}
