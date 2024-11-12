package myexamples;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.*;

public class ExampleScheduledExecutorService {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor ses = new ScheduledThreadPoolExecutor(3);
        ses.setRemoveOnCancelPolicy(true);
        ALU alu = new ALU();
        ScheduledFuture<?> future1 = ses.scheduleAtFixedRate(new CalculationRunner(alu), 0, 250, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> future2 = ses.scheduleAtFixedRate(new CalculationRunner(alu), 0, 250, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> future3 = ses.scheduleAtFixedRate(new CalculationRunner(alu), 0, 250, TimeUnit.MILLISECONDS);
        alu.setFutures(future1, future2, future3);
        ses.shutdown();
//        try{
//            if(ses.awaitTermination(5, TimeUnit.SECONDS)){
//                ses.shutdown();
//            }
//        }catch (Exception e){
//
//        }
    }
}

class CalculationRunner implements Runnable {
    private final ALU alu;
//    int toIncrement = 1000;

    CalculationRunner(ALU alu) {
        this.alu = alu;
    }

    @Override
    public void run() {
        alu.increment();
        System.out.println(this.hashCode() + ":" + alu.suma);
    }
}

class ALU {
    int suma = 0;

    private ScheduledFuture<?> future1;
    private ScheduledFuture<?> future2;
    private ScheduledFuture<?> future3;

    // Ustawienie referencji do zadań ScheduledFuture
    public void setFutures(ScheduledFuture<?> future1, ScheduledFuture<?> future2, ScheduledFuture<?> future3) {
        this.future1 = future1;
        this.future2 = future2;
        this.future3 = future3;
    }

    public synchronized void increment() {
        if(this.suma == 100){
            System.out.println("WYWOLANIE");
            if (future1 != null) future1.cancel(true);
            if (future2 != null) future2.cancel(true);
            if (future3 != null) future3.cancel(true);
            System.out.println("WYWOLANIE2");
            return;
        }
        suma++;
    }
}
