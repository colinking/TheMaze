package game.level.tiles;

import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;


public abstract class Tile {
    
    public static final Tile[] tiles = new Tile[256];
    
    public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, -1, -1), 0xFF555555);
    public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00);
    public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colors.get(000, -1, -1, -1), 0xFF000000);
    public static final Tile WATER = new AnimatedTile(3, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 1000);
    private int levelColor;
    
    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    
    public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) {
        this.id = (byte) id;
        this.levelColor = levelColor;
        if(tiles[id] != null) {
            throw new RuntimeException("Tile ID already occupied at ID: " + id);
        }
        this.solid = isSolid;
        this.emitter = isEmitter;
        tiles[id] = this;
    }
    
    public byte getId() {
        return id;
    }
    public boolean getSolid() {
        return solid;
    }
    public boolean getEmitter() {
        return emitter;
    }
    public int getLevelColor() {
        return levelColor;
    }
    
    public abstract void tick();
    
    public abstract void render(Screen screen, Level level, int x, int y);
}
