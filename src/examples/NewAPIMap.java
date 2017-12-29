package examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAPIMap {

    public static void main(String[] args) {

        Person p1 = new Person("Alice", 23);
        Person p2 = new Person("Brian", 56);
        Person p3 = new Person("Chelsea", 46);
        Person p4 = new Person("David", 28);
        Person p5 = new Person("Erica", 37);

        City newYork = new City("New York");
        City shanghai = new City("Shanghai");
        City paris = new City("Paris");
        
        Map<City, List<Person>> map = new HashMap<>();

        // -----------Example getOrDefault------------
        System.out.println("Example getOrDefault: ");
        System.out.println("People from Paris: " + map.getOrDefault(paris, Collections.emptyList()));
    
        // -----------Example putIfAbsent------------
        System.out.println("Example putIfAbsent: ");
        map.putIfAbsent(paris, new ArrayList<>());
        map.get(paris).add(p1);
        
        // -----------Example computeIfAbsent------------
        System.out.println("Example computeIfAbsent: ");
        map.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p2);
        //Since the key is already added in the above line the below lambda expression won't be executed
        map.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p3);
        System.out.println("People from newYork: " + map.get(newYork));

        // -----------Example merge------------
        System.out.println("Example merge: ");
        
        Map<City, List<Person>> map1 = new HashMap<>();
        map1.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p1);
        map1.computeIfAbsent(shanghai, city -> new ArrayList<>()).add(p2);
        map1.computeIfAbsent(shanghai, city -> new ArrayList<>()).add(p3);
        
        System.out.println("Map 1");
        map1.forEach((key, value) -> System.out.println(key + ": " + value));

        Map<City, List<Person>> map2 = new HashMap<>();
        map2.computeIfAbsent(shanghai, city -> new ArrayList<>()).add(p4);
        map2.computeIfAbsent(paris, city -> new ArrayList<>()).add(p4);
        map2.computeIfAbsent(paris, city -> new ArrayList<>()).add(p5);
        
        System.out.println("Map 2");
        map1.forEach((key, value) -> System.out.println(key + ": " + value));
        
        map2.forEach((city, people) -> {
            map1.merge(city, people, (peopleFromMap1, peopleFromMap2) -> {
                peopleFromMap1.addAll(peopleFromMap2);
                return peopleFromMap1;
            });
        });
        System.out.println("Merged result");
        map1.forEach((key, value) -> System.out.println(key + ": " + value));   
    }

}
