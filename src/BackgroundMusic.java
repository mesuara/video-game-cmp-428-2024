import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusic {
    Clip clip;

    public void playMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY); // This will allow the audio to loop continuously.
            } else {
                System.out.println("Can't find file");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    
    public void playSoundEffect(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Play the sound once
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }
    
    public void stopMusic() {
        if (clip != null) {
            clip.stop(); 
        }
    }
}
