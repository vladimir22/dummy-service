package interview;

import java.util.concurrent.CompletableFuture;

public class Exceptions {

    public static class Class_That_Throws implements AutoCloseable { // https://www.baeldung.com/java-try-with-resources#custom
        public void throw_throwable () throws Throwable {
            throw new Throwable("test");
        }
        public void throw_RuntimeException () throws RuntimeException {
            throw new RuntimeException("test");
        }
        @Override
        public void close() throws Exception {
            throw new NullPointerException("test"); // Exceptions in the close method will be Suppressed by basic exception from try-catch block
        }
    }


    public static void main (String... args){

        Class_That_Throws class_that_throws = new Class_That_Throws();

        try {
            class_that_throws.throw_throwable(); // try-catch is required: Throwable is CHECKED exception
        } catch (Throwable e) {
            System.out.println(e);
        }


//        class_that_throws.throw_RuntimeException(); // try-catch is not needed for unchecked RuntimeException


        try (Class_That_Throws class_that_throws1 = new Class_That_Throws();
             Class_That_Throws class_that_throws2 = new Class_That_Throws()) {

            class_that_throws2.throw_RuntimeException();

        } catch (Exception e) {
            e.printStackTrace(); // RuntimeException will be thrown + Suppressed: java.lang.NullPointerException from close() method
        }


        try (Class_That_Throws class_that_throws1 = new Class_That_Throws();
             Class_That_Throws class_that_throws2 = new Class_That_Throws()) {

            class_that_throws2.throw_RuntimeException();

            // finally block may not be executed in case of JVM problems or interrupted thread:
            // - System.exit(1);
            // - JVM sends OutOfMemory
            // - Thread.currentThread().stop();
        } finally {
             throw new ArithmeticException(); // ArithmeticException will be thrown in ANY case!



        }
    }

}
