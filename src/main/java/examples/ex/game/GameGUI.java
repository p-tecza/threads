/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.game;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Dominik Olszewski
 */
public final class GameGUI implements ActionListener, ChangeListener {
    
    private final GameEngine gameEngine;
    private final JFrame gameFrame;
    private final JPanel mainPanel;
    private final GamePanel gamePanel;
    private final JButton startButton;
    private final JButton moveBonusButton;
    private final JButton soundEffectOnButton;
    private final JButton musicOnButton;
    private final JLabel infoLabel;
    private final JLabel timeLabel;
    private final JLabel scoreLabel;
    private final JSlider speedSlider;
    private final JSlider turningSpeedSlider;
    private final short side;
    
    public GameGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        gameFrame = new JFrame("Game");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setBackground(Color.WHITE);
        side = 600;
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.setMinimumSize(new Dimension(side + 260, side + 50));
        
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        gamePanel = new GamePanel();
        
        startButton = new JButton("Start game");
        moveBonusButton = new JButton("Still bonus");
        soundEffectOnButton = new JButton("Sound effects off");
        musicOnButton = new JButton("Music off");
        infoLabel = new JLabel("<html><body><center>Click on the white area to start the game<br>"
                + "LEFT ARROW -- turn left<br>"
                + "RIGHT ARROW -- turn right<br>"
                + "SPACE -- start or pause the game<br>"
                + "ESCAPE -- end the current game</center></body></html>");
        timeLabel = new JLabel("00:00:00");
        scoreLabel = new JLabel("Score: 0");
        speedSlider = new JSlider(2, 6, 4);
        turningSpeedSlider = new JSlider(2, 6, 3);
        gameEngine = new GameEngine(gameFrame, gamePanel, startButton, scoreLabel, timeLabel);
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridheight = 9;
        c.gridx = 0;
        c.gridy = 0;

        mainPanel.add(gamePanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.;
        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 1;
        c.gridy = 0;

        mainPanel.add(startButton, c);
        
        c.gridx = 1;
        c.gridy = 1;
        
        mainPanel.add(moveBonusButton, c);
        
        c.gridx = 1;
        c.gridy = 2;
        
        mainPanel.add(soundEffectOnButton, c);
        
        c.gridx = 1;
        c.gridy = 3;
        
        mainPanel.add(musicOnButton, c);
        
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 4;
        
        mainPanel.add(infoLabel, c);

        c.gridx = 1;
        c.gridy = 5;

        mainPanel.add(timeLabel, c);

        c.gridx = 1;
        c.gridy = 6;

        mainPanel.add(scoreLabel, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 7;

        mainPanel.add(speedSlider, c);

        c.gridx = 1;
        c.gridy = 8;

        mainPanel.add(turningSpeedSlider, c);
        
        gameFrame.add(mainPanel);
        
        gameFrame.addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentResized(ComponentEvent event) {
                gamePanel.updateGraphics();
            }
        });
        gameFrame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowIconified(WindowEvent event) {
                gameEngine.gamePause();
            }
            
            @Override
            public void windowDeiconified(WindowEvent event) {
                gameEngine.gameResume();
            }
        });
        
        timeLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        
        speedSlider.setMajorTickSpacing(2);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        speedSlider.setBorder(BorderFactory.createTitledBorder(null, "Speed",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

        turningSpeedSlider.setMajorTickSpacing(2);
        turningSpeedSlider.setMinorTickSpacing(1);
        turningSpeedSlider.setPaintTicks(true);
        turningSpeedSlider.setPaintLabels(true);

        turningSpeedSlider.setBorder(BorderFactory.createTitledBorder(null, "Turning Speed",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
        
        gameFrame.pack();
        gameFrame.setVisible(true);
    }
    
    private void addEventListeners() {
        startButton.addActionListener(this);
        moveBonusButton.addActionListener(this);
        soundEffectOnButton.addActionListener(this);
        musicOnButton.addActionListener(this);
        speedSlider.addChangeListener(this);
        turningSpeedSlider.addChangeListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(startButton)) {
            gameEngine.gameStart();
        } else if (event.getSource().equals(moveBonusButton) && Bonus.isMoving) {
            if (!gameEngine.isGameStopped()) gameEngine.stopBonus();
            Bonus.isMoving = false;
            moveBonusButton.setText("Moving square");
            gamePanel.requestFocus();
        } else if (event.getSource().equals(moveBonusButton)) {
            Bonus.isMoving = true;
            if (!gameEngine.isGameStopped()) gameEngine.startBonus();
            moveBonusButton.setText("Still square");
            gamePanel.requestFocus();
        } else if (event.getSource().equals(soundEffectOnButton) && gameEngine.isSoundEffectOn()) {
            gameEngine.setSoundEffectOn(false);
            soundEffectOnButton.setText("Sound effects on");
            gamePanel.requestFocus();
        } else if (event.getSource().equals(soundEffectOnButton) && !gameEngine.isSoundEffectOn()) {
            gameEngine.setSoundEffectOn(true);
            soundEffectOnButton.setText("Sound effects off");
            gamePanel.requestFocus();
        } else if (gameEngine.isMusicOn()) {
            gameEngine.setMusicOn(false);
            musicOnButton.setText("Music on");
            gamePanel.requestFocus();
        } else {
            gameEngine.setMusicOn(true);
            musicOnButton.setText("Music off");
            gamePanel.requestFocus();
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource().equals(speedSlider)) {
            gameEngine.setSpeed(speedSlider.getValue());
            gamePanel.requestFocus();
        } else {
            gameEngine.setAngleInc(turningSpeedSlider.getValue() / 100.);
            gamePanel.requestFocus();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI().addEventListeners());
    }
}
