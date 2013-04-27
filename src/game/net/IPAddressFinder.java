/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.net;

import game.Game;
import game.net.packets.Packet03HostCheck;

/**
 *
 * @author kking
 */
public class IPAddressFinder extends Thread {
    
    private Game game;
    
    public IPAddressFinder(Game game) {
        this.game = game;
    }
    
    @Override
    public void run() {
        searchforHosts();
    }
    
    public void searchforHosts() {
        new Packet03HostCheck().writeData(game.socketClient);
        
        
        //send a packet, if recieved, add to list
//        ArrayList<String> addresses = new ArrayList<>();
//        DatagramSocket socket;
//        byte[] data = new byte[1024];
//        DatagramPacket packet;
//        Packet03HostCheck p = new Packet03HostCheck();
////        for(int i = 0; i < 100; i++) {
//            String comp = Integer.toString(51);
//            if(comp.length() == 1) {
//                comp = "0" + comp;
//            }
//            System.out.print(comp + " ");
//            try {
//                
//                socket = new DatagramSocket();
//                packet = new DatagramPacket(p.getData(), p.getData().length, InetAddress.getByName(IPAddressGrabber.getDG() + comp), 1331);
//                socket.send(packet);
//                while(true) {
//                    socket.receive(packet);
//                    System.out.println("- " + packet.getData());
//                }
////                socket.receive(packet); //nurp
////                if(packet != null) {
////                    System.out.println(IPAddressGrabber.getDG() + comp);
////                    addresses.add(IPAddressGrabber.getDG() + comp);
////                }
//            } catch (IOException ex) {
//                System.out.println("Error at (" + comp + "): " + ex.getMessage());
//            }
        }
//        for(String s : addresses) {
//            System.out.println(s);
//        }
//        return null;
//    }
}
