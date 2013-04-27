package game.net.packets;

import game.net.GameClient;
import game.net.GameServer;


public class Packet03HostCheck extends Packet {
    
    public Packet03HostCheck() {
        super(03);
    }
    
    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("03").getBytes();
    }
}
