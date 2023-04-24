package interview;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

public class Streams {


    // ------------------------------ map vs flatMap ------------------------------
    public static void main(String args[]) {

        Stream.of(1,2,3)
                .map(i->i*10) // map returns exact type we need
                .forEach(System.out::println);

        Stream.of(1,2,3)
                .flatMap(i->Stream.of(i*100)) // flatMap returns Stream (or Optional) of our type  https://www.baeldung.com/java-difference-map-and-flatmap#map-and-flatmap-in-optionals
                .forEach(System.out::println);

        Stream.of(new Integer[]{1,2}, new Integer[]{3})
                .flatMap(i -> Arrays.stream(i))// flatMap returns Stream/Optional of our type  https://www.baeldung.com/java-difference-map-and-flatmap#map-and-flatmap-in-optionals
                .map(i->i*1000)
                .forEach(System.out::println);

        Stream.of(new int[]{1,2}, new int[]{3})
                .flatMapToInt(i -> Arrays.stream(i))// flatMapToInt used for primitives
                .map(i->i*10000)
                .forEach(System.out::println);
    }


    // ------------------------------ Streams ------------------------------
//    public static void main(String args[]) {
//
//        List<String> cats = new ArrayList<>();
//        cats.add("Annie");
//        cats.add("Ripley");
//        Stream<String> stream = cats.stream();
//        cats.add("KC");
//        System.out.println(stream.count()); // correct answer is 3, Streams are LAZILY evaluated, here stream pipeline actually runs.
//
//
//
//        List<String> list = Arrays.asList("Adam", "Alexander", "John", "Tom");
//        Predicate<String> predicate1 = str -> str.startsWith("A");
//        Predicate<String> predicate2 =  str -> str.length() < 5;
//        List<String> result =  list.stream()
//                .filter(predicate1.and(predicate2)) // Use Predicate.and(), Predicate.or(), and Predicate.negate() for multiple predicates
//                .collect(Collectors.toList());
//        assertEquals(1, result.size());
//        assertThat(result, contains("Adam"));
//
//        List<Predicate<String>> predicates = List.of( // List.of returns immutable, Arrays.asList returns mutable: https://stackoverflow.com/a/46579348
//                str -> str.startsWith("A"),
//                str -> str.length() > 4,
//                str -> str.contains("d"));
//        result = list.stream()
//                .filter(predicates.stream().reduce(x->true, Predicate::and)) // reduce - produce one single result from a sequence of elements
//                .collect(Collectors.toList());
//        assertEquals(1, result.size());
//        assertThat(result, contains("Alexander"));
//
//
//        Map<String, Integer> counts = new HashMap<>();
//        counts.put("Jenny", 1);
//        counts.computeIfPresent("Jenny", (k, v) -> null); // removes the key from the map
//        counts.computeIfAbsent("Sam", k -> null); // doesn’t add a key
//        System.out.println(counts); // {}
//
//
//        Stream<String> st = Stream.of("w", "o", "l", "f");
//        StringBuilder word = st .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
//        System.out.println(word); // wolf
//
//        Stream<String> st2 = Stream.of("lions","tigers", "bears"); //
//
//        Map<String, Integer> map = st2.collect(Collectors.toMap(s -> s, String::length)); // Duplicate keys ("lions","lions") will throw java.lang.IllegalStateException
//        System.out.println(map); // {lions=5, bears=5, tigers=6}
//
//        Stream<String> st3 = Stream.of("lions", "lions", "tigers", "bears");
//        Map<Integer, String> map2 = st3.collect(Collectors.toMap(String::length, k -> k, (s1, s2) -> s1 + "," + s2));  // resolves duplicate key issue
//        System.out.println(map2); // {5=lions,bears, 6=tigers}
//    }
}
