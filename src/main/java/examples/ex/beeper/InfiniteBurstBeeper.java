/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.beeper;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Dominik Olszewski
 */
public class InfiniteBurstBeeper {
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Incorrect number of input arguments.");
            System.exit(1);
        }
        Timer beepTimer = new Timer();
        
        beepTimer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                Toolkit.getDefaultToolkit().beep();
                
                try {
                    Thread.sleep(Long.parseLong(args[0]));
                } catch (InterruptedException ie) {
                }
            }
            }
        }, 0, Long.parseLong(args[1]));
        
        System.out.print("Press enter to stop...");
        try {
            System.in.read();
        } catch (IOException ioe) {
        }
        beepTimer.cancel();
    }
}
