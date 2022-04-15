package tftpserver.db;

import tftpserver.core.TFTPDataChunk;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Abstraction to define what the DB should support
 */
public interface Db {
    /**
     * Implement saving a file to the DB
     * @param filename Name of the file to use
     * @param content Contents of the file expressed as
     *                strings of maxlength = 512
     *
     * The way the files will be saved is something like this:
     *    Filename <--> [512] String (Block 0)
     *                  [512] String (Block 1)
     *                  ...
     *                  ...
     *                  ...
     *                 [<512] String (Block `n`)
     */
    void saveFile(String filename, List<TFTPDataChunk> content);

    /**
     * Given a filename, return the contents of it as a List<byte[]>
     * @param filename name of the file whose contents must be fetched
     * @return Contents of the file formatted as List<byte[]>
     * @throws FileNotFoundException If requested file isn't found
     */
    List<byte[]> getFile(String filename) throws FileNotFoundException;
    boolean isPresent(String filename); 
}
