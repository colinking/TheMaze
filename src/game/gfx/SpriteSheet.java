package game.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;


public class SpriteSheet {
    public int width;
    public int height;
    public String path;
    
    public int[] pixels;
    
    public SpriteSheet(String path) {
        BufferedImage image = null;
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        
        try {
            String fullpath = System.getProperty("user.dir") + "/" + path;
            System.out.println(fullpath);
            image = ImageIO.read(new File(fullpath));
            this.width = image.getWidth();
        this.height = image.getHeight();
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        
        this.path = path;
        
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
        
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = (pixels[i] & 0xff) / 64;
        }
//        for(int v:pixels)
//            System.out.print(v + " ");
    }
}
