package tftpserver.handlers;

import tftpserver.PacketHelper;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.Random;

/**
 * RequestHandler will accept SET/GET Requests from clients and do the following
 *   - Create a new Port that will be used for the remainder of the operation
 *   - Create a thread for an appropriate SET/GET handler and pass in the required things:
 *      - Client Address (IP:Port)
 *      - New Port to operate on
 *      - Filename (For Set & Get)
 *   - Run the thread
 */
public class RequestHandler implements Runnable {
    private DatagramPacket reqPkt = null;

    public RequestHandler(DatagramPacket requestPacket) {
        this.reqPkt = requestPacket;
    }

    @Override
    public void run() {
        // 1. Get a new port for the Request
        // TODO Select the port randomly, but for now just use 15000
        int port = 15000;

        // 2. Since we are assuming only Set now:
        Runnable handler = new SetHandler(port, reqPkt.getSocketAddress(), PacketHelper.getFileName(reqPkt));

        // 3. Run the handler
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
    }
}
