package interview;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Primitives {


    public static String getWith0(String format, String result) {
        return String.format("%32s", result).replaceAll(" ", "0");  // 32-bit Integer
    }

    public static String getInt32Binary(int value) {
        return getWith0("%32s", Integer.toBinaryString(value));  // 32-bit Integer
    }

    public static void printInt32Bin(int value) {
        System.out.println(String.format(" %s -> %s", getInt32Binary(value), String.valueOf(value)));
    }

    public static void main(String args[]) {
//        printInt32Bin(Integer.MIN_VALUE);     //  10000000000000000000000000000000 -> -2147483648
//        printInt32Bin(-1);                    //  11111111111111111111111111111111 -> -1
//        printInt32Bin(0);                     //  00000000000000000000000000000000 -> 0
//        printInt32Bin(Integer.MAX_VALUE);     //  01111111111111111111111111111111 -> 2147483647

//        printInt32Bin(-1);
//        printInt32Bin(16);
//        printInt32Bin(-1 >>> 16);


        int key_hashCode = 22;
        int h;
        int tab_id = (h = key_hashCode) ^ (h >>> 16);

        printInt32Bin(key_hashCode);
        printInt32Bin(key_hashCode >>> 16);
        printInt32Bin(key_hashCode ^ (key_hashCode >>> 16));
        printInt32Bin(tab_id);

    }




}
