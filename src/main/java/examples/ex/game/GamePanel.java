/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.game;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Dominik Olszewski
 */
public final class GamePanel extends JPanel {
    
    private Image offscreenImage, tmpImage;
    private Graphics2D offscreenGraphics, tmpGraphics;
    
    public void updateGraphics() {
        offscreenImage = createImage(getWidth() + 10, getHeight() + 10);
        offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        offscreenGraphics.drawImage(tmpImage, 0, 0, this);
    }
    
    public void saveGraphics() {
        tmpImage = createImage(getWidth(), getHeight());
        tmpGraphics = (Graphics2D) tmpImage.getGraphics();
        tmpGraphics.drawImage(offscreenImage, 0, 0, this);
    }
    
    public synchronized void fill(Shape shape, Color color) {
        offscreenGraphics.setColor(color);
        offscreenGraphics.fill(shape);
        repaint();
    }
    
    public synchronized void fillOval(int x, int y, Color color) {
        offscreenGraphics.setColor(color);
        offscreenGraphics.fillOval(x - 10, y - 10, 20, 20);
        repaint();
    }
    
    public synchronized void clearAndFill(Shape shapeToClear, Shape shapeToFill, Color color) {
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fill(shapeToClear);
        
        offscreenGraphics.setColor(color);
        offscreenGraphics.fill(shapeToFill);
        repaint();
    }
    
    public synchronized void clear(Shape shape) {
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fill(shape);
    }
    
    public synchronized void clearOval(int x, int y) {
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fillOval(x - 11, y - 11, 22, 22);
    }
    
    public synchronized void clearAll() {
        offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
        repaint();
    }
    
    @Override
    public synchronized void paint(Graphics g) {
        g.drawImage(offscreenImage, 0, 0, this);
    }
}
