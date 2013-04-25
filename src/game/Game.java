/*
 * The Maze
 * 
 * Game engine originally developed by: (GH) vanZeben or (YT)DesignsbyZephyr
 * Edited by: Colin King
 * Started: April 20 2013
 * 
 * Bugs:
 * Water animation activates too early (when he is not yet on the water tile)
 * -maybe usiing bounding boxes and if the outmost box cross the edge of the tile then
 * --the player is in the water?
 * 
 * Future Plans:
 * Maze Generator
 *      Possibly with the ablity to design levels in game?
 * Darkness
 * Menu system
 * new Sprites
 *      possibly with torch?
 *      Different color shirts for dfferent players
 * Teams
 *      Would then need massive worlds
 * Attacking w/ Health
 * Power-ups
 *      swirl animations
 * Sounds? Might be repetitive/annoying
 *      Sound bytes for moving/punching/drop pickup/etc.
 * Enemies
 * Spawns + Exits
 * Credits page
 * 
 * later..?
 * option for a Sandbox..
 * Levels system
 * inventory
 * Goals?
 * 
 * School IP: "10.7.192.176"
 */

package game;

import game.entities.Player;
import game.entities.PlayerMP;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;
import game.net.GameClient;
import game.net.GameServer;
import game.net.IPAddressGrabber;
import game.net.packets.Packet00Login;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game extends Canvas implements Runnable {

    public static Game game;
    public final int WIDTH = 160;
    public final int HEIGHT = WIDTH / 12 * 9;
    public final int SCALE = 3;
    public final String NAME = "The Maze: Version 0.0.1";
    public boolean running = false;
    public int tickCount = 0;
    public InputHandler input;
    public Level level;
    public Player player;
    private final String mapName = "mazeOne.png";
    private final String ipAddress = new IPAddressGrabber().getIP();
    public JFrame frame;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private SpriteSheet spriteSheet = new SpriteSheet("spriteSheet.png");
    private Screen screen;
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6*6*6];
    public GameClient socketClient;
    public GameServer socketServer;
    public WindowHandler windowHandler;
    
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
        game = this;
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
        screen = new Screen(WIDTH, HEIGHT, spriteSheet);
        input = new InputHandler(this);
        level = new Level(mapName);
        windowHandler = new WindowHandler(this);
        player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
        level.addEntity(player);
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
        
        if(socketServer != null) {
            socketServer.addConnection((PlayerMP) player, loginPacket);
        }
//        socketClient.sendData("ping".getBytes());
        
        loginPacket.writeData(socketClient);
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start(); //new Thread that runs the method run()
        
        if(JOptionPane.showConfirmDialog(this, "Do you want to run the server?") == 0) {
            socketServer = new GameServer(this);
            socketServer.start();
        }
        socketClient = new GameClient(this, ipAddress);
        socketClient.start();
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
                frame.setTitle(ticks + " ticks, " + frames + " frames");
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
        level.renderEntities(screen);
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int colorCode = screen.pixels[x + y * screen.width];
                if (colorCode < 255) {
                    pixels[x + y * WIDTH] = colors[colorCode];
                }
            }
        }
        
        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose();
        bs.show();
    }
}
