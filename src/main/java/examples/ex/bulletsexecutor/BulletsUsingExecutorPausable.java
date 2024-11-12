/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.bulletsexecutor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.awt.geom.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import static java.lang.Math.*;

/**
 *
 * @author Dominik Olszewski
 */
public class BulletsUsingExecutorPausable {

    private JFrame frame;
    private Queue<PausableBullet> bullets;
    private Queue<ScheduledFuture<?>> bulletsHandles;
    private ScheduledExecutorService animationExecutor;
    private PausableScreen screen;
    private int nBullets;
    private boolean animationPaused;

    public BulletsUsingExecutorPausable() {
        frame = new JFrame("Bullets: click to pause, another click to resume");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setLayout(new GridLayout());

        bullets = new ArrayDeque<>();
        animationExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        screen = new PausableScreen();
        nBullets = 100;
        animationPaused = false;
        screen.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                if (animationPaused) {
                    animationPaused = false;
                    scheduleBullets();
                } else {
                    cancelBullets();
                    animationPaused = true;
                }
            }
        });
        frame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent event) {
                if (animationPaused) screen.updateGraphics();
                else screen.setGraphics();
            }
        });
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowIconified(WindowEvent event) {
                cancelBullets();
            }

            @Override
            public void windowDeiconified(WindowEvent event) {
                scheduleBullets();
            }
        });
        frame.add(screen);

        frame.pack();
        frame.setVisible(true);

        screen.setGraphics();
        createBullets();
        scheduleBullets();
    }

    private void createBullets() {
        int i = 0;
        while (i < nBullets) {
            PausableBullet bullet = new PausableBullet(new Color((int) (random() * 0xFFFFFF)), screen);
            if (bullets.stream().noneMatch(b -> b.distance(bullet) < b.getRadius() + bullet.getRadius())) {
                bullets.add(bullet);
                i++;
            }
        }
        bullets.stream().forEach(b -> b.setNeighbors(new ArrayDeque<>(bullets)));
    }

    private void scheduleBullets() {
        if (!animationPaused) {
            bulletsHandles = bullets.stream()
                    .map(b -> animationExecutor.scheduleWithFixedDelay(b, 0, 30, TimeUnit.MILLISECONDS))
                    .collect(Collectors.toCollection(ArrayDeque::new));
        }
    }

    private void cancelBullets() {
        if (!animationPaused) {
            bulletsHandles.stream().forEach(b -> b.cancel(true));
            screen.saveGraphics();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BulletsUsingExecutorPausable::new);
    }
}

class PausableBullet implements Runnable {

    private double x, y, dx, dy;
    private final double norm;
    private final int radius;
    private Queue<PausableBullet> neighbors;
    private final Color color;
    private final PausableScreen screen;

    {
        norm = 3.;
        double angle = random() * 2 * PI;
        dx = norm * cos(angle);
        dy = norm * sin(angle);
        radius = 10;
    }

    public PausableBullet(PausableScreen screen) {
        x = random() * (screen.getWidth() - 2 * radius) + radius;
        y = random() * (screen.getHeight() - 2 * radius) + radius;
        color = Color.BLACK;
        this.screen = screen;
    }

    public PausableBullet(Color color, PausableScreen screen) {
        x = random() * (screen.getWidth() - 2 * radius) + radius;
        y = random() * (screen.getHeight() - 2 * radius) + radius;
        this.color = color;
        this.screen = screen;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public void setNeighbors(Queue<PausableBullet> neighbors) {
        this.neighbors = neighbors;
        this.neighbors.remove(this);
    }

    private void collisionCheck() {
        neighbors.stream()
                .filter(b -> b.distance(this) <= b.radius + radius)
                .forEach(b -> {
                    b.hit(this);
                    hit(b);
                });
    }

    private void hit(PausableBullet b) {
        double angle1 = angle(b);
        double angle2 = b.angle(this);

        if (angle1 >= PI) {
            angle1 = 2 * PI - angle1;
        }
        while (angle1 >= PI / 2) {
            angle1 = angle1 - PI / 2;
        }

        if (angle2 >= PI) {
            angle2 = 2 * PI - angle2;
        }
        while (angle2 >= PI / 2) {
            angle2 = angle2 - PI / 2;
        }

        double a = min(angle1, angle2);

        double xw = ((x - b.x) / 20) * norm * cos(a);
        double yw = ((y - b.y) / 20) * norm * cos(a);

        synchronized (this) {
            dx = dx + xw;
            dy = dy + yw;

            double length = sqrt(dx * dx + dy * dy);
            dx = (dx / length) * norm;
            dy = (dy / length) * norm;
        }
    }

    private double angle(PausableBullet b) {
        double phi1 = atan2(dy, dx);
        double phi2 = atan2(b.y - y, b.x - x);
        return abs(phi1 - phi2);
    }

    public double distance(PausableBullet b) {
        return Point.distance(x, y, b.x, b.y);
    }

    @Override
    public void run() {
        synchronized (this) {
            screen.drawBullet(x, y, dx, dy, radius, color);

            x += dx;
            y += dy;
        }

        collisionCheck();

        if ((x - radius <= 0 && dx < 0) || (x + radius >= screen.getWidth() && dx > 0)) {
            dx = -dx;
        }
        if ((y - radius <= 0 && dy < 0) || (y + radius >= screen.getHeight() && dy > 0)) {
            dy = -dy;
        }
    }
}

class PausableScreen extends JPanel {

    private VolatileImage offscreenImage, savedImage;
    private Graphics2D offscreenGraphics, savedGraphics;
    private final Ellipse2D.Double bulletEllipse;

    public PausableScreen() {
        bulletEllipse = new Ellipse2D.Double();
    }

    public void updateGraphics() {
        offscreenImage = createVolatileImage(getWidth() + 10, getHeight() + 10);
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        offscreenGraphics.drawImage(savedImage, 0, 0, this);
    }

    public void saveGraphics() {
        savedImage = createVolatileImage(getWidth(), getHeight());
        savedGraphics = (Graphics2D) savedImage.getGraphics();
        savedGraphics.drawImage(offscreenImage, 0, 0, this);
    }

    public void setGraphics() {
        offscreenImage = createVolatileImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public synchronized void drawBullet(double x, double y, double dx, double dy, int radius, Color color) {
        offscreenGraphics.setColor(Color.WHITE);
        bulletEllipse.setFrame(x - (radius + 1), y - (radius + 1), 2 * (radius + 1), 2 * (radius + 1));
        offscreenGraphics.fill(bulletEllipse);

        offscreenGraphics.setColor(color);
        bulletEllipse.setFrame(x + dx - radius, y + dy - radius, 2 * radius, 2 * radius);
        offscreenGraphics.fill(bulletEllipse);

        repaint();
    }

    @Override
    public synchronized void paint(Graphics g) {
        g.drawImage(offscreenImage, 0, 0, this);
    }
}
