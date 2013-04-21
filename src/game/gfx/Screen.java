package game.gfx;

public class Screen {

    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
    public static final byte BIT_MIRROR_X = 0x01;
    public static final byte BIT_MIRROR_Y = 0x02;
    public int xOffset = 0;
    public int yOffset = 0;
    public int width;
    public int height;
    public SpriteSheet spriteSheet;
    public int[] pixels;

    public Screen(int width, int height, SpriteSheet spriteSheet) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;

        pixels = new int[width * height];
    }

    public void render(int xPos, int yPos, int tile, int color) {
        render(xPos, yPos, tile, color, 0x00);
    }

    public void render(int xPos, int yPos, int tile, int color, int mirrorDir) {
        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
        
        int xTile = tile % 32;
        int yTile = tile / 32;
        int tileOffset = (xTile << 3) + (yTile << 3) * spriteSheet.width;
        for (int y = 0; y < 8; y++) {
            if (y + yPos < 0 || y + yPos >= height) {
                continue;
            }
            int ySheet = y;
            if (mirrorY) {
                ySheet = 8 - y;
            }
            for (int x = 0; x < 8; x++) {
                if (x + xPos < 0 || x + xPos >= width) {
                    continue;
                }
                int xSheet = x;
                if (mirrorX) {
                    xSheet = 8 - x;
                }
                int col = (color >> (spriteSheet.pixels[xSheet + ySheet * spriteSheet.width + tileOffset] * 8)) & 255;
                if (col < 255) {
                    pixels[(x + xPos) + (y + yPos) * width] = col;
                }
            }
        }
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
