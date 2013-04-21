package game.entities;

import game.InputHandler;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public class Player extends Mob {

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);

    public Player(Level level, int x, int y, InputHandler input) {
        super(level, "Player", x, y, 1);
        this.input = input;
    }

    @Override
    public boolean hasCollided(int xa, int ya) {
        return false;
    }

    @Override
    public void tick() {
        int xa = 0;
        int ya = 0;

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

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;
        } else {
            isMoving = false;
        }
    }

    @Override
    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;
        screen.render(xOffset, yOffset, xTile + yTile * 32, color);
        screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32, color);
        screen.render(xOffset, yOffset, xTile + (yTile + 1) * 32, color);
        screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, color);
    }
}
