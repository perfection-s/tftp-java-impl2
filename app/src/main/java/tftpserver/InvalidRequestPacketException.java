package tftpserver;

public class InvalidRequestPacketException extends Exception {
    public InvalidRequestPacketException(String msg) {
        super(msg);
    }
}
