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

    public void render(int xPos, int yPos, int tile, int color, int mirrorDir, int scale) {
        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
        
        int scaleMap = scale - 1;
        //tile 34
        int xTile = tile % 32;
        int yTile = tile / 32;
        int tileOffset = (xTile << 3) + (yTile << 3) * spriteSheet.width; //index in 1d pixels array
        for (int y = 0; y < 8; y++) {
            
            int ySheet = y; //y coord on sprite sheet
            if (mirrorY) {
                ySheet = 7 - y;
            }
            int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2); //y pixel on screen
            for (int x = 0; x < 8; x++) {
                
                int xSheet = x; //x coord on sprite sheet
                if (mirrorX) {
                    xSheet = 7 - x;
                }
                int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2); //x pixel on screen
                //ss.pixels[] -> 0-3 color of pixel on spritesheet
                //then shifted above # * 8 to get whichever color
                //make sure it is below 255 (within 8 bits)
                int col = (color >> (spriteSheet.pixels[xSheet + ySheet * spriteSheet.width + tileOffset] * 8)) & 255;
                if (col < 255) {
                    for(int yScale = 0; yScale < scale; yScale++) {
                        if (yPixel + yScale < 0 || yPixel + yScale >= height) {
                            continue;
                        }
                        for(int xScale = 0; xScale < scale; xScale++) {
                            if (xPixel + xScale < 0 || xPixel + xScale >= width) {
                                continue;
                            }
                            pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
                        }
                    }
                }
            }
        }
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
