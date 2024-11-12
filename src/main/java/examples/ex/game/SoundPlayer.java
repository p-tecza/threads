/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.ex.game;

import javax.sound.sampled.*;
import java.io.*;

/**
 *
 * @author Dominik Olszewski
 */
public final class SoundPlayer {
    
    private AudioInputStream audioInputStream;
    private Clip audioClip;
    private final String filePath;
    
    public SoundPlayer(String filePath) {
        this.filePath = filePath;
    }
    
    public void play() {
        new Thread(() -> {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
                audioClip = AudioSystem.getClip();
                audioClip.open(audioInputStream);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
            audioClip.start();
        }).start();
    }
    
    public void loop() {
        new Thread(() -> {
            try {
                audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
                audioClip = AudioSystem.getClip();
                audioClip.open(audioInputStream);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        }).start();
    }
    
    public void stop() {
        audioClip.stop();
    }
}
