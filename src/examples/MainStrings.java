package examples;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainStrings {

    public static void main(String[] args) {
        // New method on string join
        System.out.println("join example: ");
        String out = String.join(":", "foo", "bar");
        System.out.println(out);

        System.out.println("chars example: ");
        // char creates a stream for all characters of the string
        String out1 = "foo:bar".chars()
            .mapToObj(c -> String.valueOf(c))
            .collect(Collectors.joining());
        System.out.println(out1);
        
        System.out.println("regex example: ");
        // split strings for any pattern 
        String out2 =Pattern.compile(":")
            .splitAsStream("foo:bar")
            .filter(s -> s.contains("bar"))
            .sorted()
            .collect(Collectors.joining());
        System.out.println(out2);

        System.out.println("regex pattern converted into predicate example: ");
        Pattern pattern = Pattern.compile(".*gmail\\.com");
        long out3 = Stream.of("bob@gmail.com", "alice@hotmail.com")
            .filter(pattern.asPredicate())
            .count();
        System.out.println(out3);
    }

}
