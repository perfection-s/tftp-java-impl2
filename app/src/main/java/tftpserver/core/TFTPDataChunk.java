package tftpserver.core;

import lombok.Getter;

public class TFTPDataChunk implements Comparable<TFTPDataChunk>{
    private final Short blockNumber;
    @Getter
    private final byte[] data;

    public TFTPDataChunk(Short blockNumber, byte[] data) {
        this.blockNumber = blockNumber;
        this.data = data;
    }

    @Override
    public int compareTo(TFTPDataChunk o) {
        return Short.compare(o.blockNumber, this.blockNumber);
    }
}
