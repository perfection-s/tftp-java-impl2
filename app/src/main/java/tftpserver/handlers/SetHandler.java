package tftpserver.handlers;

import tftpserver.PacketHelper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * SetHandler operates on SET Requests
 */
public class SetHandler implements Runnable {
    public SetHandler(int port, SocketAddress socketAddress, String fileName) {

    }

    @Override
    public void run() {
        short blockNumber = 0;
        int dataSize = 0;

        /*
        DatagramSocket tftpSocket = new DatagramSocket();

        do {
            int bufLength = 516;
            byte[] dataPktBuffer = new byte[bufLength];
            DatagramPacket dataPkt = new DatagramPacket(dataPktBuffer, bufLength);
            tftpSocket.receive(dataPkt);
            String data = new String(dataPkt.getData());
            dataSize = dataPkt.getLength();

            System.out.println("Got Data: " + data);
            PacketHelper.sendAck(blockNumber++, tftpSocket, requestPacket.getSocketAddress());
        } while (dataSize >= 512);*/

    }
}
