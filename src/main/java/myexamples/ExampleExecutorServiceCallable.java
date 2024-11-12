package myexamples;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExampleExecutorServiceCallable {

    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger(0);
        Object obj = new Object();
        ArrayBlockingQueue<Runnable> tasks = new ArrayBlockingQueue<>(3);
        ExecutorService executor = new ThreadPoolExecutor(3, 3, 1, TimeUnit.SECONDS, tasks);

        ExecutorService testExecutor = Executors.newFixedThreadPool(3); //TODO inna opcja stworzenia executora

        Future<?> res1 = executor.submit(new CallableCalculator(counter, obj, "A"));
        Future<?> res2 = executor.submit(new CallableCalculator(counter, obj, "B"));
        Future<?> res3 = executor.submit(new CallableCalculator(counter, obj, "C"));
        executor.shutdown();

        try {
            if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println(counter);
                try {
                    System.out.println("TU:" + res1.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class CallableCalculator implements Callable<Integer> {
    int toIncrement = 1000;
    final Object chamskiLock;
    AtomicInteger counter;
    String name;

    CallableCalculator(AtomicInteger counter, Object chamskiLock, String name) {
        this.counter = counter;
        this.chamskiLock = chamskiLock;
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {

        System.out.println("Running counter: " + counter);
        System.out.println(toIncrement);
        while (toIncrement-- != 0) {
            synchronized (chamskiLock) {
                counter.getAndIncrement();
                System.out.println(name+": "+counter.get());
            }
        }
        return counter.get();
    }
}
