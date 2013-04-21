/*
 * The Maze
 * 
 * Game engine originally developed by: (GH) vanZeben or (YT)DesignsbyZephyr
 * Edited by: Colin King
 * Started: April 20 2013
 */

package game;

import game.entities.Player;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

    public final int WIDTH = 160;
    public final int HEIGHT = WIDTH / 12 * 9;
    public final int SCALE = 3;
    public final String NAME = "The Maze: Version 0.0.1";
    public boolean running = false;
    public int tickCount = 0;
    public InputHandler input;
    public Level level;
    public Player player;
    private JFrame frame;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private SpriteSheet spriteSheet = new SpriteSheet("SpriteSheet.png");
    private Screen screen;
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6*6*6];

    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Game().start();
    }

    public void init() {
        int index = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = r * 255 / 5;
                    int gg = g * 255 / 5;
                    int bb = b * 255 / 5;

                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("SpriteSheet.png"));
        input = new InputHandler(this);
        level = new Level(64, 64);
        player = new Player(level, 0, 0, input);
        level.addEntity(player);
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start(); //new Thread that runs the method run()
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / 60.0;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                ticks++;
                tick();
                delta--;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
            }
            frames++;
            render();

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(ticks + " ticks, " + frames + " frames");
                ticks = 0;
                frames = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        level.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        
        int xOffset = player.x - screen.width / 2;
        int yOffset = player.y - screen.height / 2;
        
        level.renderTiles(screen, xOffset, yOffset);
        
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int colorCode = screen.pixels[x + y * screen.width];
                if (colorCode < 255) {
                    pixels[x + y * WIDTH] = colors[colorCode];
                }
            }
        }
        
        level.renderEntities(screen);
        
        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose();
        bs.show();
    }
}
