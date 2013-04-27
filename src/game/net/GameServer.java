package game.net;

import game.Game;
import game.entities.PlayerMP;
import game.net.packets.Packet;
import game.net.packets.Packet00Login;
import game.net.packets.Packet.PacketTypes;
import game.net.packets.Packet01Disconnect;
import game.net.packets.Packet02Move;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer extends Thread {

    private DatagramSocket socket;
    Game game;
    private ArrayList<PlayerMP> connectedPlayers = new ArrayList<>();

    public GameServer(Game game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331); //listens at this port
        } catch (SocketException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                //Server thread continously looks on above port for incoming packets
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//            String msg = new String(packet.getData());
//            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]> " + msg);
//            if(msg.trim().equalsIgnoreCase("ping")) {
//                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookUpPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login) packet).getUsername() + " has connected...");
                PlayerMP player = new PlayerMP(game.level, 100, 100, ((Packet00Login) packet).getUsername(), address, port);
                addConnection(player, (Packet00Login) packet);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left...");
                removeConnection((Packet01Disconnect) packet);
                break;
            case MOVE:
                packet = new Packet02Move(data);
                this.handleMove((Packet02Move) packet);
                break;
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            this.sendData(data, p.ipAddress, p.port);
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for(PlayerMP p: connectedPlayers) {
            if(player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if(p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if(p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                this.sendData(packet.getData(), p.ipAddress, p.port);
                Packet00Login sendPacket = new Packet00Login(p.getUsername(), p.x, p.y);
                this.sendData(sendPacket.getData(), player.ipAddress, player.port);
            }
        }
        if(!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.connectedPlayers.remove(this.getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }
    
    public PlayerMP getPlayerMP(String username) {
        for(PlayerMP p : connectedPlayers) {
            if(p.getUsername().equalsIgnoreCase(username)) {
                return p;
            }
        }
        return null;
    }
    
    public int getPlayerMPIndex(String username) {
        int index = 0;
        for(PlayerMP p : connectedPlayers) {
            if(p.getUsername().equalsIgnoreCase(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    private void handleMove(Packet02Move packet) {
        if(getPlayerMP(packet.getUsername()) != null) {
            int index = getPlayerMPIndex(packet.getUsername());
            PlayerMP player = this.connectedPlayers.get(index);
            player.x = packet.getX();
            player.y = packet.getY();
            player.setNumSteps(packet.getNumSteps());
            player.setIsMoving(packet.isMoving());
            player.setMovingDir(packet.getMovingDir());
            packet.writeData(this);
        }
    }
}
