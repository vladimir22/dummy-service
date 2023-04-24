package interview;



import java.util.ArrayList;
import java.util.List;

public class Generic {

    public static class Bird {}
    public static class Sparrow extends Bird {}


    public static <T> void sink(T t) { } // the formal parameter type immediately before the return type of void
    public static <T> T identity(T t) { return t; } // return type being the formal parameter type. It looks weird, but it is correct
    //public static T noGood(T t) { return t; } // DOES NOT COMPILE - omits <T> the formal parameter type


    public static void main(String[] args) {
        List<? extends Bird> birds = new ArrayList<Bird>(); //  list becomes logically immutable
        //birds.add(new Sparrow()); // DOES NOT COMPILE : we can’t add a Sparrow to List<Bird>
        //birds.add(new Bird()); // DOES NOT COMPILE : we can’t add a Bird to List<Sparrow>.

        List<String> strings = new ArrayList<>();
        strings.add("string");
        List<Object> objects = new ArrayList<>(strings);
        addSound(strings);
        addSound(objects);
    }

//    list.add(null);

//    public static void addSound(List<? super String> list){list.add("text");} // OK - we can add string to  List<String> or List<Object>

//    public static void addSound(List<?> list){list.add("text");} // DOES NOT COMPILE - unbounded generics are immutable
//    public static void addSound(List<? extends Object> list){list.add("text");}	// DOES NOT COMPILE - upperbounded generics are immutable
//    public static void addSound(List<Object> list){list.add("text");} // CANNOT ACCEPT List<String> - must pass exact match List<Object>
    public static void addSound(List<? super String> list){list.add("text");} // DOES NOT COMPILE - unbounded generics are immutable + CANNOT ACCEPT List<Object>

//    public static void addSound(List<? super String> list){list.add(new Object());} // DOES NOT COMPILE -  we cannot add object into List<String>

//    public static void addSound(List<? super String> list){list.add(new Object());} // CANNOT ACCEPT List<String> -  Object is not superclass for String

}
