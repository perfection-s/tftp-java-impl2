package tftpserver.handlers;

import tftpserver.PacketHelper;
import tftpserver.db.InMemDb;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SetHandler operates on SET Requests
 */
public class SetHandler implements Runnable {
    private DatagramSocket sock = null;
    private SocketAddress clientAddress = null;
    private String filename = null;

    public SetHandler(int port, SocketAddress socketAddress, String fileName) throws SocketException {
        sock = new DatagramSocket(port);
        clientAddress = socketAddress;
        this.filename = fileName;
    }

    @Override
    public void run() {
        short blockNumber = 0;
        int dataSize = 0;

        List<byte[]> fileContents = new ArrayList<>();

        do {
            try {
                // Step 1: Send an ACK Packet to the client to let it know what port to use
                //         to send the data packets to.
                PacketHelper.sendAck(blockNumber++, sock, clientAddress);

                // Step 2: Wait for the client to send the first data packet
                int bufLength = 516;
                byte[] dataPktBuffer = new byte[bufLength];
                DatagramPacket dataPkt = new DatagramPacket(dataPktBuffer, bufLength);

                sock.receive(dataPkt);

                dataSize = dataPkt.getLength();
                fileContents.add(dataPkt.getData());
                PacketHelper.sendAck(blockNumber++, sock, clientAddress);
            } catch (IOException e) {
                e.printStackTrace();

                // FIXME There are two locations where an IOException can be thrown. It will be good if
                //       if we can handle exceptions from different lines in different try/catch blocks.

                // FIXME Something strange happened. Return for now, but we must investigate what happened
                return;
            }
        } while (dataSize >= 512);

        InMemDb.getDb().saveFile(filename, fileContents);
    }
}
