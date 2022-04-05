package tftpserver.db;

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
    void saveFile(String filename, List<String> content);


    List<String> getFile(String filename);
}
