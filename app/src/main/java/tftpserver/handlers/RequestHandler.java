package tftpserver.handlers;

import lombok.extern.java.Log;
import tftpserver.InvalidRequestPacketException;
import tftpserver.PacketHelper;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Random;

/**
 * RequestHandler will accept SET/GET Requests from clients and do the following
 * - Create a new Port that will be used for the remainder of the operation
 * - Create a thread for an appropriate SET/GET handler and pass in the required things:
 * - Client Address (IP:Port)
 * - New Port to operate on
 * - Filename (For Set & Get)
 * - Run the thread
 */
@Log
public class RequestHandler {

    /**
     * Validation Rules
     *   - Op Code must be one of 01/02
     * @throws InvalidRequestPacketException If Packet is not a REQ type packet
     */
    public static void verifyAndHandle(DatagramPacket reqPkt) throws InvalidRequestPacketException{
        final short RRQ = 1;
        final short WRQ = 2;

        short opCode = PacketHelper.getOpCode(reqPkt);
        if (opCode != RRQ && opCode != WRQ) {
            throw new InvalidRequestPacketException("Invalid Request Packet. Illegal OpCode: " + opCode);
        }

        // 1. Get a new port for the Request
        int tid = new Random().ints(2000, 20000)
                .findFirst()
                .orElse(15000);

        try {

            // 2. Setup the SetHandler
            DataTransferHandler handler;

            if (opCode == RRQ) {
                // TODO Vidit to implement his stuff here.
                throw new UnsupportedOperationException("Read Not Implemented Yet..");
            } else {
                handler = new PutHandler(tid,
                        reqPkt.getSocketAddress(),
                        PacketHelper.getFileName(reqPkt));
            }

            // 3. Run the handler
            new Thread(handler).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
