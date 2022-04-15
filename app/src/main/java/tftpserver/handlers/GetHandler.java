package tftpserver.handlers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.List;

import tftpserver.PacketHelper;
import tftpserver.db.InMemDb;

public class GetHandler implements DataTransferHandler {
    private DatagramSocket sock = null;
    private SocketAddress clientAddress = null;
    private String filename = null;
    private short errorCode=1;
    private short ACK_PACKET_LEN=4;
    byte arr[] = new byte[1024];
    DatagramPacket ackPkt = new DatagramPacket(arr, arr.length);
    public GetHandler(int port, SocketAddress socketAddress, String fileName) throws SocketException {
        sock = new DatagramSocket(port);
        clientAddress = socketAddress;
        this.filename = fileName;
    }
    
    @Override
    public void run() {
        InMemDb DbObj = InMemDb.getDb();
        if(!DbObj.isPresent(filename)) {
            try {
                PacketHelper.sendErr(errorCode,sock,clientAddress);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        else {
            List<byte[]> contentList = DbObj.getFile(filename);
            int l=contentList.size(),i=0;
            while(l>0) {
                try {
                    sock.send(new DatagramPacket(contentList.get(i),ACK_PACKET_LEN , clientAddress));
                    sock.receive(ackPkt);
                    if(ackPkt != null) {
                        l--;
                    }
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
