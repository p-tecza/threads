/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import static java.lang.Math.*;

/**
 *
 * @author Dominik Olszewski
 */
public final class GameEngine {

    private final java.util.List<Point> ovalList;
    private final JFrame gameFrame;
    private final GamePanel gamePanel;
    private final JButton startButton;
    private final JLabel timeLabel;
    private final JLabel scoreLabel;
    private final DecimalFormat format;
    private Bonus bonus;
    private java.util.Timer animationTimer;
    private java.util.Timer turningTimer;
    private java.util.Timer clockTimer;
    private final SoundPlayer musicPlayer;
    private Color ovalColor, bonusColor, tmpColor;
    private double xCenter, yCenter, angle, angleInc;
    private long seconds;
    private int length, speed, score;
    private boolean stopFlag, gameOverFlag, keyEventProcessed, turnDirection, soundEffectOn, musicOn;
    
    private class AnimationTimerTask extends TimerTask {
        
        @Override
        public void run() {
            Point tail = new Point((int) xCenter, (int) yCenter);
            
            gamePanel.fillOval(tail.x, tail.y, ovalColor);
            
            ovalList.add(tail);

            //Bonus
            if (tail.distance(bonus.getCenter()) <= 25) {
                getBonus();
            } else if (ovalList.stream().anyMatch(p -> p.distance(bonus.getCenter()) <= 23)) {
                gameOver();
            }
            
            //Move
            xCenter += cos(angle);
            yCenter += sin(angle);

            //Full length
            if (ovalList.size() >= length) {
                Point head = ovalList.remove(0);
                gamePanel.clearOval(head.x, head.y);
            }
            
            //Self-collision
            if (ovalList.size() > 30 && ovalList.subList(0, ovalList.size() - 30)
                    .stream().anyMatch(p -> p.distance(tail) <= 20))
                gameOver();

            //Boundary collision
            if (xCenter - 10. <= 0 || xCenter + 10. >= gamePanel.getWidth()
                    || yCenter - 10. <= 0 || yCenter + 10. >= gamePanel.getHeight())
                gameOver();
        }
    }
    
    private class TurningTimerTask extends TimerTask {

        @Override
        public void run() {
            if (turnDirection) angle += angleInc;
            else angle -= angleInc;
        }
    }
    
    private class ClockTimerTask extends TimerTask {

        @Override
        public void run() {
            timeLabel.setText(format.format(++seconds / 3600) + ":" +
                              format.format(seconds / 60) + ":" +
                              format.format(seconds % 60));
        }
    }
    
    public GameEngine(JFrame gameFrame, GamePanel gamePanel, JButton startButton,
            JLabel scoreLabel, JLabel timeLabel) {
        ovalList = new ArrayList<>();
        this.gameFrame = gameFrame;
        this.gamePanel = gamePanel;
        this.startButton = startButton;
        this.scoreLabel = scoreLabel;
        this.timeLabel = timeLabel;
        format = (DecimalFormat) DecimalFormat.getInstance();
        format.applyPattern("00");
        angleInc = 0.03;
        speed = 4;
        seconds = 0;
        stopFlag = true;
        gameOverFlag = true;
        soundEffectOn = true;
        musicOn = true;
        
        musicPlayer = new SoundPlayer("/audio/Etude.wav");
        musicPlayer.loop();
        
        this.gamePanel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_LEFT && !keyEventProcessed && !stopFlag) {
                    keyEventProcessed = true;
                    turnDirection = false;
                    turningTimer = new java.util.Timer();
                    turningTimer.scheduleAtFixedRate(new TurningTimerTask(), 0, 10);
                } else if (event.getKeyCode() == KeyEvent.VK_RIGHT && !keyEventProcessed && !stopFlag) {
                    keyEventProcessed = true;
                    turnDirection = true;
                    turningTimer = new java.util.Timer();
                    turningTimer.scheduleAtFixedRate(new TurningTimerTask(), 0, 10);
                } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameOverFlag) {
                        gameStart();
                    } else {
                        keyEventProcessed = false;
                        if (stopFlag) {
                            stopFlag = false;
                            gameResume();
                        } else {
                            gamePause();
                            stopFlag = true;
                        }
                    }
                } else if (event.getKeyCode() == KeyEvent.VK_ESCAPE && !gameOverFlag) {
                    gameOver();
                }
            }

            @Override
            public void keyReleased(KeyEvent event) {
                if (((event.getKeyCode() == KeyEvent.VK_LEFT && !turnDirection)
                        || (event.getKeyCode() == KeyEvent.VK_RIGHT && turnDirection)) && !stopFlag) {
                    keyEventProcessed = false;
                    turningTimer.cancel();
                }
            }
        });

        this.gamePanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (gameOverFlag) {
                    gameStart();
                }
            }
        });
    }
    
    public void setAngleInc(double angleInc) {
        this.angleInc = angleInc;
    }
    
    public void setSpeed(int speed) {
        if (stopFlag) {
            this.speed = speed;
        } else {
            this.speed = speed;
            animationTimer.cancel();
            animationTimer = new java.util.Timer();
            animationTimer.scheduleAtFixedRate(new AnimationTimerTask(), 0, 12 - speed);
        }
    }
    
    public boolean isGameStopped() {
        return stopFlag;
    }
    
    public boolean isSoundEffectOn() {
        return soundEffectOn;
    }
    
    public boolean isMusicOn() {
        return musicOn;
    }
    
    public void setSoundEffectOn(boolean soundEffectOn) {
        this.soundEffectOn = soundEffectOn;
    }
    
    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
        if (musicOn) musicPlayer.loop();
        else musicPlayer.stop();
    }
    
    public void startBonus() {
        bonus.start();
    }
    
    public void stopBonus() {
        bonus.stop();
    }

    private boolean isValidLocation(int x, int y) {
        return ovalList.stream().noneMatch(p -> p.distance(x, y) <= 40);
    }

    private Point findLocation() {
        int x, y;
        do {
            x = (int) (random() * (gamePanel.getWidth() - 20.));
            y = (int) (random() * (gamePanel.getHeight() - 20.));
        } while (!isValidLocation(x, y));
        return new Point(x, y);
    }

    private void getBonus() {
        bonus.stop();
        if (soundEffectOn) new SoundPlayer("/audio/EatingSound.wav").play();
        ovalColor = tmpColor;
        bonusColor = new Color((int) (random() * 0xFFFFFF));
        tmpColor = bonusColor;
        gamePanel.clear(bonus.toClear());
        bonus = new Bonus(gamePanel);
        bonus.setFrame(findLocation(), new Dimension(20, 20));
        bonus.setColor(bonusColor);
        gamePanel.fill(bonus, bonusColor);
        bonus.start();
        length += 50;
        score++;
        scoreLabel.setText("Score: " + score);
    }
    
    public void gameStart() {
        gameFrame.setResizable(false);
        startButton.setEnabled(false);
        startButton.setText("Game running...");
        gamePanel.requestFocus();
        ovalColor = Color.BLACK;
        bonusColor = new Color((int) (random() * 0xFFFFFF));
        tmpColor = bonusColor;
        xCenter = random() * (gamePanel.getWidth() - 400.) + 200.;
        yCenter = random() * (gamePanel.getHeight() - 400.) + 200.;
        angle = random() * 2. * PI;
        length = 200;
        score = 0;
        seconds = 0;
        stopFlag = false;
        gameOverFlag = false;
        keyEventProcessed = false;
        timeLabel.setText("00:00:00");
        scoreLabel.setText("Score: " + score);
        bonus = new Bonus(gamePanel);
        bonus.setFrame(findLocation(), new Dimension(20, 20));
        bonus.setColor(bonusColor);
        gamePanel.clearAll();
        gamePanel.fill(bonus, bonusColor);
        bonus.start();
        animationTimer = new java.util.Timer();
        turningTimer = new java.util.Timer();
        clockTimer = new java.util.Timer();
        animationTimer.scheduleAtFixedRate(new AnimationTimerTask(), 0, 12 - speed);
        clockTimer.schedule(new ClockTimerTask(), 1000, 1000);
    }
    
    public void gamePause() {
        if (!stopFlag) {
            turningTimer.cancel();
            animationTimer.cancel();
            bonus.stop();
            clockTimer.cancel();
            startButton.setText("Game paused");
            gamePanel.saveGraphics();
        }
    }
    
    public void gameResume() {
        if (!stopFlag) {
            startButton.setText("Game running...");
            bonus.start();
            animationTimer = new java.util.Timer();
            clockTimer = new java.util.Timer();
            animationTimer.scheduleAtFixedRate(new AnimationTimerTask(), 0, 12 - speed);
            clockTimer.schedule(new ClockTimerTask(), 1000, 1000);
        }
    }

    private void gameOver() {
        turningTimer.cancel();
        animationTimer.cancel();
        bonus.stop();
        clockTimer.cancel();
        gameFrame.setResizable(true);
        startButton.setText("Start game");
        startButton.setEnabled(true);
        if (soundEffectOn) new SoundPlayer("/audio/GameOverSound.wav").play();
        stopFlag = true;
        gameOverFlag = true;
        keyEventProcessed = false;
        ovalList.clear();
        gamePanel.saveGraphics();
        JOptionPane.showMessageDialog(gamePanel, "Click OK and press the Start game"
                + " button, space or click on the white area to play again.",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
