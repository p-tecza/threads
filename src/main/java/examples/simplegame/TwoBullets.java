/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.simplegame;

import java.applet.Applet;
import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;

/**
 *
 * @author Dominik Olszewski
 */
public class TwoBullets extends Applet {
    
    private Bullet firstBullet;
    private Bullet secondBullet;
    private Screen screen;
    
    @Override
    public void init() {
        resize(400, 400);
        screen = new Screen(getWidth(), getHeight());
        firstBullet = new Bullet("Bullet1", screen);
        secondBullet = new Bullet("Bullet2", screen);
        add(screen);
    }
    
    @Override
    public void start() {
        screen.setGraphics();
        firstBullet.start();
        secondBullet.start();
    }
}

class Bullet extends Thread {
    
    private double xCenter, yCenter, xInc, yInc;
    private final int radius, width, height;
    private final Screen screen;
    
    public Bullet(Screen screen) {
        double angle = random() * 2 * PI;
        radius = 10;
        width = screen.getWidth();
        height = screen.getHeight();
        xCenter = random() * (width - 2 * radius) + radius;
        yCenter = random() * (height - 2 * radius) + radius;
        xInc = 3. * cos(angle);
        yInc = 3. * sin(angle);
        this.screen = screen;
    }
    
    public Bullet(String name, Screen screen) {
        super(name);
        double angle = random() * 2 * PI;
        radius = 10;
        width = screen.getWidth();
        height = screen.getHeight();
        xCenter = random() * (width - 2 * radius) + radius;
        yCenter = random() * (height - 2 * radius) + radius;
        xInc = 3. * cos(angle);
        yInc = 3. * sin(angle);
        this.screen = screen;
    }
    
    @Override
    public void run() {
        while (true) {
            screen.drawBullet(xCenter, yCenter, xInc, yInc, radius);
            
            xCenter += xInc;
            yCenter += yInc;
            
            if (xCenter - radius <= 0 || xCenter + radius >= width) {
                xInc = -xInc;
            }
            if (yCenter - radius <= 0 || yCenter + radius >= height) {
                yInc = -yInc;
            }
            
            try {
                sleep(30);
            } catch (InterruptedException ie) {}
        }
    }
}

class Screen extends Canvas {
    
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private final Ellipse2D.Double bulletEllipse;
    
    public Screen(int width, int height) {
        setSize(width, height);
        bulletEllipse = new Ellipse2D.Double();
    }
    
    public void setGraphics() {
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public synchronized void drawBullet(double x, double y, double xInc, double yInc, int radius) {
        offscreenGraphics.setColor(Color.WHITE);
        bulletEllipse.setFrame(x - (radius + 1), y - (radius + 1), 2 * (radius + 1), 2 * (radius + 1));
        offscreenGraphics.fill(bulletEllipse);
        
        offscreenGraphics.setColor(Color.BLACK);
        bulletEllipse.setFrame(x + xInc - radius, y + yInc - radius, 2 * radius, 2 * radius);
        offscreenGraphics.fill(bulletEllipse);
        
        repaint();
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
