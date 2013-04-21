package game.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SpriteSheet {
    public int width;
    public int height;
    public String path;
    
    public int[] pixels;
    
    public SpriteSheet(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {}
        
        this.path = path;
        this.width = image.getWidth();
        this.height = image.getHeight();
        
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
        
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = (pixels[i] & 0xff) / 64;
        }
    }
}
