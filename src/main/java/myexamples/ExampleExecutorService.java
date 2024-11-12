package myexamples;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExampleExecutorService {

    public static void main(String[] args){
        AtomicInteger counter = new AtomicInteger(0);
        Object obj = new Object();
        ArrayBlockingQueue<Runnable> tasks = new ArrayBlockingQueue<>(3);
        ExecutorService executor = new ThreadPoolExecutor(3,3, 1, TimeUnit.SECONDS, tasks);
        Future<?> res1 = executor.submit(new Calculator(counter, obj)); // TODO w tych Future jest null bo nie korzystamy z callable
        Future<?> res2 = executor.submit(new Calculator(counter, obj));
        Future<?> res3 = executor.submit(new Calculator(counter, obj));
        executor.shutdown();

        try {
            if(executor.awaitTermination(5, TimeUnit.SECONDS)){
                System.out.println(counter);
                try {
                    System.out.println(res1.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }


}


class Calculator implements Runnable{

    int toIncrement = 100000;
    final Object chamskiLock;
    AtomicInteger counter;
    Calculator(AtomicInteger counter, Object chamskiLock){
        this.counter = counter;
        this.chamskiLock = chamskiLock;
    }
    @Override
    public void run() {
        synchronized (chamskiLock){
            System.out.println("Running counter: "+ counter);
            System.out.println(toIncrement);
            while(toIncrement-- != 0){
                counter.getAndIncrement();
//                System.out.println(toIncrement);
            }
        }
//        return counter;
    }
}
