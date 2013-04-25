package game.net;

import game.Game;
import game.entities.PlayerMP;
import game.net.packets.Packet;
import game.net.packets.Packet00Login;
import game.net.packets.Packet01Disconnect;
import game.net.packets.Packet02Move;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameClient extends Thread {
    
    private InetAddress ipAddress;
    private DatagramSocket socket;
    Game game;
    
    public GameClient(Game game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException ex) {
            System.err.println("Error: " + ex.getMessage());
        } catch (SocketException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        while(true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                //continously looks for packets from the server socket
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//            System.out.println("SERVER > " + new String(packet.getData()));
        }
    }
    
    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookUpPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                handleLogin(new Packet00Login(data), address, port);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
                game.level.removePlayerMP(((Packet01Disconnect) packet).getUsername());
                break;
            case MOVE:
                packet = new Packet02Move(data);
                this.handleMove((Packet02Move)packet);
        }
    }

    private void handleMove(Packet02Move packet) {
        this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
    }
    
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " + (packet).getUsername() + " has joined the game...");
        PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), (packet).getUsername(), address, port);
        game.level.addEntity(player);
    }
}
