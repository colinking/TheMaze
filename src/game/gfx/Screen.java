package game.gfx;

public class Screen {

    public static final byte BIT_MIRROR_X = 0x01;
    public static final byte BIT_MIRROR_Y = 0x02;
    public int xOffset = 0;
    public int yOffset = 0;
    public int width;
    public int height;
    public SpriteSheet spriteSheet;
    public int[] pixels;
    public int[] darkPixels;
    private int mapWidth;
    private int mapHeight;

    public Screen(int width, int height, SpriteSheet spriteSheet, int mapWidth, int mapHeight) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        pixels = new int[width * height];
        darkPixels = new int[mapWidth * mapHeight];
    }

    public void render(int xPos, int yPos, int tile, int color, int mirrorDir, int scale) {
        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

        int scaleMap = scale - 1;
        int xTile = tile % 32;
        int yTile = tile / 32;
        int tileOffset = (xTile << 3) + (yTile << 3) * spriteSheet.width;
        for (int y = 0; y < 8; y++) {

            int ySheet = y;
            if (mirrorY) {
                ySheet = 7 - y;
            }
            int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);
            for (int x = 0; x < 8; x++) {

                int xSheet = x;
                if (mirrorX) {
                    xSheet = 7 - x;
                }
                int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
                int col = (color >> (spriteSheet.pixels[xSheet + ySheet * spriteSheet.width + tileOffset] * 8)) & 255;
                if (col < 255) {
                    for (int yScale = 0; yScale < scale; yScale++) {
                        if (yPixel + yScale < 0 || yPixel + yScale >= height) {
                            continue;
                        }
                        for (int xScale = 0; xScale < scale; xScale++) {
                            if (xPixel + xScale < 0 || xPixel + xScale >= width) {
                                continue;
                            }
//                            System.out.println("X: " + (xPos + xOffset + xScale) + " Y: " + (yPos + yOffset + yScale));
//                            if (darkPixels[(xPixel + xOffset + xScale) + (yPixel + yOffset + yScale) * mapWidth] != 0) {
                            if(true){
                                pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
                            } else {
                                pixels[(xPixel + xScale) + (yPixel + yScale) * width] = 0;
                            }

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

    public void updateDarkPixels(int xUpdate, int yUpdate, int radius) {
//        xUpdate -= 20;
//        int x, y;
//        int maxLength = 24;
////        int side = (radius - 2) / 4;
//        for (y = yUpdate - maxLength; y < yUpdate; y++) {
//            for (x = xUpdate - (y - yUpdate); x < xUpdate + maxLength; x++) {
//                darkPixels[x + y * mapWidth] = 1;
//            }
//            maxLength++;
//        }
//        for (y = yUpdate - 1; y < yUpdate + maxLength; y++) {
//            maxLength--;
//            for (x = xUpdate + (y - yUpdate); x < xUpdate + maxLength; x++) {
//                darkPixels[x + y * mapWidth] = 1;
//            }
//        }
        
//        int x, y;
        radius *= 8;
//        int times = 0;
//        for(y = yUpdate - radius; y < yUpdate - radius / 2; y++) {
//            for(x = xUpdate - radius/2 - times; x < times + xUpdate + radius/2; x++) {
//                darkPixels[x + y * mapWidth] = 1;
//            }
//            times++;
//        }
//        for(y = yUpdate - radius / 2; y < yUpdate + radius / 2; y++) {
//            for(x = xUpdate - times - radius / 2; x < times + xUpdate + radius / 2; x++) {
//                darkPixels[x + y * mapWidth] = 1;
//            }
//        }
//        for(y = yUpdate + radius / 2; y < yUpdate + radius; y++) {
//            for(x = xUpdate - radius/2 - times; x < xUpdate + radius/2 + times; x++) {
//                darkPixels[x + y * mapWidth] = 1;
//            }
//            times--;
//        }
        
        for(int y = yUpdate - radius; y < yUpdate + radius; y++) {
            for(int x = xUpdate - radius; x < xUpdate + radius; x++) {
                darkPixels[x + y * mapWidth] = 1;
            }
        }
    }
}
