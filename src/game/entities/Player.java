package game.entities;

import game.*;
import game.gfx.Colors;
import game.gfx.Font;
import game.gfx.Screen;
import game.level.Level;
import game.net.packets.Packet02Move;

public class Player extends Mob {

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    protected boolean isSwimming = false;
    private int tickCount = 0;
    private String username;
    
    public Player(Level level, int x, int y, InputHandler input, String username) {
        super(level, "Player", x, y, 1);
        this.input = input;
        this.username = username;
    }

    @Override
    public boolean hasCollided(int xa, int ya) {
//        int xMin = -2;
//        int xMax = 9;
//        int yMin = -8;
//        int yMax = 7;
        int xMin = 0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 7;
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMin)) {
                return true;
            }
        }
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMax)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMin, y)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMax, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        int xa = 0;
        int ya = 0;
        if(input != null) {
            if (input.up.getPressed()) {
                ya--;
            }
            if (input.down.getPressed()) {
                ya++;
            }
            if (input.left.getPressed()) {
                xa--;
            }
            if (input.right.getPressed()) {
                xa++;
        }
        }
        
        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;
            
            Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
            packet.writeData(Game.game.socketClient);
        } else {
            isMoving = false;
        }

        if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3) {
            isSwimming = true;
        }
        if(isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3) {
            isSwimming = false;
        }
        tickCount++;
    }

    @Override
    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;
        int walkingSpeed = 4; //lower + faster
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;
        
        if (movingDir == 1) {
            xTile += 2;
        } else if (movingDir > 1) {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
        }

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;
        if(isSwimming) {
            int waterColor = 0;
            yOffset += 4;
            if(tickCount % 60 < 15) {
                waterColor = Colors.get(-1, -1, 225, -1);
            } else if(tickCount % 60 >= 15 && tickCount % 60 < 30) {
                yOffset--;
                waterColor = Colors.get(-1, 225, 115, -1);
            } else if(tickCount % 60 >= 30 && tickCount % 60 < 45) {
                waterColor = Colors.get(-1, 115, -1, 225);
            } else {
                yOffset--;
                waterColor = Colors.get(-1, 225, 115, -1);
            }
            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColor, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColor, 0x01, 1);
        }
        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, scale);
        screen.render(xOffset - (modifier * flipTop) + modifier, yOffset, (xTile + 1) + yTile * 32, color, flipTop, scale);
        if(!isSwimming) {
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, color, flipBottom, scale);
            screen.render(xOffset - (modifier * flipBottom) + modifier, yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, color, flipBottom, scale);
        }
        if(username != null) {
            Font.render(username, screen, xOffset - username.length()*8/2 + 8 , yOffset - 10, Colors.get(-1, -1, -1, 555), 1);
        }
    }
    public String getUsername() {
        return username;
    }
}
