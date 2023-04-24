package interview;

import okhttp3.internal.concurrent.Task;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

public class Collections {



    // ----- Spliterator:  https://www.digitalocean.com/community/tutorials/java-spliterator#java-spliterator
//    public static void main(String[] args) {
//        List<String> list = Arrays.asList("Java", "Android", "MySQL", "Python");
//        Spliterator<String> splitr = list.spliterator();
//        Spliterator<String> st = splitr.trySplit(); // split for parallel calculation: https://www.baeldung.com/java-spliterator#2-trysplit
//        splitr.forEachRemaining(System.out::println);
//        System.out.println("--Traversing the other half of the spliterator---");
//        st.forEachRemaining(System.out::println);
//    }




    // ----- TreeMap: https://www.baeldung.com/java-treemap#importance-of-treemap-sorting
//    public static void main(String args[]) {
//        TreeMap<Integer, String> map = new TreeMap<>();
//        map.put(3, "val");
//        map.put(2, "val");
//        map.put(1, "val");
//        map.put(5, "val");
//        map.put(4, "val");
//
//        Integer highestKey = map.lastKey();
//        Integer lowestKey = map.firstKey();
//        Set<Integer> keysLessThan3 = map.headMap(3).keySet();
//        Set<Integer> keysGreaterThanEqTo3 = map.tailMap(3).keySet();
//
//        assertEquals(new Integer(5), highestKey);
//        assertEquals(new Integer(1), lowestKey);
//        assertEquals("[1, 2]", keysLessThan3.toString());
//        assertEquals("[3, 4, 5]", keysGreaterThanEqTo3.toString());
//    }



//    // ----- LinkedList & ArrayDeque: https://www.baeldung.com/java-array-deque#1-using-arraydeque-as-a-stack
//    public static void main(String args[]) {
//        /*
//          transient int size = 0;
//          transient LinkedList.Node<E> first;
//          transient LinkedList.Node<E> last;
//        */
//        LinkedList<Long> linkedList =  new LinkedList<>();
//
//        linkedList.add(1L);
//        linkedList.addFirst(2L);
//        linkedList.addLast(3L);
//        print( linkedList.getFirst() ); // Returns the first element in this list.
//        print( linkedList.getLast()  ); // Returns the last element in this list.
//        print( linkedList );
//
//        print( linkedList.peek() ); // Retrieves, but does not remove, the head (first element)
//        print( linkedList.poll() ); // Retrieves and removes the head (first element) of this list.
//        print( linkedList.pollLast()); // Retrieves and removes the last element of this list, or returns null if this list is empty.
//        print( linkedList );
//
//        linkedList.push(0L); // addFirst(e);  Inserts the element at the front of this list.
//        linkedList.offer(5L) ; // return add(e); Adds the specified element as the tail (last element) of this list.
//        print( linkedList );
//        linkedList.pop(); // return removeFirst();   Removes and returns the first element of this list.
//        print( linkedList );
//
//
//        // ArrayDeque as a Stack
//        ArrayDeque<String> stack = new ArrayDeque<>(); // transient Object[] elements; All array cells not holding deque elements are always null. The array always has at least one null slot (at tail).
//        stack.push("first");
//        stack.push("second");
//        assertEquals("second", stack.getFirst());
//        assertEquals("first", stack.getLast());
//        assertEquals("second", stack.pop()); // return removeFirst();   Removes and returns the first element of this list.
//        assertEquals("first",  stack.pop());
//
//
//        // ArrayDeque as a Queue
//        Deque<String> queue = new ArrayDeque<>();
//        queue.offer("first");
//        queue.offer("second");
//        assertEquals("first", queue.getFirst());
//        assertEquals("second", queue.getLast());
//        assertEquals("first", queue.poll());     // Retrieves and removes the head (first element) of this list
//        assertEquals("second", queue.poll());
//
//
//        var _stack = new Stack<Integer>(); // Extends Class Vector (synchronized List).
//        Deque<Integer> deque = new ArrayDeque<>(); // Implements Deque<E> Interface  which is more flexible
//
//        _stack.push(1);//1 is the top
//        deque.push(1);//1 is the top
//        _stack.push(2);//2 is the top
//        deque.push(2);//2 is the top
//
//        print( deque.stream().collect(Collectors.toList()) );//[2,1]  ArrayDeque applies LIFO concept when convert to List
//        print( _stack.stream().collect(Collectors.toList()) );//[1,2]  Stack does not !
//    }



//    // ------------------------------ ArrayList ------------------------------
//    public static void main(String args[]) {
//
//        int initialCapacity = 2; // private static final int DEFAULT_CAPACITY = 10; - default initial capacity
//        ArrayList<Long> arrayList =  new ArrayList<>(initialCapacity);
//
//        Long first = 1L; Long second = 2L;
//
//        arrayList.add(first); // Add object
//        arrayList.add(second);
//        arrayList.add(1, 3L); // Add/Insert by index
//        System.out.println(arrayList);
//
//        arrayList.set(1, 4L);// Replace by index
//        System.out.println(arrayList);
//
//        arrayList.remove((int) 1); // Remove by index. Important! after remove indexes RECALCULATED !!!
//        System.out.println(arrayList);
//
//
//        arrayList.add((long) 1); //Add duplicate objects
//        arrayList.add((long) 1);
//        arrayList.add((long) 1);
//
//        arrayList.remove((long) 1); // Remove by object (only first)
//        System.out.println(arrayList);
//
//        while (arrayList.remove((long) 1)) {} // Remove by object (all duplicates)
//        System.out.println(arrayList);
//

//
//    }




//   // ------------------------------ HashMap ------------------------------
//   public static void main(String args[]) {
//        /* Default HashMap values:
//             static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16.  MUST be a power of two.
//             static final int MAXIMUM_CAPACITY = 1 << 30; // aka 1073741824. MUST be a power of two and <= 1<<30
//             static final float DEFAULT_LOAD_FACTOR = 0.75f; // расширение произойдет когда мы добавим 16 * 0.75 = 12 объектов
//             static final int TREEIFY_THRESHOLD = 8; // bin count threshold for using a tree rather than list
//             static final int UNTREEIFY_THRESHOLD = 6; // threshold for untreeifying a (split) bin during a resize operation
//             static final int MIN_TREEIFY_CAPACITY = 64 // smallest table capacity for which bins may be treeified
//         */
//
//        int initialCapacity = 3; // but final capacity (this.threshold = tableSizeFor(initialCapacity)) will be powered by 2 = 4
//        float loadFactor = 0.75F;
//
//        HashMap<Integer, String> hashMap = new HashMap<>(initialCapacity, loadFactor);
//
//        hashMap.put(1, "value1");
//        hashMap.put(2, "value2");
//
//        System.out.println(hashMap.get(2));
//    }
//
//
//

   // ------------------------------ HashSet & LinkedHashSet ------------------------------
   public static void main(String args[]) {
        /*
        HashSet<E> extends AbstractSet<E> implements Set<E>
          private transient HashMap<E,Object> map;
          private static final Object PRESENT = new Object();  // Dummy value to associate with an Object in the backing Map
         */

        int initialCapacity = 3; // but final capacity powered by 2 = 4 (this.threshold = tableSizeFor(initialCapacity))
        float loadFactor = 0.75F;

        HashSet<String> hashSet = new HashSet<>(initialCapacity, loadFactor);

        hashSet.add("hashSet-value1");
        hashSet.add("hashSet-value2");
        hashSet.add("hashSet-value3");
        System.out.println(hashSet); // [hashSet-value3, hashSet-value2, hashSet-value1]


        /*
            LinkedHashSet<E> extends HashSet<E> implements Set<E>
            ...
            @Override
            public Spliterator<E> spliterator() {
                return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.ORDERED); //order is defined for elements(Spliterator guarantees that method trySplit splits a strict prefix of elements, that method tryAdvance steps by one element in prefix order, and that forEachRemaining performs actions in encounter order.
            }
         */
       LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(initialCapacity, loadFactor);
       linkedHashSet.add("linkedHashSet_value1");
       linkedHashSet.add("linkedHashSet_value3");
       linkedHashSet.add("linkedHashSet_value2");
       System.out.println(linkedHashSet); // [linkedHashSet_value1, linkedHashSet_value3, linkedHashSet_value2]


       /*
         TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>
           private transient NavigableMap<E,Object> m;
           private static final Object PRESENT = new Object(); // Dummy value to associate with an Object in the backing Map
        */
       TreeSet<String> treeSet = new TreeSet<>();
       treeSet.add("treeSet_value1");
       treeSet.add("treeSet_value3");
       treeSet.add("treeSet_value2");
       System.out.println(treeSet); // [treeSet_value1, treeSet_value2, treeSet_value3]
    }



    // -------------------- Common Methods --------------------
    public static void print (Object o){
        System.out.println(o);
    }
}
