/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.simplegame;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.*;

/**
 *
 * @author Dominik Olszewski
 */
public class SimpleGameApplet extends Applet implements Runnable {

    private Thread gameThread;
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private double xCenter, yCenter, angle;
    private boolean stopFlag;

    @Override
    public void init() {
        resize(800, 800);
        gameThread = new Thread(this);
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        xCenter = random() * (getWidth() - 200) + 100;
        yCenter = random() * (getHeight() - 200) + 100;
        angle = random() * 2 * PI;
        stopFlag = false;
        
        setFocusable(true);
        
        addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() ==  KeyEvent.VK_LEFT) {
                    angle -= 0.2;
                } else if (event.getKeyCode() ==  KeyEvent.VK_RIGHT) {
                    angle += 0.2;
                }
            }
        });
    }

    @Override
    public void start() {
        gameThread.start();
    }

    @Override
    public void run() {
        while (!stopFlag) {
            offscreenGraphics.fillOval((int) xCenter - 10, (int) yCenter - 10, 20, 20);

            xCenter += 4. * cos(angle);
            yCenter += 4. * sin(angle);

            try {
                Thread.sleep(30);
            } catch (InterruptedException ie) {
            }
            
            if (xCenter - 10 <= 0 || xCenter + 10 >= getWidth() || yCenter - 10 <= 0
                    || yCenter + 10 >= getHeight()) {
                stopFlag = true;
                offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
                offscreenGraphics.setFont(new Font("Arial", Font.PLAIN, 50));
                offscreenGraphics.drawString("Game Over", getWidth() / 2 - 120, getHeight() / 2 - 10);
            }

            repaint();
        }
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
