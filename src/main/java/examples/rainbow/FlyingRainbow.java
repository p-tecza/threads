package examples.rainbow;

import java.applet.Applet;
import java.awt.*;
import java.util.*;

public class FlyingRainbow extends Applet implements Runnable {
    
    private Thread animationThread;
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private RadialGradientPaint paint;
    private Toolkit toolkit;
    private Random rg;
    private Color[] colors;
    private double x, y, xInc, yInc;
    private float[] fractions;
    private int radius, velocity, delay;

    @Override
    public void init() {
        resize(500, 400);
        animationThread = new Thread(this);
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        toolkit = Toolkit.getDefaultToolkit();
        rg = new Random();
        colors = new Color[] {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE};
        double angle = rg.nextDouble() * 2 * Math.PI;
        fractions = new float[] {0.0f, 0.01f, 0.5f, 1.0f};
        radius = 30;
        velocity = 3;
        delay = 30;
        x = rg.nextInt(getWidth() - 2 * radius) + radius;
        y = rg.nextInt(getHeight() - 2 * radius) + radius;
        xInc = velocity * Math.cos(angle);
        yInc = velocity * Math.sin(angle);
    }
    
    @Override
    public void start() {
        animationThread.start();
    }

    /**
     * Adds an element, passed as the second argument, to the destination
     * array of Objects, passed as the first argument, at the first position. 
     * Elements stored in the array are shifted right. The last element is 
     * removed from the array.
     * @param array the destination array of any instances of Object class
     * @param element an element inserted into destination array at the first position
     */
    private void addFirst(Object[] array, Object element) {
        if (array instanceof Object && array.length != 0) {
            for (int i = array.length - 2; i >= 0; i--) {
                array[i + 1] = array[i];
            }
            array[0] = element;
        }
    }

    @Override
    public void run() {
        while (true) {
            fractions[1] += 0.01f;
            fractions[2] += 0.01f;
            if (fractions[2] >= 0.99f) {
                fractions[2] = fractions[1];
                fractions[1] = 0.01f;
                addFirst(colors, new Color(rg.nextFloat(), rg.nextFloat(), rg.nextFloat()));
            }
            paint = new RadialGradientPaint((float) x, (float) y, radius, fractions, colors);
            offscreenGraphics.setPaint(paint);
            offscreenGraphics.fillOval((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
            offscreenGraphics.setColor(Color.BLACK);
            offscreenGraphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            repaint();

            try {
                Thread.sleep(delay);
            } catch (InterruptedException ie) {
            }
            
            offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());

            x += xInc;
            y += yInc;

            if (x < radius || x > getWidth() - radius) {
                xInc = -xInc;
                toolkit.beep();
            }
            if (y < radius || y > getHeight() - radius) {
                yInc = -yInc;
                toolkit.beep();
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
