/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.beeper;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.*;

/**
 *
 * @author Dominik Olszewski
 */
public class BeeperUsingExecutor {

    private final ScheduledExecutorService beeper;
    private final int nBeeps;
    private ScheduledFuture<?> beepingHandle;

    private final String beeperId;
    
    public BeeperUsingExecutor(int nBeeps, String beeperId) {
        beeper = new ScheduledThreadPoolExecutor(1);
        this.beeperId = beeperId;
        this.nBeeps = nBeeps;
    }
    
    public void startBeeping() {
        beepingHandle = beeper.scheduleAtFixedRate(() -> {
            System.out.println("Beep " + this.beeperId);
        }, 1, 1, SECONDS);
        beeper.schedule(() -> {beepingHandle.cancel(true); beeper.shutdown();}, nBeeps, SECONDS);
    }

    public void startBeepingWithFixedDelay(long msToKeepAlive){
        beepingHandle = beeper.scheduleWithFixedDelay(() -> {
            System.out.println("Beep " + this.beeperId);
        }, 1, 1, SECONDS);
        beeper.schedule(() -> {beepingHandle.cancel(true); beeper.shutdown();}, msToKeepAlive, MILLISECONDS);
    }

    public static void main(String[] args) {
        BeeperUsingExecutor beeperNo1Ref = new BeeperUsingExecutor(5, "beeper#1");
        beeperNo1Ref.startBeeping();
        new BeeperUsingExecutor(5, "beeper#2").startBeepingWithFixedDelay(10500);
    }
}
