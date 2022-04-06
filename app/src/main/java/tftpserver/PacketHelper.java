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
    final static Short DATA_OP = 3;
    final static Short ACK_OP = 4;

    public static void sendAck(Short blockNumber, DatagramSocket sock, SocketAddress addr) throws IOException {
        ByteBuffer ackByteBuffer = ByteBuffer.allocate(4);
        ackByteBuffer.putShort(ACK_OP);
        ackByteBuffer.putShort(blockNumber);
        sock.send(new DatagramPacket(ackByteBuffer.array(), ACK_PACKET_LEN, addr));
    }

    public static String getFileName(DatagramPacket reqPkt) {
        StringBuilder sb = new StringBuilder();
        byte[] data = reqPkt.getData();
        for (int offset = 2; data[offset] != 0 && offset < reqPkt.getLength(); offset++) {
            sb.append((char)data[offset]);
        }
        return sb.toString();
    }

    public static Short getDataPacketBlockNumber(DatagramPacket datagramPacket) {
        return -1;
    }

    /**
     * Given a Datagram Packet, the method extracts the bytes at the
     * first two locations, parses a Short value from them and returns
     * them as the OpCode.
     * @param pkt Input DatagramPacket from which the OpCode must be extracted
     * @return OpCode
     */
    public static Short getOpCode(DatagramPacket pkt) {
        return ByteBuffer.wrap(pkt.getData()).getShort(0);
    }

    /**
     * Given a Datagram Packet, the method extracts the bytes at the
     * second and the third locations in the data and parses a Short from
     * it.
     *
     * @param pkt Input DatagramPacket from which the block number must be extracted
     * @return Block Number
     */
    public static Short getBlockNumber(DatagramPacket pkt) {
        return ByteBuffer.wrap(pkt.getData()).getShort(2);
    }

    public static Boolean isDataPacket(DatagramPacket pkt) {
        return PacketHelper.getOpCode(pkt).equals(DATA_OP);
    }
}
