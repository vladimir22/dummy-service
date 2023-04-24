package interview;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CompletableFuture_Examples {


    public static void main (String... args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(500);         // имитируем долгое выполнение
            } catch (InterruptedException e) {}
            return "Hi";
        }); // It is possible to specify dedicated threadPool  by adding "Executors.newCachedThreadPool()" argument

        System.out.println(future.get()); //output Hi
        System.out.println(future.get()); //output Hi (future executed once, it keeps response in get())


        CompletableFuture.supplyAsync(() -> 1)
                .thenApply(x -> x+1) // thenApply is used if you have a synchronous mapping function.
                .thenAccept(System.out::println);  //output 2


        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> 10)
                .thenCompose(result -> // thenCompose is used if you have an asynchronous mapping function. It will then return a future with the result directly, rather than a nested future.
                        CompletableFuture.supplyAsync(() -> result * 2)
                ).thenCompose(result ->
                        CompletableFuture.supplyAsync(() -> result * 5)
                );
        System.out.println(future3.get()); //output 100



    }
}
