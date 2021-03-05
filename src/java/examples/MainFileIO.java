package examples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainFileIO {

    public static void main(String[] args) {
        System.out.println("listing files : ");
        // the creation of the stream is wrapped into a try/with statement.
        // Streams implement AutoCloseable and in this case we really have to close the 
        // stream explicitly since it's backed by IO operations
        try (Stream<Path> stream = Files.list(Paths.get(""))) {
            String joined = stream
                    .map(String::valueOf)
                    .filter(path -> !path.startsWith("."))
                    .sorted()
                    .collect(Collectors.joining("; "));
            System.out.println("List: " + joined);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("find files in a directory or sub directory: ");
        Path start = Paths.get("");
        int maxDepth = 5;
        
        try(Stream<Path> stream = Files.find(start, maxDepth, (path, attr) -> 
                String.valueOf(path).endsWith(".js"))) {
            String joined = stream
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining("; "));
            System.out.println("Found: " + joined);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        System.out.println("find files in a directory or sub directory using walk: ");
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            String joined = stream
                    .map(String::valueOf)
                    .filter(s -> s.startsWith(".js"))
                    .sorted()
                    .collect(Collectors.joining("; "));
            System.out.println("walk: " + joined);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("reading files modifying the content and writing to an another files using readAllLines: ");
        try {
            //this method is not memory-efficient because the whole file will be read into memory
            List<String> lines = Files.readAllLines(Paths.get("res/example.js"));
            lines.add("print('foobar');");
            Files.write(Paths.get("res/example-out.js"), lines);
            
        } catch (IOException e) {
            System.out.println("Failed to read file res/example.js");
            e.printStackTrace();
        }
 
        System.out.println("reading line by line of a file using lines: ");
        //Instead of reading all lines into memory at once, 
        //this method reads and streams each line one by one via streams
        try (Stream<String> stream = Files.lines(Paths.get("res/example.js"))) {
            stream
                .filter(line -> line.contains("print"))
                .map(String::trim)
                .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("reading line using buffered reader: ");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("res/example.js"))) {
            System.out.println(reader.readLine());
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("buffered reader has access to functional streams: ");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("res/example.js"))) {
            long countPrints = reader
                .lines()
                .filter(line -> line.contains("print"))
                .count();
            System.out.println("countPrints: " + countPrints);
        } catch (IOException e1) {
            e1.printStackTrace();
        } 

        System.out.println("writing line using buffered writer: ");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("res/example-out.js"))) {
            writer.write("print('Hi from writer');");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
