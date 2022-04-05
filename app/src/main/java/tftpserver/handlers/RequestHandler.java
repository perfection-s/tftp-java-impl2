package tftpserver.handlers;

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
public class RequestHandler implements Runnable {
    private DatagramPacket reqPkt = null;

    public RequestHandler(DatagramPacket requestPacket) {
        this.reqPkt = requestPacket;
    }

    @Override
    public void run() {
        // 1. Get a new port for the Request
        int transferPort = new Random().ints(2000, 20000)
                .findFirst()
                .orElse(15000);

        try {
            // 2. Setup the SetHandler
            Runnable handler = new SetHandler(transferPort,
                    reqPkt.getSocketAddress(),
                    PacketHelper.getFileName(reqPkt));

            // 3. Run the handler
            new Thread(handler).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
