package tftpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * PacketHelper class encompasses common TFTPPacket functionality. Examples include:
 *   - Given a packet, classify the type of Packet (ACK, REQ, W/R, ERR)
 *   - Given a Request Packet, extract the filename from it
 *   - Given a Datapacket, extract the block number from it
 *   - Prepare and send an ACK Packet
 *   - Given a packet, extract the Filename from it
 */
public class PacketHelper {
    /**
     * Define some packet related constants
     */
    final static int ACK_PACKET_LEN = 4;

    /**
     * Define Operation identifiers as constants
     */
    final static short ACK_OP = 4;

    public static void sendAck(short blockNumber, DatagramSocket sock, SocketAddress addr) throws IOException {
        ByteBuffer ackByteBuffer = ByteBuffer.allocate(4);
        ackByteBuffer.putShort(ACK_OP);
        ackByteBuffer.putShort(blockNumber);
        sock.send(new DatagramPacket(ackByteBuffer.array(), ACK_PACKET_LEN, addr));
    }

    public static String getFileName(DatagramPacket reqPkt) {
        // FIXME parse the packet and get the filename
        return "somefile";
    }
}
