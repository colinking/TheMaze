package game.entities;

import game.InputHandler;
import game.level.Level;
import java.net.InetAddress;

public class PlayerMP extends Player {
    
    public InetAddress ipAddress;
    public int port;
    public int team;
    
    public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port, int team) {
        super(level, x, y, input, username);
        this.ipAddress = ipAddress;
        this.port = port;
        this.team = team;
    }
    
    public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port, int team) {
        super(level, x, y, null, username);
        this.ipAddress = ipAddress;
        this.port = port;
        this.team = team;
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    
}
