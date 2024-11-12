/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.game;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.text.DecimalFormat;
import java.util.*;
import static java.lang.Math.*;

/**
 *
 * @author Dominik Olszewski
 */
public class GameApplet extends Applet implements Runnable {

    private java.util.List<Point> ovalList;
    private Thread gameThread;
    private TurnThread turnThread;
    private Image offscreenImage;
    private Graphics2D offscreenGraphics;
    private Rectangle bonus;
    private AudioClip eatingClip;
    private Color ovalColor, bonusColor, tmpColor;
    private LocalTime time;
    private DecimalFormat format;
    private double xCenter, yCenter, angle;
    private int length, score;
    private boolean stopFlag, gameOverFlag, keyEventProcessed, turnDirection;

    private class TurnThread extends Thread {

        private final double angleInc;

        private TurnThread(double angleInc) {
            this.angleInc = angleInc;
        }

        @Override
        public void run() {
            while (!stopFlag) {
                angle += angleInc;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    return;
                }
            }
        }
    }

    @Override
    public void init() {
        resize(800, 800);
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        ovalList = new ArrayList<>();
        bonus = new Rectangle(findLocation(), new Dimension(20, 20));
        eatingClip = getAudioClip(getCodeBase(), "EatingSound.wav");

        setFocusable(true);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_LEFT && !keyEventProcessed && !stopFlag) {
                    keyEventProcessed = true;
                    turnDirection = false;
                    turnThread = new TurnThread(-0.2);
                    turnThread.start();
                } else if (event.getKeyCode() == KeyEvent.VK_RIGHT && !keyEventProcessed && !stopFlag) {
                    keyEventProcessed = true;
                    turnDirection = true;
                    turnThread = new TurnThread(0.2);
                    turnThread.start();
                } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameOverFlag) {
                        gameStart();
                    } else {
                        keyEventProcessed = false;
                        if (stopFlag) {
                            stopFlag = false;
                            gameThread = new Thread(GameApplet.this);
                            gameThread.start();
                        } else {
                            stopFlag = true;
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent event) {
                if (((event.getKeyCode() == KeyEvent.VK_LEFT && !turnDirection)
                        || (event.getKeyCode() == KeyEvent.VK_RIGHT && turnDirection)) && !stopFlag) {
                    keyEventProcessed = false;
                    turnThread.interrupt();
                }
            }
        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (gameOverFlag) {
                    gameStart();
                }
            }
        });
    }

    @Override
    public void start() {
        gameStart();
    }

    private boolean isValidLocation(int x, int y) {
        for (Point p : ovalList) {
            if (distance(p, new Point(x, y)) <= 40) {
                return false;
            }
        }
        return true;
    }

    private Point findLocation() {
        int x, y;
        do {
            x = (int) (random() * (getWidth() - 20));
            y = (int) (random() * (getHeight() - 20));
        } while (!isValidLocation(x, y));
        return new Point(x, y);
    }

    private void getBonus() {
        eatingClip.play();
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fill(bonus);
        ovalColor = tmpColor;
        bonusColor = new Color((int) (random() * Integer.decode("0xFFFFFF")));
        tmpColor = bonusColor;
        bonus.setLocation(findLocation());
        offscreenGraphics.setColor(bonusColor);
        offscreenGraphics.fill(bonus);
        offscreenGraphics.setColor(ovalColor);
        length += 30;
        score++;
    }

    private double distance(Point p1, Point p2) {
        return sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2));
    }

    private void gameStart() {
        offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
        ovalColor = Color.BLACK;
        bonusColor = new Color((int) (random() * Integer.decode("0xFFFFFF")));
        tmpColor = bonusColor;
        format = (DecimalFormat) DecimalFormat.getInstance();
        format.applyPattern("00");
        xCenter = random() * (getWidth() - 400) + 200;
        yCenter = random() * (getHeight() - 400) + 200;
        angle = random() * 2 * PI;
        length = 100;
        score = 0;
        stopFlag = false;
        gameOverFlag = false;
        keyEventProcessed = false;
        bonus.setLocation(findLocation());
        offscreenGraphics.setColor(bonusColor);
        offscreenGraphics.fill(bonus);
        offscreenGraphics.setColor(ovalColor);
        gameThread = new Thread(GameApplet.this);
        gameThread.start();
        time = LocalTime.now();
    }

    private void gameOver() {
        Duration gameDuration = Duration.between(time, LocalTime.now());
        Toolkit.getDefaultToolkit().beep();
        stopFlag = true;
        gameOverFlag = true;
        keyEventProcessed = false;
        ovalList.clear();
        offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
        offscreenGraphics.setColor(Color.BLACK);
        offscreenGraphics.setFont(new Font("Arial", Font.PLAIN, 50));
        offscreenGraphics.drawString("Game Over", getWidth() / 2 - 130, getHeight() / 2 - 70);
        offscreenGraphics.setFont(new Font("Arial", Font.PLAIN, 30));
        offscreenGraphics.drawString("Time: " +
                                     format.format(gameDuration.toHours()) + ":" +
                                     format.format(gameDuration.toMinutes() % 60) + ":" +
                                     format.format(gameDuration.getSeconds() % 60),
                                     getWidth() / 2 - 92,
                                     getHeight() / 2 - 27);
        offscreenGraphics.drawString("Score: " + score, getWidth() / 2 - 47, getHeight() / 2 + 16);
        offscreenGraphics.drawString("Press space or click to play again",
                getWidth() / 2 - 210, getHeight() / 2 + 58);
    }

    @Override
    public void run() {
        while (!stopFlag) {
            offscreenGraphics.fillOval((int) xCenter - 10, (int) yCenter - 10, 20, 20);

            Point tail = new Point((int) xCenter, (int) yCenter);

            ovalList.add(tail);

            //Bonus
            if (distance(tail, bonus.getLocation()) <= 30) {
                getBonus();
            }

            //Move
            xCenter += 4. * cos(angle);
            yCenter += 4. * sin(angle);

            try {
                Thread.sleep(30);
            } catch (InterruptedException ie) {
            }

            //Full length
            if (ovalList.size() >= length) {
                Point head = ovalList.remove(0);
                offscreenGraphics.setColor(Color.WHITE);
                offscreenGraphics.fillOval(head.x - 10, head.y - 10, 20, 20);
                offscreenGraphics.setColor(ovalColor);
            }

            //Self-collision
            if (ovalList.size() > 50) {
                for (Point p : ovalList.subList(0, ovalList.size() - 50)) {
                    if (distance(tail, p) <= 20) {
                        gameOver();
                        break;
                    }
                }
            }

            //Boundary collision
            if (xCenter - 10 <= 0 || xCenter + 10 >= getWidth()
                    || yCenter - 10 <= 0 || yCenter + 10 >= getHeight()) {
                gameOver();
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
