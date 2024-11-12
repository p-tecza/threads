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
public class Spiral extends Applet implements Runnable {

    private Graphics2D offscreenGraphics;
    private Image offscreenImage;
    private Thread animationThread;
    private double x, y;
    private int counter, spiralAngle;

    @Override
    public void init() {
        resize(400, 400);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (animationThread == null) {
                    animationThread = new Thread(Spiral.this);
                    animationThread.start();
                } else {
                    animationThread = null;
                }
            }
        });

        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        animationThread = new Thread(this);
        x = getWidth() / 2;
        y = getHeight() / 2;
        counter = 0;
        spiralAngle = 0;
    }
    
    @Override
    public void start() {
        animationThread.start();
    }

    public void run() {
        while (animationThread != null) {
            offscreenGraphics.setColor(Color.BLACK);
            for (; counter < 1440 && animationThread != null; counter++) {
                spiralAngle = counter;

                x = spiralAngle / 10. * cos(spiralAngle * PI / 180) + getWidth() / 2;
                y = spiralAngle / 10. * sin(spiralAngle * PI / 180) + getHeight() / 2;

                offscreenGraphics.fillOval((int) (x - 5), (int) (y - 5), 10, 10);

                repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                }
            }
            offscreenGraphics.setColor(Color.WHITE);
            for (; counter < 2880 && animationThread != null; counter++) {
                spiralAngle = -counter + 2880;

                x = spiralAngle / 10. * cos(spiralAngle * PI / 180) + getWidth() / 2;
                y = spiralAngle / 10. * sin(spiralAngle * PI / 180) + getHeight() / 2;

                offscreenGraphics.fillOval((int) (x - 5), (int) (y - 5), 10, 10);

                repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                }
            }
            if (animationThread != null) {
                counter = 0;
            }
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
