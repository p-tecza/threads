/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.beeper;

import java.awt.Toolkit;

/**
 *
 * @author Dominik Olszewski
 */
public class BurstBeeperThread extends Thread {
    
    private final long shortInterval;
    private final long longInterval;
    
    public BurstBeeperThread(long shortInterval, long longInterval) {
        this.shortInterval = shortInterval;
        this.longInterval = longInterval;
    }
    
    private class BurstThread extends Thread {
        
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                Toolkit.getDefaultToolkit().beep();
                
                try {
                    sleep(shortInterval);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            new BurstThread().start();
            
            try {
                sleep(longInterval);
            } catch (InterruptedException ie) {
            }
        }
    }
    
    public static void main(String[] args) {
        new BurstBeeperThread(250, 1000).start();
    }
}
