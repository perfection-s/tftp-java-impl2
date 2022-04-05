package tftpserver.handlers;

import lombok.extern.java.Log;
import tftpserver.PacketHelper;
import tftpserver.db.InMemDb;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * SetHandler operates on SET Requests
 */
@Log
public class PutHandler implements DataTransferHandler {
    private DatagramSocket sock = null;
    private SocketAddress clientAddress = null;
    private String filename = null;

    public PutHandler(int port, SocketAddress socketAddress, String fileName) throws SocketException {
        sock = new DatagramSocket(port);
        clientAddress = socketAddress;
        this.filename = fileName;
    }

    @Override
    public void run() {
        short blockNumber = 0;
        int dataSize = 0;

        /*
         * The incoming datagram packet will have a maximum of
         * 516 bytes. Upto 512 bytes of data and 4 mandatory bytes of
         * OpCode and Block Number
         */
        int bufLength = 516;

        // Handle to hold the byte arrays that define the contents
        // of the file.
        List<byte[]> fileContents = new ArrayList<>();

        // Step 1: Send an ACK Packet to the client to let it know what port to use
        //         to send the data packets to. Also indicates that the server at
        //         this point is ready to accept data packets.
        try {
            PacketHelper.sendAck(blockNumber++, sock, clientAddress);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            do {
                // Step 2: Wait for the client to send the first data packet
                byte[] dataPktBuffer = new byte[bufLength];
                DatagramPacket dataPkt = new DatagramPacket(dataPktBuffer, bufLength);

                sock.receive(dataPkt);
                byte []data = dataPkt.getData();
                ByteBuffer dataBytes = ByteBuffer.allocate(512);

                // start from 4 because the first two bytes are
                // opcode and the next two bytes are block numbers.
                for (int idx = 4; idx < 516 && data[idx] != 0; idx++ ) {
                    dataBytes.put(data[idx]);
                }

                dataSize = dataBytes.position();
                fileContents.add(Arrays.copyOfRange(dataBytes.array(), 0, dataSize));

                /*
                 * TODO
                 *  We're incrementing blocknumber linearly here which is incorrect.
                 *  We must parse a short from the packet at offset 2 to get the
                 *  block number.
                 */
                PacketHelper.sendAck(blockNumber++, sock, clientAddress);
            } while (dataSize >= 512);
        } catch (IOException e) {
            e.printStackTrace();

            // FIXME There are two locations where an IOException can be thrown. It will be good if
            //       if we can handle exceptions from different lines in different try/catch blocks.

            // FIXME Something strange happened. Return for now, but we must investigate what happened
            return;
        } finally {
            sock.close();
        }

        log.log(Level.FINE, "Saved file: " + filename);
        InMemDb.getDb().saveFile(filename, fileContents);
    }
}
