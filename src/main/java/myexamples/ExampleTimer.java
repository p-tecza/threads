package myexamples;

import java.util.Timer;
import java.util.TimerTask;

public class ExampleTimer {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new KindOfTask(), 1000);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("EO");
            }
        }, 0, 250);

        Timer timer3 = new Timer();
        timer3.schedule(new TimerCanceller(timer2, timer), 3000);
    }

}

class KindOfTask extends TimerTask{

    @Override
    public void run() {
        System.out.println("Wywolanie metody po 1s");
    }
}

class TimerCanceller extends TimerTask{

    private final Timer timerRef;
    private final Timer timerRef2;
    TimerCanceller(Timer timerRef, Timer timerRef2){
        this.timerRef = timerRef;
        this.timerRef2 = timerRef2;

    }

    @Override
    public void run() {
        System.out.println("STOP! teras wengorz");
        this.timerRef.cancel();
        this.timerRef2.cancel();
        this.cancel();
    }
}
