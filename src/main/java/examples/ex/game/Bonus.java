/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import static java.lang.Math.*;

/**
 *
 * @author Dominik Olszewski
 */
public final class Bonus extends Rectangle2D.Double {
    
    private final GamePanel gamePanel;
    private Timer animationTimer;
    private Color color;
    private double xInc, yInc;
    public static boolean isMoving;
    
    static {
        isMoving = true;
    }
    
    private class AnimationTimerTask extends TimerTask {

        @Override
        public void run() {
            Shape bonusToClear = toClear();
            
            x += xInc;
            y += yInc;
            
            gamePanel.clearAndFill(bonusToClear, Bonus.this, color);
            
            if (x < 0 || x + 20 > gamePanel.getWidth()) {
                xInc = -xInc;
            }
            if (y < 0 || y + 20 > gamePanel.getHeight()) {
                yInc = -yInc;
            }
        }
    }
    
    public Bonus(GamePanel gamePanel) {
        super(0., 0., 20., 20.);
        
        this.gamePanel = gamePanel;
        
        double angle = random() * 2 * PI;
        xInc = cos(angle);
        yInc = sin(angle);
    }
    
    public Point getCenter() {
        return new Point((int) x + 10, (int) y + 10);
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public Shape toClear() {
        return new Rectangle2D.Double(x - 1, y - 1, 22, 22);
    }
    
    public void start() {
        if (isMoving) {
            animationTimer = new Timer();
            animationTimer.scheduleAtFixedRate(new AnimationTimerTask(), 0, 10);
        }
    }
    
    public void stop() {
        if (isMoving) animationTimer.cancel();
    }
}
