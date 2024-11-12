/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.competingthreads;

/**
 *
 * @author Dominik Olszewski
 */
public class CompetingThreads {

    public static void main(String[] args) {
        PolightThread polightThread = new PolightThread();
        polightThread.start();
        polightThread.startMeanThread();
    }
}

class PolightThread extends Thread {
    
    private final MeanThread meanThread;
    
    public PolightThread() {
        meanThread = new MeanThread(this);
    }
    
    public void startMeanThread() {
        meanThread.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getId() + ": " + getName() + " iteration: " + i);

            try {
                sleep((long) (Math.random() * 3000));
            } catch (InterruptedException ie) {
                System.out.println(getId() + ": Don't disturb me!");
                //meanThread.interrupt();
            }
        }
    }
}

class MeanThread extends Thread {
    
    private final PolightThread polightThread;
    
    public MeanThread(PolightThread polightThread) {
        this.polightThread = polightThread;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getId() + ": " + getName() + " iteration: " + i);
            
            try {
                sleep((long) (Math.random() * 3000));
            } catch (InterruptedException ie) {
                System.out.println(getId() + ": Don't disturb me!");
            }
            polightThread.interrupt();
        }
    }
}
