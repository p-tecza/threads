/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.counter;

/**
 *
 * @author Dominik Olszewski
 */
public class CounterThread implements Runnable {

    private final Thread counterThread;

    public CounterThread() {
        counterThread = new Thread(this);
    }

    public void start() {
        counterThread.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CounterThread().start();
        //new BeeperThread().start();
    }
}
