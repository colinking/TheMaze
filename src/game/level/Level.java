package game.level;

import game.entities.Entity;
import game.entities.PlayerMP;
import game.gfx.Screen;
import game.level.tiles.Tile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Level {

    private byte[] tiles;
    private int width;
    private int height;
    private String imagePath;
    private BufferedImage image;
    private ArrayList<Entity> entities = new ArrayList<>();

    public Level(String imagePath) {
        if(imagePath != null) {
            this.imagePath = imagePath;
            this.loadImageFromFile();
        } else {
            this.width = 64;
            this.height = 64;
            tiles = new byte[width * height];
            this.generateLevel();
        }
    }
    
    private void loadImageFromFile() {
        try {
            this.image = ImageIO.read(new File(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        } catch(IOException i) {
            System.err.println("Error: " + i.getMessage());
        }
    }
    
    private void loadTiles() {
        int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                tileCheck: for(Tile t: Tile.tiles) {
                    if(t != null && t.getLevelColor() == tileColors[x + y * width]) {
                        this.tiles[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }
    
    private void saveLevelToFile() {
        try {
            ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getPath()));
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    public void alterTiles(int x, int y, Tile newTile) {
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColor());
    }
    
    public void generateLevel() {
        Random rand = new Random();
        //generate number of spawns for number of teams?
        //recursion!
        //
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int ran = rand.nextInt(3);
//                if(ran == 0) {
//                    tiles[x + y * width] = Tile.GRASS.getId();
//                }
//                else if(ran == 1){
//                    tiles[x + y * width] = Tile.STONE.getId();
//                }
//                else {
//                    tiles[x + y * width] = Tile.WATER.getId();
//                }
//            }
//        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0) {
            xOffset = 0;
        }
        if (xOffset > (width << 3) - screen.width) {
            xOffset = (width << 3) - screen.width;
        }
        if (yOffset < 0) {
            yOffset = 0;
        }
        if (yOffset > (height << 3) - screen.height) {
            yOffset = (height << 3) - screen.height;
        }
        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y <= (yOffset + screen.height) >> 3; y++) {
            for (int x = (xOffset >> 3); x <= (xOffset + screen.width) >> 3; x++) {
                getTile(x, y).render(screen, this, x << 3, y << 3);
            }
        }
    }
    
    public synchronized ArrayList<Entity> getEntities() {
        return entities;
    }
    
    public void tick() {
        for(Entity e : getEntities()) {
            e.tick();
        }
        
        for(Tile t: Tile.tiles) {
            if(t == null) {
                break;
            } else {
                t.tick();
            }
        }
    }
    
    public void renderEntities(Screen screen) {
        for(Entity e : getEntities()) {
            e.render(screen);
        }
    }
    
    public void addEntity(Entity entity) {
        this.getEntities().add(entity);
    }
    
    public void removePlayerMP(String username) {
        int index = 0;
        for(Entity e: this.getEntities()) {
            if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equalsIgnoreCase(username)) {
                break;
            }
            index++;
        }
        this.getEntities().remove(index);
    }
    
    public Tile getTile(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.VOID;
        }
        else {
            return Tile.tiles[tiles[x + y * width]];
        }
    }
    
    private int getPlayerMPIndex(String username) {
        int index = 0;
        for(Entity e: getEntities()) {
            if(e instanceof PlayerMP && ((PlayerMP) e).getUsername().equalsIgnoreCase(username)) {
                break;
            }
            index++;
        }
        return index;
    }
    
    public void movePlayer(String username, int x , int y, int numSteps, boolean isMoving, int movingDir) {
        int index = getPlayerMPIndex(username);
        PlayerMP player = (PlayerMP)this.getEntities().get(index);
        player.x = x;
        player.y = y;
        player.setNumSteps(numSteps);
        player.setIsMoving(isMoving);
        player.setMovingDir(movingDir);
    }
}
