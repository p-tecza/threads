/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.counter;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author Dominik Olszewski
 */
public class CounterApplet extends Applet {

    private Timer counterTimer;
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private DecimalFormat format;
    private int counter;

    private class CounterInc extends TimerTask {

        @Override
        public void run() {
            offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
            offscreenGraphics.drawString(String.valueOf(format.format(counter++)),
                    getWidth() / 2 - 55,
                    getHeight() / 2 + 30);
            repaint();
        }
    }

    @Override
    public void init() {
        resize(400, 400);
        counterTimer = new Timer();
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.setFont(new Font("Arial", Font.PLAIN, 100));
        format = (DecimalFormat) DecimalFormat.getInstance();
        format.applyPattern("00");
        counter = 0;

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (counterTimer == null) {
                    counterTimer = new Timer();
                    counterTimer.schedule(new CounterInc(), 1000, 1000);
                } else {
                    counterTimer.cancel();
                    counterTimer = null;
                }
            }
        });
    }

    @Override
    public void start() {
        counterTimer.schedule(new CounterInc(), 0, 1000);
    }

    @Override
    public void paint(Graphics g) {
        update(g);
    }
    
    @Override
    public void update(Graphics g) {
        g.drawImage(offscreenImage, 0, 0, this);
    }
}
