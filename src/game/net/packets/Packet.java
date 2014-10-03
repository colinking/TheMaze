package game.net.packets;

import game.net.GameClient;
import game.net.GameServer;


public abstract class Packet {
    
    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);
        
        private int packetId;
        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }
        public int getId() {
            return packetId;
        }
    }
    
    public byte packetId;
    
    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }
    
    public abstract void writeData(GameClient client);
    
    public abstract void writeData(GameServer server);
    
    public abstract byte[] getData();
    
    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }
    
    public static PacketTypes lookUpPacket(String packetId) {
        try {
            return lookUpPacket(Integer.parseInt(packetId));
        } catch(NumberFormatException i) {
            return PacketTypes.INVALID;
        }
    }
    
    public static PacketTypes lookUpPacket(int id) {
        for(PacketTypes p: PacketTypes.values()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}
