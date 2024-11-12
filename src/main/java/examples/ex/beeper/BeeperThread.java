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
public class BeeperThread extends Thread {
    
    private final Toolkit toolkit;

    public BeeperThread() {
        toolkit = Toolkit.getDefaultToolkit();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            toolkit.beep();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
        }
    }
}
