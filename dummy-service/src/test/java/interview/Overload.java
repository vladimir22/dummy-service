package interview;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Overload {


    public void method(Object o) {
        System.out.println("Object");
    }

    public void method(java.io.IOException i) {
        System.out.println("IOException");
    }

    public void method(java.io.FileNotFoundException f) {
        System.out.println("FileNotFoundException");
    }





    public static void main(String args[]) {

//        HashMap hashMap = new HashMap<>();
//
//
//
//
//        HashSet set = new HashSet();
//
//
//
//
//
//        Set set = new Set
//
//        set.
//
//        IntStream.concat(Arrays.stream(firstArray), Arrays.stream(secondArray))
//                .distinct()
//                .sorted()
//                .forEach(System.out::println);
//
//
//
//        int[] firstArray = new int[]{1, 3, 4, 5, 56, 6};
//        int[] secondArray = new int[]{11, 8, 3, 4, 5, 56, 6};


    }

}




//class TxManager {
//
//    @Transactional
//    public void method1() throws Exception{
//        method2();
//        // some update in DB {2}
//        throw new Exception();
//    }
//
//    @Transactional(propagation = REQUIRED_NEW)
//    private void method2() {
//        // some update in DB {1}
//    }
//}

